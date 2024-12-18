package controller;

import java.util.ArrayList;
import java.util.List;

public class ThreadBus {
    private List<SocketHandle> listSocketHandle;

    public ThreadBus() {
        this.listSocketHandle = new ArrayList<>();
    }

    public List<SocketHandle> getListSocketHandle() {
        return this.listSocketHandle;
    }

    public void addSocketHandle(SocketHandle socketHandle) {
        this.listSocketHandle.add(socketHandle);
    }

    public int getSize() {
        return this.listSocketHandle.size();
    }

    public void broadcast(SocketHandle socketHandle, String message) {
        for(SocketHandle socket : this.listSocketHandle) {
            if(socket != socketHandle) {
                socket.write("WORLD_CHAT_RESPONSE", message);
            }
        }
    }
}
