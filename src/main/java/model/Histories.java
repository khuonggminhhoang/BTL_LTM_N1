package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Histories implements Serializable {
    private int id;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String isWin;
    private Users owner;
    private Users opponent;

    public Histories() {}

    public Histories(int id, LocalDateTime timeStart, LocalDateTime timeEnd, String isWin, Users owner, Users opponent) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isWin = isWin;
        this.owner = owner;
        this.opponent = opponent;
    }

    public Histories(LocalDateTime timeStart, LocalDateTime timeEnd, String isWin, Users owner, Users opponent) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isWin = isWin;
        this.opponent = opponent;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String isWin() {
        return isWin;
    }

    public Users getOpponent() {
        return opponent;
    }

    public Users getOwner() {
        return owner;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setOpponent(Users opponent) {
        this.opponent = opponent;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setWin(String win) {
        isWin = win;
    }

    @Override
    public String toString() {
        return this.id + " " + this.owner + " " + this.opponent + " " + this.isWin;
    }
}
