package com.example.myapplication.Chat;

import java.io.Serializable;

public class Chatroom implements Serializable {

    int no;
    String coachuserid;
    String userid;
    String question;
    String lastmsg;
    String reg_date;
    String coachname;

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserid() {
        return userid;
    }

    public String getlastMsg() {
        return lastmsg;
    }

    public int getNo() {
        return no;
    }

    public String getCoachid() {
        return coachuserid;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setlastMsg(String msg) {
        this.lastmsg = msg;
    }

    public void setCoachid(String coachid) {
        this.coachuserid = coachid;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
