package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private int id;
    private Users user1;
    private Users user2;
    List<Questions> lstQuestion;

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
    }

    public void setLstQuestion(List<Questions> lstQuestion) {
        this.lstQuestion = lstQuestion;
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
}
