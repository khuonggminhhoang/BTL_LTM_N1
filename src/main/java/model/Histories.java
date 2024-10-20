package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Histories implements Serializable {
    private int id;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private boolean isWin;
    private int opponentId;
    private int ownerId;

    public Histories() {}

    public Histories(int id, LocalDateTime timeStart, LocalDateTime timeEnd, boolean isWin, int opponentId, int ownerId) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.isWin = isWin;
        this.opponentId = opponentId;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public boolean isWin() {
        return isWin;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    @Override
    public String toString() {
        return this.id + " " + this.ownerId + " " + this.opponentId + " " + this.isWin;
    }
}
