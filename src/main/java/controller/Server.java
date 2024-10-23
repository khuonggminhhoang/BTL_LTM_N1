package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Room;
import system.Config;
import view.DashboardFrm;

public class Server {
    public static ThreadBus threadBus;
    public static List<Room> lstRoom;

    public static void main(String[] args) {
        try {
            threadBus = new ThreadBus();
            lstRoom = new ArrayList<>(Config.numberOfRoom);

            // fix 6 phong
            for(int i=0; i<Config.numberOfRoom; ++i) {
                Room room = new Room(i + 101);
                lstRoom.add(room);
            }

            ServerSocket serverSocket = new ServerSocket(Config.PORT);
            System.out.println("Server listenning on port " + Config.PORT);
            DashboardFrm dashboardFrm = new DashboardFrm();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int idClientSocket = threadBus.getSize();

                String IPClient = clientSocket.getInetAddress().getHostAddress();
                int PORT = clientSocket.getPort();
                dashboardFrm.setIPClientConnected(IPClient, PORT);

                // tạo luồng riêng xử lý các client
                SocketHandle socketHandle = new SocketHandle(idClientSocket,clientSocket);
                threadBus.addSocketHandle(socketHandle);
                System.out.println(threadBus.getSize() + "luồng đang chạy");
                socketHandle.run();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
