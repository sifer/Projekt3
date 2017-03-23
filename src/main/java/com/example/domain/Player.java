package com.example.domain;

/**
 * Created by Administrator on 2017-03-23.
 */
public class Player {
    private String Alias;
    private int score;

    public Player(String alias, int score) {
        Alias = alias;
        this.score = score;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        this.Alias = alias;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
