package com.example.myapplication.Request;

import android.util.Log;



import com.android.volley.Response;

import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class EmailsendRequest extends StringRequest {


    //서버 url 설정(php파일 연동)
    private Map<String,String> map;

    public EmailsendRequest(String email, int method, String URL,Response.Listener<String> listener) {
        super(method, URL,listener, null);
        map = new HashMap<>();
        map.put("email",email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
