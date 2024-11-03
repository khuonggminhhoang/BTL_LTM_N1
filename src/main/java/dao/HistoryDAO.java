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

            UserDAO userDAO = new UserDAO();



            while (rs.next()) {
                Users owner = userDAO.getOne(rs.getInt(5));
                Users opponent = userDAO.getOne(rs.getInt(6));

                Histories hst = new Histories(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime(),
                        rs.getTimestamp(3).toLocalDateTime(),
                        rs.getBoolean(4),
                        owner,
                        opponent
                );
                arr.add(hst);
            }

            return arr;
        }
        catch(SQLException e) {
            e.printStackTrace();
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
            pstmz.setInt(4, history.getOpponent().getId());
            pstmz.setInt(5, history.getOwner().getId());

            int tmp = pstmz.executeUpdate();
            return tmp > 0;             // thành công
        } catch (SQLException e) {
            return false;
        }
    }
}
