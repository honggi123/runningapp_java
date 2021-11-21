package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestInterface {

    public static Context context;
    RequestQueue requestQueue = null;

    // 정적 필드
    public static RequestInterface singleton;

    // 생성자
    public RequestInterface(Context context){
        this.context = context;
        Log.e("RequestInterface","oncreate");
    }


    public static synchronized RequestInterface getInstance(Context context) {
        if (singleton == null) {
            singleton = new RequestInterface(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }




}
