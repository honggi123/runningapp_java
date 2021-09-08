package com.example.myapplication.Request;


import android.util.Log;


import com.android.volley.Response;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;


import java.util.HashMap;
import java.util.Map;


public class SnsRegisterRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://3.143.9.214/SnsJoin.php";
    private Map<String,String> map;

    public SnsRegisterRequest(String userID, String userPassword,String snsID,String snstype, int method, Response.Listener<String> listener) {
        super(method, URL,listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", error.toString());
            }
        });

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("snsID",snsID);
        map.put("snsType",snstype);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }


}
