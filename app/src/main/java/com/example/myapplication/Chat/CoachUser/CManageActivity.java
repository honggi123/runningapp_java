package com.example.myapplication.Chat.CoachUser;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MySingleton;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class CManageActivity extends AppCompatActivity {


    ImageView btn_menumanage;
    ImageView btn_menuchat;

    RecyclerView my_recyclerView;
    ArrayList<Categoryitem> arr_cateitem;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coachmanage);
        btn_menumanage = findViewById(R.id.btn_managemem);
        btn_menuchat = findViewById(R.id.btn_myask);
        setmenu();


        //  리사이클러뷰 xml id
                my_recyclerView = findViewById(R.id.rc_category);
                // 라사이클러뷰에 넣기
                // 어댑터 객체 생성
             arr_cateitem = new ArrayList<>();
             categoryAdapter = new CategoryAdapter(arr_cateitem);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(CManageActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                my_recyclerView.setLayoutManager(linearLayoutManager);
                // 어댑터 추가
                my_recyclerView.setAdapter(categoryAdapter);

    }


    public void request(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getcoachuserinfo.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            if(success) {

            } else {
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("coachID", mid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
    }

    public void setmenu(){
        btn_menumanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CManageActivity.this,CManageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_menuchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CManageActivity.this,CChatroomActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



}
