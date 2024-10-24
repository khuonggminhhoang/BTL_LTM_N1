package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import system.Config;
import view.DashboardFrm;

public class Server {
    public static ThreadBus threadBus;
    public static List<RoomController> lstRoomController;

    public static void main(String[] args) {
        try {
            threadBus = new ThreadBus();
            lstRoomController = new ArrayList<>(Config.numberOfRoom);

            // fix 6 phong
            for(int i=0; i<Config.numberOfRoom; ++i) {
                RoomController room = new RoomController(i + 101);
                lstRoomController.add(room);
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
