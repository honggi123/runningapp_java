package com.example.myapplication;

public class Coaching {

    String name;
    int endtime;
    String choachingjson;
    String reg_date;
    String description;

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

    public int getEndtime() {
        return endtime;
    }

    public String getChoachingjson() {
        return choachingjson;
    }

    public void setChoachingjson(String choachingjson) {
        this.choachingjson = choachingjson;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }


}
