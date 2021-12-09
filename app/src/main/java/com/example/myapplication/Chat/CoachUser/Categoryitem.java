package com.example.myapplication.Chat.CoachUser;

public class Categoryitem {
    String name;
    String click = "false";
    String type = "normal";

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public void setName(String name) {
        this.name = name;
    }
}
