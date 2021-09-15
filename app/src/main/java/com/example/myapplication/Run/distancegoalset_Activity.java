package com.example.myapplication.Run;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Challenge.viewchallenge_Activity;
import com.example.myapplication.MainAct;
import com.example.myapplication.R;

import org.json.JSONObject;

public class distancegoalset_Activity extends AppCompatActivity {

    EditText edit_rungoal;
    Button btn_rungoalset;
    SharedPreferences loginshared;
    String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tdistancegoal_set);
        edit_rungoal = findViewById(R.id.edit_rungoal);
        btn_rungoalset = findViewById(R.id.btn_rungoalset);
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        id = loginshared.getString("id", null);

        btn_rungoalset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                float fgoal = Float.parseFloat(edit_rungoal.getText().toString());
                int igoal = (int)(fgoal *1000);
                request(id,igoal);
                finish();
            }
        });


    }


    public void request(String mid,int distance){
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/goalset.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                // 업로드 성공
                                Toast.makeText(distancegoalset_Activity.this, "목표 설정이 되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // 업로드 실패
                                Toast.makeText(distancegoalset_Activity.this, "목표 설정을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(distancegoalset_Activity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("userID", mid);
                smpr.addStringParam("dgoal",String.valueOf(distance));

        // 서버에 데이터 보내고 응답 요청
//        RequestQueue requestQueue = Volley.newRequestQueue(distancegoalset_Activity.this);
//        requestQueue.add(smpr);
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(distancegoalset_Activity.this);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }

    }
}
