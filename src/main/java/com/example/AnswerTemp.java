package com.example;

/**
 * Created by Administrator on 2017-03-21.
 */
public class AnswerTemp {
    private int optionSelected;
    private String playerAlias;


    public AnswerTemp(){

    }
    public AnswerTemp(String optionSelected, String playerAlias) {
        this.optionSelected = Integer.parseInt(optionSelected);
        this.playerAlias = playerAlias;

    }

    public int getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(String optionSelected) {
        this.optionSelected = Integer.parseInt(optionSelected);
    }

    public String getPlayerAlias() {
        return playerAlias;
    }

    public void setPlayerAlias(String playerAlias) {
        this.playerAlias = playerAlias;
    }
    public String toString(){
        return getPlayerAlias()+" has selected answer "+getOptionSelected();
    }
}
