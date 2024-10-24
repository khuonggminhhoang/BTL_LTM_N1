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

    public SocketHandle getCompetitor(int idOwner) {
        return this.clientSocket1.getId() != idOwner ? this.clientSocket1 : this.clientSocket2;
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

    public void broadCast(SocketHandle socketHandle,String message) {
        if(this.clientSocket1 != socketHandle) {
            this.clientSocket1.sendMessage(message);
        }
        else {
            this.clientSocket2.sendMessage(message);
        }
    }

    public void removeSocketHandle(SocketHandle socketHandle) {
        this.clientSocket1 = this.clientSocket1 == socketHandle ? null : this.clientSocket1;
        this.clientSocket2 = this.clientSocket2 == socketHandle ? null : this.clientSocket2;
    }
}
