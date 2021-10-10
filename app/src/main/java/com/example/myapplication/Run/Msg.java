package com.example.myapplication.Run;

public class Msg {

    String msg;
    Boolean choice;

    Msg()
    {
        choice = false;
    }
    public void setChoice(Boolean choice) {
        this.choice = choice;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean getChoice() {
        return choice;
    }
}
