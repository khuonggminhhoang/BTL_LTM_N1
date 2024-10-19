package controller;

import dao.UserDAO;
import model.Users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable{
    private Users user;
    private int clientNumber;
    private Socket socketOfServer;
    private UserDAO userDAO;
    private BufferedReader is;
    private BufferedWriter os;

    public ServerThread (Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        this.userDAO = new UserDAO();


    }
}
