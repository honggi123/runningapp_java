package com.example.myapplication.Request;



import android.util.Log;


import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;


import java.util.HashMap;
import java.util.Map;

public class IdchkRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://3.12.49.32/IDchk.php";
    private Map<String,String>map;

    public IdchkRequest(String userID, String snsid, int method, Response.Listener<String> listener) {
        super(method, URL,listener, null);
        map = new HashMap<>();
        if(userID != null){
            map.put("userID",userID);
        }else {
            map.put("snsid", snsid);
        }
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
