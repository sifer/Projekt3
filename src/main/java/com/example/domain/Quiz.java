package com.example.domain;

/**
 * Created by Administrator on 2017-03-22.
 */
public class Quiz {
    private int quizID;
    private String name;
    private int user_ID;

    public Quiz(int quizID, String name, int user_ID) {
        this.quizID = quizID;
        this.name = name;
        this.user_ID = user_ID;
    }

    public int getQuizID() {
        return quizID;
    }

    public String getName() {
        return name;
    }

    public int getUser_ID() {
        return user_ID;
    }
}
