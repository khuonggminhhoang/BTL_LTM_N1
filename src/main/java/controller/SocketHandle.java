package controller;

import dao.UserDAO;
import model.Message;
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
                        Message sendMessage = new Message("LOGIN_FAIL", null);
                        this.oos.writeObject(sendMessage);
                    }
                    break;
                }
                case "REGISTER_REQUEST": {
                    Users user = (Users) receiveMessage.getObject();
                    boolean isRegisted = userDao.register(user);
                    if(isRegisted) {
                        Message sendMessage = new Message("REGISTER_SUCCESS", true);
                        this.oos.writeObject(sendMessage);
                    }
                    else {
                        Message sendMessage = new Message("REGISTER_FAIL", false);
                        this.oos.writeObject(sendMessage);
                    }
                }

            }


            // đóng socket
            this.closeSocket();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
