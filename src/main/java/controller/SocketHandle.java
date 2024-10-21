package controller;

import dao.RoomDAO;
import dao.UserDAO;
import model.Message;
import model.Room;
import model.Users;

import java.io.*;
import java.net.Socket;

public class SocketHandle implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Users user;

    public SocketHandle(Socket clientSocket) {
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
            UserDAO userDao = new UserDAO();
            RoomDAO roomDao = new RoomDAO();
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
                        System.out.println("Sending LOGIN_SUCCESS to client");
                        this.oos.writeObject(sendMessage);
                    }
                    else {
                        Message sendMessage = new Message("LOGIN_FAIL", "Sai tên tài khoản hoặc mật khẩu");
                        System.out.println("Sending LOGIN_FAIL to client");
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

                case "CREATE_ROOM": {
                    try {
                        Room room = (Room) receiveMessage.getObject();
                        boolean isCreated = roomDao.createRoom(room);
                        if (isCreated) {
                            Message sendMessage = new Message("CREATE_ROOM_SUCCESS", room);
                            System.out.println("Sending CREATE_ROOM_SUCCESS to client"); // Thêm dòng này
                            this.oos.writeObject(sendMessage);
                            this.oos.flush(); // Đảm bảo dữ liệu được gửi
                        } else {
                            Message sendMessage = new Message("CREATE_ROOM_FAIL", "Tạo phòng thất bại");
                            System.out.println("Sending CREATE_ROOM_FAIL to client"); // Thêm dòng này
                            this.oos.writeObject(sendMessage);
                            this.oos.flush(); // Đảm bảo dữ liệu được gửi
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // In ra lỗi nếu có
                    }
                    break;
                }


                case "JOIN_ROOM": {
                    // Tham gia phòng
                    String roomId = (String) receiveMessage.getObject();
//                    RoomDAO roomDao = new RoomDAO();
                    Room room = roomDao.getRoomById(roomId);
                    if (room != null && room.getPlayerCount() < 2) {
                        // Cập nhật số người chơi
                        room.setPlayerCount(room.getPlayerCount() + 1);
                        roomDao.updateRoom(room);

                        Message sendMessage = new Message("JOIN_ROOM_SUCCESS", room);
                        this.oos.writeObject(sendMessage);
                    } else {
                        Message sendMessage = new Message("JOIN_ROOM_FAIL", "Phòng đầy hoặc không tồn tại");
                        this.oos.writeObject(sendMessage);
                    }
                    break;
                }

                case "LEAVE_ROOM": {
                    // Rời khỏi phòng
                    String roomId = (String) receiveMessage.getObject();
//                    RoomDAO roomDao = new RoomDAO();
                    Room room = roomDao.getRoomById(roomId);
                    if (room != null) {
                        // Giảm số người chơi
                        room.setPlayerCount(room.getPlayerCount() - 1);
                        roomDao.updateRoom(room);

                        Message sendMessage = new Message("LEAVE_ROOM_SUCCESS", room);
                        this.oos.writeObject(sendMessage);
                    } else {
                        Message sendMessage = new Message("LEAVE_ROOM_FAIL", "Không thể rời khỏi phòng");
                        this.oos.writeObject(sendMessage);
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
