package com.example.myapplication.viewact.Coach;

public class Coachquestion {

    String qustion;
    Boolean select = false;
    String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getSelect() {
        return select;
    }

    public String getQustion() {
        return qustion;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public void setQustion(String qustion) {
        this.qustion = qustion;
    }
}
