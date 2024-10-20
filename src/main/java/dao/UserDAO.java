package dao;

import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO {
    public UserDAO() {
        super();
    }

    public Users verifyUser(Users user) {
        try {
            String sql = "SELECT * FROM users WHERE username=? AND password=? ";
            PreparedStatement pstm = this.conn.prepareStatement(sql);

            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getPassword());
            ResultSet rs = pstm.executeQuery();

            rs.next();
            return new Users(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getInt(5),
                    rs.getInt(6),
                    rs.getBoolean(7),
                    rs.getBoolean(8),
                    rs.getString(9)
            );
        }
        catch( Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean register(Users user) {
        try {
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, user.getUsername());
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                return false;       // user đã tồn tại
            }

            String sqlz = "INSERT INTO users(username, password, numberOfGame, numberOfWin, numberOfDraw, isOnline, isPlaying, avatar) values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmz = this.conn.prepareStatement(sqlz);
            pstmz.setString(1, user.getUsername());
            pstmz.setString(2, user.getPassword());
            pstmz.setInt(3, 0);
            pstmz.setInt(4, 0);
            pstmz.setInt(5, 0);
            pstmz.setBoolean(6, false);
            pstmz.setBoolean(7, false);
            pstmz.setString(8, user.getAvatar());

            int tmp = pstmz.executeUpdate();
            return tmp > 0;             // đăng ký thành công
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changePassword(Users user) {
        try {
            String username = user.getUsername();
            String newPassword = user.getPassword();

            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            if(!rs.next()) {
                return false;
            }

            String oldPassword = rs.getString(3);
            if(newPassword.equals(oldPassword)) {
                return false;
            }

            String sqlUpdate = "UPDATE users SET password = ? WHERE id = " + rs.getInt(1);
            PreparedStatement pstmUpdate = this.conn.prepareStatement(sqlUpdate);
            pstmUpdate.setString(1, newPassword);
            int tmp = pstmUpdate.executeUpdate();
            return tmp > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
