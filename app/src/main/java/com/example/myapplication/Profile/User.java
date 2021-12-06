package com.example.myapplication.Profile;

public class User {

    String id;
    int alldistance;
    int t_rundistance;
    int t_time;
    Boolean select =false;
    Boolean runonline = false;
    Boolean coachuser;


    public void setCoachuser(Boolean coachuser) {
        this.coachuser = coachuser;
    }

    public Boolean getCoachuser() {
        return coachuser;
    }

    public Boolean getRunonline() {
        return runonline;
    }

    public void setRunonline(Boolean runonline) {
        this.runonline = runonline;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAlldistance() {
        return alldistance;
    }

    public void setAlldistance(int alldistance) {
        this.alldistance = alldistance;
    }

    public int getT_rundistance() {
        return t_rundistance;
    }

    public void setT_rundistance(int t_rundistance) {
        this.t_rundistance = t_rundistance;
    }

    public int getT_time() {
        return t_time;
    }

    public void setT_time(int t_time) {
        this.t_time = t_time;
    }
}
