package controller;

import model.Room;
import model.Users;

import java.util.HashMap;

public class RoomController {
    private HashMap<String, Room> rooms = new HashMap<>();

    //create a new room
    public Room createRoom(String roomId) {
        if (!rooms.containsKey(roomId)) {
            Room newRoom = new Room(roomId);
            rooms.put(roomId, newRoom);
            return newRoom;
        }
        return null; // Phòng đã tồn tại
    }

    // add new player to room
    public boolean addPlayerToRoom(String roomId, Users player) {
        Room room = rooms.get(roomId);
        if (room != null && !room.isFull()) {
            return room.addPlayer(player);
        }
        return false; // Phòng không tồn tại hoặc đã đầy
    }

    // Lấy danh sách các phòng
    public HashMap<String, Room> getRooms() {
        return rooms;
    }
}
