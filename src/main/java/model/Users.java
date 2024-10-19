package model;

public class Users {
    private int id;
    private String username;
    private String password;
    private int numberOfGame;
    private int numberOfWin;
    private int numberOfDraw;
    private boolean isOnline;
    private boolean isPlaying;
    private String avatar;

    public Users() {}

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users(int id ,String username, String password, int numberOfGame, int numberOfWin, int numberOfDraw, boolean isOnline, boolean isPlaying, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.numberOfWin = numberOfWin;
        this.numberOfDraw = numberOfDraw;
        this.numberOfGame = numberOfGame;
        this.isOnline = isOnline;
        this.isPlaying = isPlaying;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getNumberOfDraw() {
        return numberOfDraw;
    }

    public int getNumberOfGame() {
        return numberOfGame;
    }

    public int getNumberOfWin() {
        return numberOfWin;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNumberOfDraw(int numberOfDraw) {
        this.numberOfDraw = numberOfDraw;
    }

    public void setNumberOfGame(int numberOfGame) {
        this.numberOfGame = numberOfGame;
    }

    public void setNumberOfWin(int numberOfWin) {
        this.numberOfWin = numberOfWin;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }


    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public String toString() {
        return this.username + " " + this.password + " " + this.isOnline + " " + this.isPlaying;
    }

}
