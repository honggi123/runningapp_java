package com.example.myapplication.Profile;
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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Myshoes extends AppCompatActivity {

    Button btn_addshoes;

    RecyclerView my_recyclerView;
    ArrayList<Shoe> arr_shoe;
    shoes_Adapter my_adapter;
    SharedPreferences loginshared;
    String mid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.viewmyshoes);

        Log.e("oncreate","myshoes");

        btn_addshoes = findViewById(R.id.btn_addshoes);

        btn_addshoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Myshoes.this,addshoes.class);
                startActivityForResult(intent,105);
            }
        });


        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        request(mid);

        //  리사이클러뷰 xml id
                my_recyclerView = findViewById(R.id.rc_shoes);
                // 라사이클러뷰에 넣기
                // 어댑터 객체 생성
                arr_shoe = new ArrayList<>();
                my_adapter = new shoes_Adapter(arr_shoe,Myshoes.this);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(Myshoes.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                my_recyclerView.setLayoutManager(linearLayoutManager);
                // 어댑터 추가
                my_recyclerView.setAdapter(my_adapter);


        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 105){
            if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                return;
            }
            // 코드 작성
            request(mid);
        }
    }

    public void request(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/showmyshoes.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            Log.e("json",jsonObject.toString());
            int num = jsonObject.getInt("num");
            if(num > 0) {
                //  성공
                arr_shoe = new ArrayList<Shoe>();
                JSONArray data = jsonObject.getJSONArray("data");
                    for(int i= 0; i < num; i++){
                        Shoe shoe = new Shoe();
                        JSONObject dataJSONObject = data.getJSONObject(i);
                        shoe.setShoe_id(dataJSONObject.getString("shoe_id"));
                        shoe.setName(dataJSONObject.getString("shoe_name"));
                        shoe.setDistance(Integer.parseInt(dataJSONObject.getString("distance")));
                        shoe.setG_distance(Integer.parseInt(dataJSONObject.getString("g_distance")));
                        shoe.setWear(dataJSONObject.getString("wear"));
                        JSONArray jsonArray = new JSONArray(dataJSONObject.getString("img"));
                        shoe.setImageurl(jsonArray.getString(0));
                        shoe.setReg_date(dataJSONObject.getString("reg_date"));

                        arr_shoe.add(shoe);
                    }

                    my_adapter = new shoes_Adapter(arr_shoe,Myshoes.this);

                    LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(Myshoes.this);
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    my_recyclerView.setLayoutManager(linearLayoutManager);
                    // 어댑터 추가
                    my_recyclerView.setAdapter(my_adapter);

            } else {
                //  실패
                Toast.makeText(Myshoes.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
                } catch (Exception e) {
                e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(Myshoes.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("userID", mid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(Myshoes.this);
            requestQueue.add(smpr);


    }

}
