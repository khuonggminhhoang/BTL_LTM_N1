package controller;

import model.Questions;
import model.Users;

import java.util.List;

public class RoomController {
    private int id;
    private SocketHandle clientSocket1;
    private SocketHandle clientSocket2;
    List<Questions> lstQuestion;
    private int currentQuestionIndex = 0;

    public RoomController(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public SocketHandle getCompetitor(int idOwner) {
        return this.clientSocket1.getId() != idOwner ? this.clientSocket1 : this.clientSocket2;
    }

    public boolean setClientSocket(SocketHandle clientSocket) {
        if(this.clientSocket1 == null) {
            this.clientSocket1 = clientSocket;
            return true;
        }

        if(this.clientSocket2 == null) {
            this.clientSocket2 = clientSocket;
            return true;
        }
        return false;
    }

    public int getQuantity() {
        int cnt = 0;
        cnt = this.clientSocket1 != null ? ++cnt : cnt;
        cnt = this.clientSocket2 != null ? ++cnt : cnt;
        return cnt;
    }

    public void broadCast(SocketHandle socketHandle,String message) {
        if(this.clientSocket1 != socketHandle) {
            this.clientSocket1.sendMessage(message);
        }
        else {
            this.clientSocket2.sendMessage(message);
        }
    }

    public void boardCast2(SocketHandle socketHandle) {
        System.out.println("send broad cast 2");
        if (this.clientSocket1 != socketHandle) {
            this.clientSocket1.sendMessage2();

        } else {
            this.clientSocket2.sendMessage2();
        }
    }

    public void boardCast3(SocketHandle socketHandle) {
        if (this.clientSocket1 != socketHandle) {
            this.clientSocket1.sendMessage3();

        } else {
            this.clientSocket2.sendMessage3();
        }
    }

    public void removeSocketHandle(SocketHandle socketHandle) {
        this.clientSocket1 = this.clientSocket1 == socketHandle ? null : this.clientSocket1;
        this.clientSocket2 = this.clientSocket2 == socketHandle ? null : this.clientSocket2;
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

    public boolean containsUser(SocketHandle userSocket) {
        return this.clientSocket1 == userSocket || this.clientSocket2 == userSocket;
    }

    public boolean hasNextQuestion() {
        return currentQuestionIndex + 1 < lstQuestion.size();
    }

    public void moveToNextQuestion() {
        if (currentQuestionIndex < lstQuestion.size() - 1) {
            currentQuestionIndex++;
        } else {
            currentQuestionIndex = -1; // Kết thúc danh sách câu hỏi
        }
    }

    public SocketHandle getOpponent(SocketHandle socketHandle) {
        if(clientSocket1 != socketHandle) return clientSocket1;
        else return clientSocket2;
    }
}
