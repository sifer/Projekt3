package com.example.domain;

/**
 * Created by Administrator on 2017-03-22.
 */
public class User {
    private int userID;
    private String userName;
    private String alias;

    public User(int userID, String userName, String alias) {
        this.userID = userID;
        this.userName = userName;
        this.alias = alias;
    }
    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getAlias() {
        return alias;
    }
}
