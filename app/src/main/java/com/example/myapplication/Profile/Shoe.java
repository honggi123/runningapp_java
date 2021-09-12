package com.example.myapplication.Profile;

public class Shoe {

    String name;
    int distance;
    String imageurl;
    int g_distance;
    String reg_date;
    String wear;
    String shoe_id;

    public int getG_distance() {
        return g_distance;
    }


    public String getShoe_id() {
        return shoe_id;
    }

    public void setShoe_id(String shoe_id) {
        this.shoe_id = shoe_id;
    }

    public void setG_distance(int g_distance) {
        this.g_distance = g_distance;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getWear() {
        return wear;
    }

    public void setWear(String wear) {
        this.wear = wear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
