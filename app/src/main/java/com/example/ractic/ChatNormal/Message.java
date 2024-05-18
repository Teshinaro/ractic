package com.example.ractic.ChatNormal;

public class Message {
    String message;
    String key;
    String Name;

    public Message() {
    }

    public Message(String message, String name) {
        this.message = message;
        Name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
