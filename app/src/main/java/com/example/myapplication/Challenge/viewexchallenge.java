package com.example.myapplication.Challenge;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class viewexchallenge extends AppCompatActivity {

    ChallengelistAdapter adapter;
    ArrayList<ChallengeInfo> arr_exch;
    RecyclerView recyclerView;
    SharedPreferences loginshared;
    String mid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewexchallenge);

        recyclerView = findViewById(R.id.rc_exchallenge);

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        request(mid);
    }


    public void request(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getexchallenge.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
                Log.e("json",jsonObject.toString());
                int c_num = jsonObject.getInt("num");
                int joinc_num = jsonObject.getInt("joinc_num");
            if(c_num+joinc_num > 0) {
                arr_exch = new ArrayList<ChallengeInfo>();
                JSONArray data = jsonObject.getJSONArray("data");
                for(int i= 0; i < c_num+joinc_num; i++){
                    Log.e("mychdata",String.valueOf(data.get(i)));
                    ChallengeInfo challengeInfo = new ChallengeInfo();
                    JSONObject dataJSONObject = data.getJSONObject(i);
                    challengeInfo.setCno(Integer.parseInt(dataJSONObject.getString("cno")));
                    challengeInfo.setId(dataJSONObject.getString("id"));
                    challengeInfo.setName(dataJSONObject.getString("name"));
                    challengeInfo.setG_distance(Integer.parseInt(dataJSONObject.getString("g_distance")));
                    challengeInfo.setNum_member(Integer.parseInt(dataJSONObject.getString("num_member")));
                    challengeInfo.setS_date(dataJSONObject.getString("s_date"));
                    challengeInfo.setN_distance(Integer.parseInt(dataJSONObject.getString("n_distance")));
                    challengeInfo.setG_date(dataJSONObject.getString("e_date"));
                    challengeInfo.setReg_date(dataJSONObject.getString("reg_date"));
                    arr_exch.add(challengeInfo);
                }

                adapter = new ChallengelistAdapter(arr_exch,1);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(viewexchallenge.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            } else {
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(viewexchallenge.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("userID", mid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(viewexchallenge.this);
            requestQueue.add(smpr);
            }
}
