package com.example.myapplication.Profile;


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
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import org.json.JSONObject;

public class myinfo extends AppCompatActivity {

    NumberPicker numberPicker;
    NumberPicker numberPicker2;
    NumberPicker numberPicker3;
    SharedPreferences loginshared;;
    Button btn_chginfo;
    String gender;
    int weight;
    int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        numberPicker = findViewById(R.id.numberpicker);
        numberPicker2 = findViewById(R.id.numberpicker2);
        numberPicker3 = findViewById(R.id.numberpicker3);

        btn_chginfo = findViewById(R.id.btn_chginfo);

        String mid;

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        request(mid);

        // Number Picker Setting
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(2);
        numberPicker.setDisplayedValues(new String[]{"남자", "여자"});

        // Number Picker Setting
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(100);
        String[] arr_height = new String[200];

        for(int i = 0;i < arr_height.length; i++ ){
            arr_height[i] = (i+120) +" cm";
        }
        numberPicker2.setDisplayedValues(arr_height);


        // Number Picker Setting
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(100);
        String[] arr_weight = new String[200];

        for(int i = 0;i < arr_weight.length; i++ ){
            arr_weight[i] = (i+20) +" kg";
        }
        numberPicker3.setDisplayedValues(arr_weight);


        btn_chginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender;
                int weight;
                int height;
                if(numberPicker.getValue() == 1){
                    gender = "m";
                }else{
                    gender = "f";
                }

                height= numberPicker2.getValue() + 120;
                weight= numberPicker3.getValue() +20;
                Log.e("height",height+"");

                setinforequest(mid,gender,height,weight);
            }
        });

    }


    public void request(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getuserinfo.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            Log.e("json",jsonObject+"");
            boolean success = jsonObject.getBoolean("success");
            if(success) {
                JSONObject json =jsonObject.getJSONObject("data");
                Log.e("json",json+"");

                gender = json.getString("gender");
                weight = json.getInt("weight");
                height = json.getInt("height");
                Log.e("gender",gender+"");
                if(gender.equals("f")){
                    numberPicker.setValue(2);
                }

                numberPicker2.setValue(height - 120);
                numberPicker3.setValue(weight - 20);

            } else {

            }

            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(myinfo.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("userID", mid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(myinfo.this);
           requestQueue.add(smpr);

            }




            public void setinforequest(String mid,String gender,int height, int weight){
                    // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                    String serverUrl="http://3.143.9.214/setmyinfo.php";

                    // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                    SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                    // 업로드 성공
                    Toast.makeText(myinfo.this, "정보가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    } else {
                    // 업로드 실패
                    Toast.makeText(myinfo.this, "정보 업데이트가 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(myinfo.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    });

                    // 요청 객체에 보낼 데이터를 추가
                    smpr.addStringParam("userID", mid);
                    smpr.addStringParam("gender", gender);
                    smpr.addStringParam("height", String.valueOf(height));
                    smpr.addStringParam("weight", String.valueOf(weight));

                    // 서버에 데이터 보내고 응답 요청
                    RequestQueue requestQueue = Volley.newRequestQueue(myinfo.this);
                    requestQueue.add(smpr);


                    }

}
