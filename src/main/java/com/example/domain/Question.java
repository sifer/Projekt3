package com.example.domain;

/**
 * Created by Administrator on 2017-03-22.
 */
public class Question {
    private int questionID;
    private String text;
    private String img_URL;
    private int quiz_ID;

    public Question(int questionID, String text, String img_URL, int quiz_ID) {
        this.questionID = questionID;
        this.text = text;
        this.img_URL = img_URL;
        this.quiz_ID = quiz_ID;
    }

    public Question(int questionID, String text, int quiz_ID) {
        this.questionID = questionID;
        this.text = text;
        this.quiz_ID = quiz_ID;
    }
    public int getQuestionID() {
        return questionID;
    }

    public String getText() {
        return text;
    }

    public String getImg_URL() {
        return img_URL;
    }

    public int getQuiz_ID() {
        return quiz_ID;
    }
}
