package com.example;

public class HelloMessage {

    private String name;
    private String message;

    public HelloMessage() {
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public HelloMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
