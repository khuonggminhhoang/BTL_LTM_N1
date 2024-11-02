package dao;

import model.Histories;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public boolean addHistory(Histories history) {
        try {
            String sqlz = "INSERT INTO histories(timeStart, timeEnd, isWin, opponentId, ownerId) values (?, ?, ?, ?, ?)";
            PreparedStatement pstmz = this.conn.prepareStatement(sqlz);
            pstmz.setTimestamp(1, Timestamp.valueOf(history.getTimeStart()));
            pstmz.setTimestamp(2, Timestamp.valueOf(history.getTimeEnd()));
            pstmz.setInt(3, history.isWin() ? 1 : 0);
            pstmz.setInt(4, history.getOpponentId());
            pstmz.setInt(5, history.getOwnerId());

            System.out.println(Timestamp.valueOf(history.getTimeStart()));
            System.out.println(Timestamp.valueOf(history.getTimeEnd()));
            System.out.println(history.isWin());
            System.out.println(history.getOpponentId());
            System.out.println(history.getOwnerId());

            int tmp = pstmz.executeUpdate();
            return tmp > 0;             // thành công
        } catch (SQLException e) {
            return false;
        }
    }
}
