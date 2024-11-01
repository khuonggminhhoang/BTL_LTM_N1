package dao;

import model.Histories;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO extends DAO{

    public HistoryDAO() {
        super();
    }

    public List<Histories> getAllHistory(Users user) {
        try {
            List<Histories> arr = new ArrayList<>();
            int ownerId = user.getId();
            String sql = "SELECT * FROM histories WHERE ownerId != ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setInt(1, ownerId);  // Thiết lập giá trị cho dấu chấm hỏi
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Histories hst = new Histories(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getTimestamp(3).toLocalDateTime(),
                        rs.getBoolean(4),
                        rs.getInt(5),
                        rs.getInt(6)
                );
                arr.add(hst);
            }

            return arr;
        }
        catch(SQLException e) {
            return null;
        }
    }
}
