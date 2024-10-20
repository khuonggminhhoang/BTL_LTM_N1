package model;

import java.io.Serializable;

public class Questions implements Serializable {
    private int id;
    private String answer;
    private String imgPath;
    private String hint;

    public Questions() {}

    public Questions(int id, String answer, String imgPath, String hint) {
        this.id = id;
        this.answer = answer;
        this.imgPath = imgPath;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getHint() {
        return hint;
    }

    public String getImgPath() {
        return imgPath;
    }

    @Override
    public String toString() {
        return this.answer + " " + this.hint;
    }
}
