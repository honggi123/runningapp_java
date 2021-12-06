package com.example.myapplication.Chat.CoachUser;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.Loign.LoginActivity;
import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.example.myapplication.Chat.MySocketService;
import com.example.myapplication.Socketsingleton;
import com.google.gson.Gson;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class CChatroomActivity extends AppCompatActivity {

    Button btn_logout;
    SharedPreferences loginshared;
    String mid;
    String logintype;
    String read;
    Socketsingleton socketsingleton;
    Handler mHandler;
    private String ip = "3.12.49.32";
    private int port = 3001;
    OutputStream socketoutputStream;
    PrintWriter sendWriter;
    BufferedReader input;

    RecyclerView chatroomrecyclerView;
    ArrayList<Chatroom> arrchatroom;
    CChatroomAdapter cradapter;
    Socket socket;
    Thread thread;
    int state = 1;
    Boolean isService;
    MySocketService socketService;
    private String[] splited;

    ImageView btn_menumanage;
    ImageView btn_menuchat;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coachmain);

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);
        Intent serviceIntent = new Intent(CChatroomActivity.this, CoachAskNoti.class);
        mHandler = new Handler();

        btn_menumanage = findViewById(R.id.btn_managemem);
        btn_menuchat = findViewById(R.id.btn_myask);
        setmenu();


       //socketsingleton = socketsingleton.getInstance(getApplicationContext());


        IntentFilter intentFilter = new IntentFilter("action");
        registerReceiver(mBroadTestReceiver, intentFilter);


        //  리사이클러뷰 xml id
        chatroomrecyclerView = findViewById(R.id.rc_chatroomc);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arrchatroom = new ArrayList<>();
        cradapter = new CChatroomAdapter(arrchatroom);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(CChatroomActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        chatroomrecyclerView.setLayoutManager(linearLayoutManager);
        // 어댑터 추가
        chatroomrecyclerView.setAdapter(cradapter);
    }

    public void logout(){
        Log.e("logout","1");
        SharedPreferences.Editor edit = loginshared.edit();
        Log.e("logout","2");
        logintype = loginshared.getString("logintype", null);
        Log.e("logout","3");
        edit.clear();
        Log.e("logout","4");
        edit.commit();
        Log.e("logout","5");
        // 카카오로 로긴한 유저 로그아웃
        Log.e("logout","6");
        if(logintype.equals("kakao")){
            Log.e("logout","7");
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                }
            });
        }
        Log.e("logout","8");
        Intent intent = new Intent(CChatroomActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public BroadcastReceiver mBroadTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("action")) {
                // 브로드캐스트 수신받았을 때 실행할 내용
                String read = intent.getStringExtra("read");
                splited = read.split("@");
                if(splited[0].equals("newask")){
                    Log.e("read","newask");
                    request(mid);
                    mHandler.post(new showdialog(splited[1]));
                }else if (splited[0].equals("receivemsg")){
                    request(mid);
                }
            }
        }
    };


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        request(mid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //라이브러리  추가
    public void request(String mid){
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
                arrchatroom.clear();
               JSONArray jsonArray =  jsonObject.getJSONArray("cr");
                for (int i = 0; i < size; i++){
                    Chatroom chatroom = gson.fromJson(jsonArray.get(i).toString(), Chatroom.class);

                    arrchatroom.add(chatroom);
                    Log.e("addarrchatroom",chatroom.getReg_date());
                }

               cradapter.notifyDataSetChanged();
            } else {
            }

            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(CChatroomActivity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });
            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("uid", mid);
            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
    }

    public class showdialog implements Runnable{
        String mid;
        showdialog(String mid){
            this.mid = mid;
        }
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(CChatroomActivity.this);
            builder.setTitle(mid+"님 으로부터 새로운 코칭 신청서가 도착했습니다.")        // 제목 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton){
                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
        }
    }

    public void setmenu(){
        btn_menumanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CChatroomActivity.this,CManageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_menuchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CChatroomActivity.this,CChatroomActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK){ // 값이 성공적으로 반환되었을때
                request(mid);
                return;
            }
        }
    }


}
