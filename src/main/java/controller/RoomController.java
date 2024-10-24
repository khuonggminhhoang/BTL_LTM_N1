package controller;

public class RoomController {
    private int id;
    private SocketHandle clientSocket1;
    private SocketHandle clientSocket2;

    public RoomController(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public SocketHandle getClientSocket1() {
        return clientSocket1;
    }

    public SocketHandle getClientSocket2() {
        return clientSocket2;
    }

    public boolean setClientSocket(SocketHandle clientSocket) {
        if(this.clientSocket1 == null) {
            this.clientSocket1 = clientSocket;
            return true;
        }

        if(this.clientSocket2 == null) {
            this.clientSocket2 = clientSocket;
            return true;
        }
        return false;
    }

    public int getQuantity() {
        int cnt = 0;
        cnt = this.clientSocket1 != null ? ++cnt : cnt;
        cnt = this.clientSocket2 != null ? ++cnt : cnt;
        return cnt;
    }

    public void broadCast(String message) {
        this.clientSocket1.sendMessage(message);
        this.clientSocket2.sendMessage(message);
    }
}
