package model;

public class Room {

    private int id;
    private Users[] players = new Users[2];
    private int playerCount = 0;
    private boolean isFull;

    public Room(int id) {
        this.id = id;
    }

    public Room(int id,  int playerCount, boolean isFull) {
        this.id = id;
        this.playerCount = playerCount;
        this.isFull = isFull;
    }

    public boolean isFull() {
        return playerCount > 1;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
