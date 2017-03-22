package com.example.domain;

/**
 * Created by Administrator on 2017-03-22.
 */
public class Answer {
    private int answerID;
    private String text;
    private boolean isCorrect;
    private int question_ID;

    public Answer(int answerID, String text, int isCorrect, int question_ID) {
        this.answerID = answerID;
        this.text = text;
        if(isCorrect == 1){
            this.isCorrect = true;
        }
        else {
            this.isCorrect = false;
        }
        this.question_ID = question_ID;
    }

    public int getAnswerID() {
        return answerID;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public int getQuestion_ID() {
        return question_ID;
    }
    public String toString(){
        return getText();
    }
}
