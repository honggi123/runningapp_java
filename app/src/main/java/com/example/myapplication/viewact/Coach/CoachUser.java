package com.example.myapplication.viewact.Coach;

public class CoachUser {

    String id;
    String name;
    String career;
    String desc;
    Boolean sel =false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSel() {
        return sel;
    }

    public void setSel(Boolean sel) {
        this.sel = sel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getCareer() {
        return career;
    }

    public String getDesc() {
        return desc;
    }
}
