package com.example.myapplication.Request;



import android.util.Log;



import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://3.143.9.214/Login.php";
    private Map<String,String>map;

    public LoginRequest(String userID, String userPassword,int method, Response.Listener<String> listener) {
        super(method, URL,listener, null);
        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }


}
