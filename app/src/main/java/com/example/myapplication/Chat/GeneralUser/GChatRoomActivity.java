package com.example.myapplication.Chat.GeneralUser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.myapplication.Challenge.Fragment3;
import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.Chat.CoachUser.CChatroomActivity;
import com.example.myapplication.MySingleton;
import com.example.myapplication.Profile.ProfileMenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.Run.RunMenuActivity;
import com.example.myapplication.viewact.ViewactMenuActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GChatRoomActivity extends AppCompatActivity {

    ImageView menurun;
    ImageView menuviewact;
    ImageView menuch;
    ImageView menumy;
    ImageView menuchat;
    SharedPreferences loginshared;
    String mid;

    RecyclerView crrecyclerView;
    ArrayList<Chatroom> arr_chatroom;
    GChatroomAdapter gChatroomAdapter;
    private String[] splited;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuchat_general);

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        menuchat = findViewById(R.id.btn_menuchat);
        menurun = findViewById(R.id.btn_menurun);
        menuviewact = findViewById(R.id.btn_menuviewact);
        menuch = findViewById(R.id.btn_menuchat);
        menumy = findViewById(R.id.btn_menumy);
        menuset();

        //  리사이클러뷰 xml id
        crrecyclerView = findViewById(R.id.rc_gchatroom);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arr_chatroom = new ArrayList<>();
        gChatroomAdapter = new GChatroomAdapter(arr_chatroom);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(GChatRoomActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        crrecyclerView.setLayoutManager(linearLayoutManager);
        // 어댑터 추가
        crrecyclerView.setAdapter(gChatroomAdapter);

        IntentFilter intentFilter = new IntentFilter("action");
        registerReceiver(mBroadTestReceiver, intentFilter);

    }

    public BroadcastReceiver mBroadTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("action")) {
                // 브로드캐스트 수신받았을 때 실행할 내용
                String read = intent.getStringExtra("read");
                splited = read.split("@");
               if (splited[0].equals("receivemsg")){
                    requestchatroom(mid);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        requestchatroom(mid);
    }

    public void menuset(){
        Log.e("menuset","1");
        menurun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GChatRoomActivity.this, RunMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","2");
        menuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GChatRoomActivity.this, Fragment3.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","3");
        menuviewact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GChatRoomActivity.this, ViewactMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","4");
        menumy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GChatRoomActivity.this, ProfileMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        menuchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GChatRoomActivity.this, GChatRoomActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","5");
    }

    //라이브러리  추가
    public void requestchatroom(String mid){
        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.12.49.32/getchatroom.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",jsonObject.toString());
                    boolean success = jsonObject.getBoolean("success");
                    int size = jsonObject.getInt("num");
                    Gson gson = new Gson();
                    if(success) {
                        arr_chatroom.clear();
                        JSONArray jsonArray =  jsonObject.getJSONArray("cr");
                        for (int i = 0; i < size; i++){
                            Chatroom chatroom = gson.fromJson(jsonArray.get(i).toString(), Chatroom.class);
                            arr_chatroom.add(chatroom);
                            Log.e("addarrchatroom",chatroom.getCoachname());
                        }
                        gChatroomAdapter.notifyDataSetChanged();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GChatRoomActivity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("uid", mid);
        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(smpr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK){ // 값이 성공적으로 반환되었을때
                requestchatroom(mid);
                return;
            }

        }
    }
}
