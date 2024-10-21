package model;

public class Room {
    private String id;
    private Users[] players = new Users[2];
    private String password;
    private int playerCount = 0;
    private boolean isFull;

    public Room(String id,  int playerCount, boolean isFull) {
        this.id = id;
        this.playerCount = playerCount;
        this.isFull = isFull;
    }

    public boolean isFull() {
        return playerCount == 2;
    }

    public Room(String id) {
        this.id = id;
    }

    public boolean addPlayer(Users player) {
        if (isFull()) {
            return false; // room is full
        }
        players[playerCount] = player;
        playerCount++;
        return true;
    }

    public Users[] getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        this.isFull = (playerCount == 2);
    }

    public Room(String password, String id) {
        this.password = password;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
