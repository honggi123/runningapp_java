package com.example.myapplication.Run;

import com.google.android.gms.maps.model.LatLng;

public class friendmsg {

    String id;
    String content;
    LatLng loaction;

    public LatLng getLoaction() {
        return loaction;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLoaction(LatLng loaction) {
        this.loaction = loaction;
    }
}
