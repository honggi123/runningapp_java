package com.example.myapplication.Challenge;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainAct;
import com.example.myapplication.Profile.Fragment4;
import com.example.myapplication.Profile.User;
import com.example.myapplication.Profile.reqfrdlist_Adapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaderBoard_Activity extends AppCompatActivity {

    LeaderBoard_Adapter leaderBoard_adapter;
    ArrayList<User> arr_user;
    RecyclerView recyclerView;
    ChallengeInfo challengeInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        recyclerView = findViewById(R.id.rc_leaderboard);
        challengeInfo = (ChallengeInfo) getIntent().getSerializableExtra("chinfo");
        request(challengeInfo.cno);
    }


    public void request(int cno){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getjoinpeople.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
                Log.e("json",String.valueOf(jsonObject));
            boolean success = jsonObject.getBoolean("success");
            if(success) {
            //  성공
                int size = jsonObject.getInt("num");
                arr_user = new ArrayList<User>();
                JSONArray temp_arr = jsonObject.getJSONArray("joinid");
                for (int i = 0; i < size; i++){
                    User user = new User();
                    user.setId(temp_arr.get(i).toString());
                    Log.e("user",temp_arr.get(i).toString());
                    arr_user.add(user);
                }

                // 라사이클러뷰에 넣기
                leaderBoard_adapter = new LeaderBoard_Adapter(arr_user);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(LeaderBoard_Activity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(leaderBoard_adapter);
            } else {
            //  실패
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(LeaderBoard_Activity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가

            smpr.addStringParam("cno", String.valueOf(cno));

            // 서버에 데이터 보내고 응답 요청
//            RequestQueue requestQueue = Volley.newRequestQueue(LeaderBoard_Activity.this);
//            requestQueue.add(smpr);
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(LeaderBoard_Activity.this);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }

    }


}
