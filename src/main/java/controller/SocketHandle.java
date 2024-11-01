package controller;

import dao.HistoryDAO;
import dao.QuestionDAO;
import dao.UserDAO;
import model.Histories;
import model.Message;
import model.Questions;
import model.Users;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocketHandle implements Runnable{
    private int id;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Users user;
    private boolean isClosed;
    private RoomController roomController;

    public SocketHandle(int id,Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
        this.ois = new ObjectInputStream(clientSocket.getInputStream());
        this.isClosed = false;
    }

    public Users getUser() {
        return this.user;
    }

    public int getId() {
        return id;
    }

    public void closeSocket() {
        try {
            if (this.ois != null) this.ois.close();
            if (this.oos != null) this.oos.close();
            if (this.clientSocket != null) this.clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            Message messageObj = new Message("ANSWER_TEMP_RESPONSE", message);

            this.oos.writeObject(messageObj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage2() {
        try {
            Message messageObj = new Message("OTHER_ANSWER_CORRECT", roomController.getCurrentQuestion());

            this.oos.writeObject(messageObj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage3() {
        try {
            Message messageObj = new Message("OTHER_USER", this.user);

            this.oos.writeObject(messageObj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // gửi dữ liệu sang client
    public void write(String type, Object object) {
        try {
            Message sendMessage = new Message(type, object);
            this.oos.writeObject(sendMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        QuestionDAO questionDAO = new QuestionDAO();
        UserDAO userDao = new UserDAO();
        HistoryDAO historyDAO = new HistoryDAO();
        System.out.println("new thread started with id: " + this.clientSocket);
        try {
            while (!isClosed) {
                // nhận dữ liệu từ client
                Message receiveMessage = (Message) this.ois.readObject();
                String type = receiveMessage.getType();

                switch (type) {
                    case "LOGIN_REQUEST": {
                        Users user = (Users) receiveMessage.getObject();
                        if (userDao.verifyUser(user) != null) {
                            this.user = userDao.verifyUser(user);
                            userDao.updateOnlineUser(this.user, true);
                            // gửi dữ liệu sang client
                            this.write("LOGIN_SUCCESS", this.user);
                        } else {
                            this.write("LOGIN_FAIL", "Sai tên tài khoản hoặc mật khẩu");
                        }
                        break;
                    }
                    case "REGISTER_REQUEST": {
                        Users user = (Users) receiveMessage.getObject();
                        boolean isRegisted = userDao.register(user);
                        if (isRegisted) {
                            this.write("REGISTER_SUCCESS", "Đăng ký tài khoản thành công");
                        } else {
                            this.write("REGISTER_FAIL", "Tên tài khoản đã tồn tại");
                        }
                        break;
                    }
                    case "CHANGE_PASSWORD_REQUEST": {
                        Users user = (Users) receiveMessage.getObject();
                        System.out.println(user.getUsername());
                        boolean isChanged = userDao.changePassword(user);
                        if (isChanged) {
                            this.write("CHANGE_PASSWORD_SUCCESS", "Thay đổi mật khẩu thành công");
                        } else {
                            this.write("CHANGE_PASSWORD_FAIL", "Tên tài khoản không tồn tại hoặc mật khẩu mới bị trùng");
                        }

                        break;
                    }

                    case "GET_ROOMS_REQUEST": {
                        HashMap<Integer, Integer> mapRoom = new HashMap<>();
                        for (RoomController roomController : Server.lstRoomController) {
                            mapRoom.put(roomController.getId(), roomController.getQuantity());
                        }
                        this.write("GET_ROOMS_SUCCESS", mapRoom);
                        break;
                    }

                    case "GET_ROOM_REQUEST": {
                        System.out.println("get room request + " + roomController.getQuantity());
                        this.write("GET_ROOM_REQUEST", roomController.getQuantity());
                        break;
                    }

//                    case "GET_OTHER_USER" : {
//                        roomController.boardCast3(this);
//                    }

                    // type: JOIN_ROOM_REQUEST | object: idRoom
                    case "JOIN_ROOM_REQUEST": {
                        int idRoom = Integer.parseInt(receiveMessage.getObject() + "");
                        for (RoomController roomController : Server.lstRoomController) {
                            if (roomController.getId() == idRoom) {
                                boolean flag = roomController.setClientSocket(this);
                                if (!flag) {
                                    this.write("JOIN_ROOM_FAIL", "Phòng full");
                                    break;
                                }
                                userDao.updatePlayingUser(this.user, true);
                                this.write("JOIN_ROOM_SUCCESS", "Join phòng thành công");
                                this.roomController = roomController;
                                this.roomController.setLstQuestion(questionDAO.getThreeQuestion());

                                System.out.println(roomController);
                                break;
                            }
                        }


                        break;
                    }
                    case "START_GAME": {
                        // Tìm RoomController chứa người chơi hiện tại
//                        RoomController currentRoom = findRoomByUser(this);
                        if (roomController == null) {
                            System.out.println("Không tìm thấy phòng cho người chơi.");
                            break;
                        }

                        // Lấy câu hỏi hiện tại
                        Questions currentQuestion = roomController.getCurrentQuestion();
                        if (currentQuestion == null) {
                            System.out.println("Không có câu hỏi nào để bắt đầu trò chơi.");
                            break;
                        }
                        System.out.println(currentQuestion.getAnswer());

                        // Tạo một Message để gửi câu hỏi
                        Message questionMessage = new Message("QUESTION", currentQuestion);
                        this.oos.writeObject(questionMessage);

                        Message otherUser = new Message("OTHER_USER", this.roomController.getOpponent(this).getUser());
                        this.oos.writeObject(otherUser);

                        System.out.println("Đã gửi câu hỏi đầu tiên cho người chơi trong phòng.");
                        break;
                    }

                    case "SEND_ANSWER": { //thang kia cung se nhan
                        String userAnswer = (String) receiveMessage.getObject();

                        if (roomController != null) {
                            // Lấy câu hỏi hiện tại và đáp án đúng của phòng
                            String correctAnswer = roomController.getCurrentQuestion().getAnswer();
                            // nếu người chơi hết thời gian trả lời client gửi một message với object là null
                            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                                // Cập nhật điểm và chuyển sang câu hỏi mới || Nếu đúng thì bên client sẽ cập nhật bên server không cần cập nhật
    //                            currentRoom.updateScore(this.user);
                                // Kiểm tra xem đã hết câu hỏi chưa
                                if (roomController.hasNextQuestion()) {
                                    roomController.moveToNextQuestion();
                                    System.out.println("tăng câu hỏi");
                                }
                                Message sendMessage = new Message("ANSWER_CORRECT", roomController.getCurrentQuestion());
                                roomController.broadCast(this, userAnswer);
                                roomController.boardCast2(this);
                                this.oos.writeObject(sendMessage);
//                                else {
//                                    // Kết thúc trò chơi khi hết câu hỏi
//                                    Message endMessage = new Message("WIN_GAME", "Trò chơi kết thúc");
//                                    roomController.broadCast(this, userAnswer);
//                                    roomController.boardCast2(this);
//                                    this.oos.writeObject(endMessage);
//                                }
                            } else {
                                // Trả lời sai
                                Message sendMessage = new Message("ANSWER_INCORRECT", "Đáp án không đúng.");
                                roomController.broadCast(this, userAnswer);
                                this.oos.writeObject(sendMessage);
                            }
                        }
                        break;
                    }

//                    case "WIN_GAME": {
//                        Message winMessage = new Message("USER_WIN_GAME", "Thang game");
//                        this.oos.writeObject(winMessage);
//                        break;
//                    }
//
//                    case "OTHER_WIN_GAME": {
//                        Message winMessage = new Message("OTHER_WIN_GAME", "Thang game");
//                        this.oos.writeObject(winMessage);
//                        break;
//                    }

                    case "UPDATE_USER_REQUEST": {
                        String resultGame = (String) receiveMessage.getObject();
                        if (resultGame.equals("win")) {
                            userDao.updateUserGameResult(user, true);
                        } else {
                            userDao.updateUserGameResult(user, false);
                        }
                        break;
                    }

//                    case "GAME_OVER": {
//
//                        break;
//                    }

                    // type: GET_ALL_USER | object: null
                    case "GET_ALL_USER_REQUEST": {
                        List<Users> lst = userDao.getAllUser(this.user);
                        if(lst != null)
                            this.write( "GET_ALL_USER_SUCCESS", lst);
                        else
                            this.write( "GET_ALL_USER_FAIL", false);

                        break;
                    }

                    // type: VIEW_RANK_REQUEST | object: null
                    case "VIEW_RANK_REQUEST": {
                        List<Users> lst = userDao.getListRank();
                        if(lst != null)
                            this.write( "VIEW_RANK_SUCCESS", lst);
                        else
                            this.write( "VIEW_RANK_FAIL", false);

                        break;
                    }

                    // type: WORLD_CHAT_REQUEST | object: String
                    case "WORLD_CHAT_REQUEST": {
                        String message = (String) receiveMessage.getObject();
                        Server.threadBus.broadcast(this, message);
                        System.out.println(message);
                        break;
                    }

                    case "GET_HISTORY_REQUEST": {
                        List<Histories> lst = historyDAO.getAllHistory(this.user);
                        if(lst != null)
                            this.write( "GET_HISTORY_SUCCESS", lst);
                        else
                            this.write( "GET_HISTORY_FAIL", false);

                        break;
                    }
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            // đóng socket
            this.isClosed = true;
            Server.threadBus.getListSocketHandle().remove(this);
            System.out.println("1 luồng đã bị xóa");

            // cập nhật trạng thái user thành offline
            if(this.user != null) {
                userDao.updateOnlineUser(this.user, false);
                userDao.updatePlayingUser(this.user, false);
            }

            // xóa socketHandle khỏi room
            if(this.roomController != null) {
                roomController.removeSocketHandle(this);
                System.out.println(this.roomController.getQuantity());
            }

        }
    }

    // Phương thức tìm phòng chứa người dùng
    private RoomController findRoomByUser(SocketHandle userSocket) {
        for (RoomController room : Server.lstRoomController) {
            if (room.containsUser(userSocket)) {
                return room;
            }
        }
        return null;
    }
}
