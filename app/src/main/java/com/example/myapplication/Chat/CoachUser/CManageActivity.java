package com.example.myapplication.Chat.CoachUser;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CManageActivity extends AppCompatActivity {


    ImageView btn_menumanage;
    ImageView btn_menuchat;

    RecyclerView my_recyclerView;
    ArrayList<Categoryitem> arr_cateitem;
    CategoryAdapter categoryAdapter;
    SharedPreferences loginshared;
    String mid;
    JSONArray catjsonArray;

    RecyclerView userrc;
    ArrayList<String> arruser;
    CateuserAdapter cateuserAdapter;
    SharedPreferences modshared;
    String clicknum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coachmanage);
        btn_menumanage = findViewById(R.id.btn_managemem);
        btn_menuchat = findViewById(R.id.btn_myask);
        setmenu();

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        //  리사이클러뷰 xml id
        userrc = findViewById(R.id.rc_user);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arruser = new ArrayList<>();
        cateuserAdapter = new CateuserAdapter(arruser);

        LinearLayoutManager linearLayoutManager2 =  new LinearLayoutManager(CManageActivity.this);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        userrc.setLayoutManager(linearLayoutManager2);
        cateuserAdapter.setCid(mid);
        // 어댑터 추가
        userrc.setAdapter(cateuserAdapter);


        //  리사이클러뷰 xml id
        my_recyclerView = findViewById(R.id.rc_category);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arr_cateitem = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(arr_cateitem);

        categoryAdapter.setAdapter2(cateuserAdapter);
        categoryAdapter.setId(mid);
                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(CManageActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                my_recyclerView.setLayoutManager(linearLayoutManager);
                // 어댑터 추가
                my_recyclerView.setAdapter(categoryAdapter);
        getcategoryrequest(mid);
    }


    public void getcategoryrequest(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/getcoachuserinfo.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
                Log.e("response",response);
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            if(success) {
                JSONArray jsonArray=new JSONArray(jsonObject.getString("category"));
                arr_cateitem.clear();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Categoryitem categoryitem = new Categoryitem();
                    categoryitem.setName(jsonObject1.getString("name"));
                    arr_cateitem.add(categoryitem);
                }

                Categoryitem categoryitem = new Categoryitem();
                categoryitem.setType("Add");
                arr_cateitem.add(categoryitem);

                catjsonArray = new JSONArray(jsonObject.getString("cat"));
                categoryAdapter.setUsercat(catjsonArray.toString());
                categoryAdapter.notifyDataSetChanged();

                if(clicknum != null){
                    arr_cateitem.get(Integer.parseInt(clicknum)).setClick("true");
                    categoryAdapter.notifyDataSetChanged();
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result","0");
            if(requestCode == 200){

                for (int i = 0; i < arr_cateitem.size(); i++){
                    if(arr_cateitem.get(i).getClick().equals("true")){
                        clicknum = String.valueOf(i);
                        Log.e("click",clicknum+"");
                    }
                }
                getcategoryrequest(mid);

             }

    }
}
