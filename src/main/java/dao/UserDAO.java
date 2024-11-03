package dao;

import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {
    public UserDAO() {
        super();
    }

    public Users getOne(int id) {
        try {
            String sql = "SELECT * FROM users WHERE id=?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Users verifyUser(Users user) {
        try {
            String sql = "SELECT * FROM users WHERE username=? AND password=? ";
            PreparedStatement pstm = this.conn.prepareStatement(sql);

            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getPassword());
            ResultSet rs = pstm.executeQuery();

            if(rs.next()) {
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
        }
        catch( Exception e) {
            return null;
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
            return false;
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
            return false;
        }
    }

    public boolean updateOnlineUser(Users user, boolean online) {
        try {
            String username = user.getUsername();
            String sql = "UPDATE users SET isOnline=? WHERE username=?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setBoolean(1, online);
            pstm.setString(2, username);

            int tmp = pstm.executeUpdate();
            return tmp > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updatePlayingUser(Users user, boolean playing) {
        try {
            String username = user.getUsername();
            String sql = "UPDATE users SET isPlaying=? WHERE username=?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setBoolean(1, playing);
            pstm.setString(2, username);

            int tmp = pstm.executeUpdate();
            return tmp > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateUserGameResult(Users user, boolean isWin) {
        try {
            String username = user.getUsername();
            // Câu lệnh SQL để cập nhật kết quả
            String sql = "UPDATE users SET numberOfGame = numberOfGame + 1 "
                    + (isWin ? ", numberOfWin = numberOfWin + 1" : "") + " "
                    + "WHERE username = ?";

            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, username);

            int tmp = pstm.executeUpdate();
            return tmp > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
            return false; // Trả về false nếu có lỗi
        }
    }

    public boolean increaseNumberOfGame(Users user) {
        try {
            String username = user.getUsername();
            String sql = "UPDATE users SET numberOfGame = numberOfGame + 1 WHERE username = ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, username);
            int tmp = pstm.executeUpdate();

            return tmp > 0;
        } catch (SQLException e) {
            return false;
        }
    }


    public List<Users> getAllUser(Users user) {
        try {
            List<Users> arr = new ArrayList<>();
            String username = user.getUsername();
            String sql = "SELECT * FROM users WHERE username != ?";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            pstm.setString(1, username);  // Thiết lập giá trị cho dấu chấm hỏi
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Users u = new Users(
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
                arr.add(u);
            }

            return arr;
        }
        catch(SQLException e) {
            return null;
        }
    }

    public List<Users> getListRank() {
        try {
         List<Users> arr = new ArrayList<>();
         String sql = "SELECT * FROM users ORDER BY numberOfWin ASC";
         PreparedStatement pstm = this.conn.prepareStatement(sql);
         ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Users u = new Users(
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
            arr.add(u);
        }

        return arr;

        }
        catch(SQLException e) {
            return null;
        }
    }
}
