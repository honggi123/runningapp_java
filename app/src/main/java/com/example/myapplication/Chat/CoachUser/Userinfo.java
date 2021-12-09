package com.example.myapplication.Chat.CoachUser;

public class Userinfo {
    String goal;
    String sdate;
    int weeknum;
    String group;
    String uniq;

    public int getWeeknum() {
        return weeknum;
    }

    public String getGoal() {
        return goal;
    }

    public String getGroup() {
        return group;
    }

    public String getSdate() {
        return sdate;
    }

    public String getUniq() {
        return uniq;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public void setUniq(String uniq) {
        this.uniq = uniq;
    }

    public void setWeeknum(int weeknum) {
        this.weeknum = weeknum;
    }
}
