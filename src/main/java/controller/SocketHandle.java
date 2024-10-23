package controller;

import dao.QuestionDAO;
import dao.UserDAO;
import model.Message;
import model.Room;
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

    public SocketHandle(int id,Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void closeSocket() {
        try {
            this.ois.close();
            this.oos.close();
            this.clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            QuestionDAO questionDAO = new QuestionDAO();
            UserDAO userDao = new UserDAO();
            this.ois = new ObjectInputStream(this.clientSocket.getInputStream());
            this.oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
            System.out.println("new thread started with id: " + this.clientSocket);

            // nhận dữ liệu từ client
            Message receiveMessage = (Message) this.ois.readObject();
            String type = receiveMessage.getType();

            switch (type) {
                case "LOGIN_REQUEST": {
                    Users user = (Users) receiveMessage.getObject();
                    if(userDao.verifyUser(user) != null) {
                        this.user = userDao.verifyUser(user);
                        // gửi dữ liệu sang client
                        Message sendMessage = new Message("LOGIN_SUCCESS", this.user);
                        this.oos.writeObject(sendMessage);
                    }
                    else {
                        Message sendMessage = new Message("LOGIN_FAIL", "Sai tên tài khoản hoặc mật khẩu");
                        this.oos.writeObject(sendMessage);
                    }
                    break;
                }
                case "REGISTER_REQUEST": {
                    Users user = (Users) receiveMessage.getObject();
                    boolean isRegisted = userDao.register(user);
                    if(isRegisted) {
                        Message sendMessage = new Message("REGISTER_SUCCESS", "Đăng ký tài khoản thành công");
                        this.oos.writeObject(sendMessage);
                    }
                    else {
                        Message sendMessage = new Message("REGISTER_FAIL", "Tên tài khoản đã tồn tại");
                        this.oos.writeObject(sendMessage);
                    }
                    break;
                }
                case "CHANGE_PASSWORD_REQUEST": {
                    Users user = (Users) receiveMessage.getObject();
                    System.out.println(user.getUsername());
                    boolean isChanged = userDao.changePassword(user);
                    if(isChanged) {
                        Message sendMessage = new Message("CHANGE_PASSWORD_SUCCESS", "Thay đổi mật khẩu thành công");
                        this.oos.writeObject(sendMessage);
                    }
                    else {
                        Message sendMessage = new Message("CHANGE_PASSWORD_FAIL", "Tên tài khoản không tồn tại hoặc mật khẩu mới bị trùng");
                        this.oos.writeObject(sendMessage);
                    }

                    break;
                }

                case "GET_ROOMS_REQUEST": {
                    Message sendMessage = new Message("GET_ROOMS_SUCCESS", Server.lstRoom);
                    this.oos.writeObject(sendMessage);
                    break;
                }

                case "JOIN_ROOM_REQUEST": {
                    HashMap<Integer, Users> map = (HashMap<Integer, Users>) receiveMessage.getObject();
                    int idRoom = (Integer) map.keySet().toArray()[0];
                    Users user = map.get(idRoom);


                    for(Room room : Server.lstRoom) {
                        if(room.getId() == idRoom) {
                            if(room.getQty() >= 2) {
                                Message sendMessage = new Message("JOIN_ROOM_FAIL", null);
                                this.oos.writeObject(sendMessage);
                                break;
                            }
                            room.setUser(user);
                            room.setLstQuestion(questionDAO.getThreeQuestion());
                            Message sendMessage = new Message("JOIN_ROOM_SUCCESS", room);
                            this.oos.writeObject(sendMessage);
                            break;
                        }
                    }



                    break;
                }


            }

            // đóng socket
            this.closeSocket();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
