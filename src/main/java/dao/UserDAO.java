package dao;

import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            e.printStackTrace();
        }

        return null;
    }
}
