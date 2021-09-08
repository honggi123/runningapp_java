package com.example.myapplication.Request;



import android.util.Log;



import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://3.143.9.214/Join.php";
    private Map<String,String>map;

    public RegisterRequest(String userID, String userPassword,String email, String gender, int method, Response.Listener<String> listener) {
        super(method, URL,listener, null);
            map = new HashMap<>();
            map.put("userID",userID);
            map.put("userPassword",userPassword);
            map.put("email",email);
            map.put("gender",gender);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
