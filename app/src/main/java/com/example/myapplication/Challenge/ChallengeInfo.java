package com.example.myapplication.Challenge;

import java.io.Serializable;

public class ChallengeInfo implements Serializable {
    String id;
    int cno;
    String name;
    int g_distance;
    int num_member;
    int n_distance;

    String s_date;
    String g_date;
    String reg_date;

    public ChallengeInfo() {
    }

    public int getN_distance() {
        return n_distance;
    }

    public void setN_distance(int n_distance) {
        this.n_distance = n_distance;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public int getCno() {
        return cno;
    }

    public void setCno(int cno) {
        this.cno = cno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getG_distance() {
        return g_distance;
    }

    public void setG_distance(int g_distance) {
        this.g_distance = g_distance;
    }

    public int getNum_member() {
        return num_member;
    }

    public void setNum_member(int num_member) {
        this.num_member = num_member;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getG_date() {
        return g_date;
    }

    public void setG_date(String g_date) {
        this.g_date = g_date;
    }
}
