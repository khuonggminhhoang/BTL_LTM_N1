package dao;

import model.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO extends DAO {
    public RoomDAO() {
        super();
    }

    // Thêm phòng mới vào cơ sở dữ liệu
    public boolean createRoom(Room room) {
        try {
            String sql = "INSERT INTO rooms (roomId, playerCount, isFull) VALUES (?, ?, ?)";
            PreparedStatement pstm = this.conn.prepareStatement(sql);

            pstm.setString(1, room.getId());
            pstm.setInt(2, room.getPlayerCount());
            pstm.setBoolean(3, room.isFull());

            int rows = pstm.executeUpdate();
            return rows > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy thông tin phòng từ cơ sở dữ liệu
    public Room getRoomById(String roomId) {
        try {
            String sql = "SELECT * FROM rooms WHERE roomId = ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, roomId);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getString("roomId"),
                        rs.getInt("playerCount"),
                        rs.getBoolean("isFull")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Nếu không tìm thấy phòng
    }

    // Cập nhật thông tin phòng (số lượng người chơi và trạng thái đầy)
    public boolean updateRoom(Room room) {
        try {
            String sql = "UPDATE rooms SET playerCount = ?, isFull = ? WHERE roomId = ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);

            pstm.setInt(1, room.getPlayerCount());
            pstm.setBoolean(2, room.isFull());
            pstm.setString(3, room.getId());

            int rows = pstm.executeUpdate();
            return rows > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa phòng khỏi cơ sở dữ liệu
    public boolean deleteRoom(String roomId) {
        try {
            String sql = "DELETE FROM rooms WHERE roomId = ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, roomId);

            int rows = pstm.executeUpdate();
            return rows > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
