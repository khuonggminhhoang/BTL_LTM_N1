package model;

import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private Object object;

    public Message() {}

    public Message(String type, Object object) {
        this.type = type;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String getType() {
        return type;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setType(String type) {
        this.type = type;
    }
}
