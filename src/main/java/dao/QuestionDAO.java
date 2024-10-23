package dao;

import model.Questions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO extends DAO {
    public QuestionDAO() {
        super();
    }

    public List<Questions> getThreeQuestion() {
        try {
            List<Questions> arr = new ArrayList<>();
            String sql = "SELECT * FROM questions ORDER BY RAND() LIMIT 3";
            PreparedStatement pstm = this.conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String answer = rs.getString("answer");
                String imgPath = rs.getString("imgPath");
                String hint = rs.getString("hint");

                Questions questions = new Questions(id, answer, imgPath, hint);
                arr.add(questions);
            }

            return arr;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
