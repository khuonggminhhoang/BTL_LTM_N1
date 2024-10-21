package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import system.Config;
import view.DashboardFrm;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Config.PORT);
            System.out.println("Server listenning on port " + Config.PORT);
            DashboardFrm dashboardFrm = new DashboardFrm();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String IPClient = clientSocket.getInetAddress().getHostAddress();
                int PORT = clientSocket.getPort();
                dashboardFrm.setIPClientConnected(IPClient, PORT);

                // tạo luồng riêng xử lý các client
                SocketHandle socketHandle = new SocketHandle(clientSocket);
                socketHandle.run();
            }   

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
