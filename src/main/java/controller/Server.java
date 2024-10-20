package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import system.Config;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Config.PORT);
            System.out.println("Server listenning on port " + Config.PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress().getHostAddress() + " connected");

                // tạo luồng riêng xử lý các client
                SocketHandle socketHandle = new SocketHandle(clientSocket);
                socketHandle.run();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
