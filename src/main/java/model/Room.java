package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private int id;
    private Users user1;
    private Users user2;
    List<Questions> lstQuestion;
    private int currentQuestionIndex = 0;  // theo dõi câu hỏi hiện tại

    public Room() {}

    public Room(int id) {
        this.id = id;
        this.lstQuestion = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Users getUser1() {
        return user1;
    }

    public Users getUser2() {
        return user2;
    }

    public void setUser1(Users user1) {
        this.user1 = user1;
    }

    public void setUser2(Users user2) {
        this.user2 = user2;
    }

    public List<Questions> getLstQuestion() {
        return lstQuestion;
//        this.currentQuestionIndex = 0;
    }

    public void setLstQuestion(List<Questions> lstQuestion) {
        this.lstQuestion = lstQuestion;
    }

    public Questions getCurrentQuestion() {
        if (currentQuestionIndex < lstQuestion.size()) {
            return lstQuestion.get(currentQuestionIndex);
        }
        return null; // Trả về null nếu không còn câu hỏi nào
    }

    public void moveToNextQuestion() {
        if (currentQuestionIndex < lstQuestion.size() - 1) {
            currentQuestionIndex++;
        } else {
            currentQuestionIndex = -1; // Kết thúc danh sách câu hỏi
        }
    }

    public int getQty() {
        int quantity = 0;
        quantity = this.user1 != null ? ++quantity : quantity;
        quantity = this.user2 != null ? ++quantity : quantity;
        return quantity;
    }

    public void setUser(Users user) {
        if(this.user1 == null) {
            this.user1 = user;
        }else if(this.user2 == null) {
            this.user2 = user;
        }
    }

    public boolean containsUser(Users user) {
        return user1.equals(user) || user2.equals(user); // giả sử room có hai người chơi: user1 và user2
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex + 1 < lstQuestion.size();
    }
}
