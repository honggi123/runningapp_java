package com.example.myapplication.viewact;

import java.io.Serializable;

public class RunInfo implements Serializable {

    float rating;
    int distance;
    int time;
    String reg_date;
    String memo;
    String imgList;
    int kacl;

    public int getKacl() {
        return kacl;
    }

    public void setKacl(int kacl) {
        this.kacl = kacl;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public int getDistance() {
        return distance;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


}
