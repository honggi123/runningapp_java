package com.example.myapplication.viewact.Coach;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Coaching implements Serializable {

    String name;

    String choachingjson;
    String reg_date;
    String description;
    String question;
    int qcount;

    public int getQcount() {
        return qcount;
    }

    public void setQcount(int qcount) {
        this.qcount = qcount;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getChoachingjson() {
        return choachingjson;
    }

    public void setChoachingjson(String choachingjson) {
        this.choachingjson = choachingjson;
    }




}
