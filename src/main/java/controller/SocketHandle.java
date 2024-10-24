package controller;

import dao.QuestionDAO;
import dao.UserDAO;
import model.Message;
import model.Users;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class SocketHandle implements Runnable{
    private int id;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Users user;
    private boolean isClosed;
    private RoomController roomController;

    public SocketHandle(int id,Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
        this.ois = new ObjectInputStream(clientSocket.getInputStream());
        this.isClosed = false;
    }

    public int getId() {
        return id;
    }

    public void closeSocket() {
        try {
            if (this.ois != null) this.ois.close();
            if (this.oos != null) this.oos.close();
            if (this.clientSocket != null) this.clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            Message messageObj = new Message("ANSWER_TEMP_RESPONSE", message);
            this.oos.writeObject(messageObj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // gửi dữ liệu sang client
    public void write(String type, Object object) {
        try {
            Message sendMessage = new Message(type, object);
            this.oos.writeObject(sendMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        QuestionDAO questionDAO = new QuestionDAO();
        UserDAO userDao = new UserDAO();
        System.out.println("new thread started with id: " + this.clientSocket);

        try {
            while (!isClosed) {
                // nhận dữ liệu từ client
                Message receiveMessage = (Message) this.ois.readObject();
                String type = receiveMessage.getType();

                switch (type) {
                    case "LOGIN_REQUEST": {
                        Users user = (Users) receiveMessage.getObject();
                        if (userDao.verifyUser(user) != null) {
                            this.user = userDao.verifyUser(user);
                            userDao.updateOnlineUser(this.user, true);
                            // gửi dữ liệu sang client
                            this.write("LOGIN_SUCCESS", this.user);
                        } else {
                            this.write("LOGIN_FAIL", "Sai tên tài khoản hoặc mật khẩu");
                        }
                        break;
                    }
                    case "REGISTER_REQUEST": {
                        Users user = (Users) receiveMessage.getObject();
                        boolean isRegisted = userDao.register(user);
                        if (isRegisted) {
                            this.write("REGISTER_SUCCESS", "Đăng ký tài khoản thành công");
                        } else {
                            this.write("REGISTER_FAIL", "Tên tài khoản đã tồn tại");
                        }
                        break;
                    }
                    case "CHANGE_PASSWORD_REQUEST": {
                        Users user = (Users) receiveMessage.getObject();
                        System.out.println(user.getUsername());
                        boolean isChanged = userDao.changePassword(user);
                        if (isChanged) {
                            this.write("CHANGE_PASSWORD_SUCCESS", "Thay đổi mật khẩu thành công");
                        } else {
                            this.write("CHANGE_PASSWORD_FAIL", "Tên tài khoản không tồn tại hoặc mật khẩu mới bị trùng");
                        }

                        break;
                    }

                    case "GET_ROOMS_REQUEST": {
                        HashMap<Integer, Integer> mapRoom = new HashMap<>();
                        for (RoomController roomController : Server.lstRoomController) {
                            mapRoom.put(roomController.getId(), roomController.getQuantity());
                        }
                        this.write("GET_ROOMS_SUCCESS", mapRoom);
                        break;
                    }

                    // type: JOIN_ROOM_REQUEST | object: idRoom
                    case "JOIN_ROOM_REQUEST": {
                        int idRoom = Integer.parseInt(receiveMessage.getObject() + "");
                        for (RoomController roomController : Server.lstRoomController) {
                            if (roomController.getId() == idRoom) {
                                boolean flag = roomController.setClientSocket(this);
                                if (!flag) {
                                    this.write("JOIN_ROOM_FAIL", "Phòng full");
                                    break;
                                }
                                userDao.updatePlayingUser(this.user, true);
                                userDao.increaseNumberOfGame(this.user);
                                this.write("JOIN_ROOM_SUCCESS", "Join phòng thành công");
                                this.roomController = roomController;
                                break;
                            }
                        }


                        break;
                    }

                    // type: ANSWER_TEMP_REQUEST | object: null
                    case "ANSWER_TEMP_REQUEST": {
                        String receiveAnswer = (String) receiveMessage.getObject();
                        this.roomController.broadCast(this, receiveAnswer);
                        break;
                    }


                }
            }


        } catch (IOException | ClassNotFoundException e) {
            // đóng socket
            this.isClosed = true;
            Server.threadBus.getListSocketHandle().remove(this);
            System.out.println("1 luồng đã bị xóa");

            // cập nhật trạng thái user thành offline
            if(this.user != null) {
                userDao.updateOnlineUser(this.user, false);
                userDao.updatePlayingUser(this.user, false);
            }

            // xóa socketHandle khỏi room
            if(this.roomController != null) {
                roomController.removeSocketHandle(this);
                System.out.println(this.roomController.getQuantity());
            }

        }
    }
}
