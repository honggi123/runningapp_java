package com.example.myapplication.Profile;


import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Challenge.Fragment3;
import com.example.myapplication.Loign.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.RequestInterface;
import com.example.myapplication.Run.RunMenuActivity;
import com.example.myapplication.viewact.ViewactMenuActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileMenuActivity extends AppCompatActivity {

    String mid;
    TextView id;

    ImageView btn_plusfrined;
    ArrayList<User> reqfriendInfoarr;
    ArrayList<User> myfrindInfoArrayList;

    reqfrdlist_Adapter adapter;
    RecyclerView rq_recyclerView;
    ImageView btn_myshoes;
    ImageView btn_setting;
    myfriendlist_Adapter myfriend_adapter;
    RecyclerView myfrd_recyclerView;
    SharedPreferences loginshared;
    String logintype;
    ImageView menurun;
    ImageView menuviewact;
    ImageView menuch;
    ImageView menumy;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("ProfileMenuAct","1");
        super.onCreate(savedInstanceState);
        Log.e("ProfileMenuAct","2");
        setContentView(R.layout.fragment4);
        Log.e("ProfileMenuAct","3");
        id = findViewById(R.id.id);
        Log.e("ProfileMenuAct","4");
        btn_plusfrined = findViewById(R.id.btn_plusfrined);
        Log.e("ProfileMenuAct","5");
        btn_myshoes = findViewById(R.id.btn_myshoes);
        Log.e("ProfileMenuAct","6");
        btn_setting = findViewById(R.id.btn_set);
        Log.e("ProfileMenuAct","7");
        // 내친구 리사이클러뷰
        myfrd_recyclerView = findViewById(R.id.rc_myfriend);
        Log.e("ProfileMenuAct","8");
        // 친구 요청 목록 리사이클러뷰
        rq_recyclerView = findViewById(R.id.rc_reqfriend);
        Log.e("ProfileMenuAct","9");

        menurun = findViewById(R.id.btn_menurun);
        Log.e("ProfileMenuAct","10");
        menuviewact = findViewById(R.id.btn_menuviewact);
        Log.e("ProfileMenuAct","11");
        menuch = findViewById(R.id.btn_menuch);
        Log.e("ProfileMenuAct","12");
        menumy = findViewById(R.id.btn_menumy);
        Log.e("ProfileMenuAct","13");
        // 아이디 설정
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        Log.e("ProfileMenuAct","14");
        mid = loginshared.getString("id", null);
        Log.e("ProfileMenuAct","15");
        id.setText(mid);
        Log.e("ProfileMenuAct","16");
        menuset();
        Log.e("ProfileMenuAct","17");
        btn_plusfrined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ProfileMenuAct","btn_plusfrined1");
                Intent intent = new Intent(ProfileMenuActivity.this, frinedsearchActivity.class);
                Log.e("ProfileMenuAct","btn_plusfrined2");
                startActivity(intent);
                Log.e("ProfileMenuAct","btn_plusfrined3");
            }
        });
        Log.e("ProfileMenuAct","18");
        btn_myshoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this, Myshoes.class);
                startActivity(intent);
            }
        });
        Log.e("ProfileMenuAct","19");
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this, viewsetting.class);
                startActivityForResult(intent,200);
            }
        });
        Log.e("ProfileMenuAct","20");

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("ProfileMenuAct","21");
        frdlistrequest(mid);
        Log.e("ProfileMenuAct","22");
       // reqreqfriendinfo(mid);
        Log.e("ProfileMenuAct","23");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == 200){
                    if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                        return;
                    }
                    // 코드 작성
                   Boolean logout = data.getBooleanExtra("logout",false); //  수정 여부 확인
                    if(logout){
                        logout();
                        Intent intent = new Intent(ProfileMenuActivity.this, LoginActivity.class);
                        ProfileMenuActivity.this.finish();
                    }
                }
    }

    public void reqreqfriendinfo(String id){
        Log.e("reqreqfriendinfo","1");
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/callfriendinfo.php";
        Log.e("reqreqfriendinfo","2");
                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e("reqreqfriendinfo","3");
                try {
                JSONObject jsonObject = new JSONObject(response);
                    Log.e("reqreqfriendinfo","4");
                boolean success = jsonObject.getBoolean("success");
                    Log.e("reqreqfriendinfo","5");
                    int frdnum = jsonObject.getInt("num");
                    Log.e("reqreqfriendinfo","6");
                    Log.e("json",String.valueOf(jsonObject));
                    Log.e("reqreqfriendinfo","7");
                    reqfriendInfoarr = new ArrayList<>();
                    Log.e("reqreqfriendinfo","8");
                if(success) {
                    Log.e("reqreqfriendinfo","9");
                    for(int i=0; i< frdnum; i++){
                        Log.e("reqreqfriendinfo","10");
                       JSONObject jsonObject1 = jsonObject.getJSONObject("user"+i);
                        Log.e("reqreqfriendinfo","11");
                       String uid = jsonObject1.getString("id");
                        Log.e("reqreqfriendinfo","12");
                       User friendInfo = new User();
                        Log.e("reqreqfriendinfo","13");
                        friendInfo.setId(uid);
                        Log.e("reqreqfriendinfo","14");
                        reqfriendInfoarr.add(friendInfo);
                    }
                    Log.e("reqreqfriendinfo","15");
                    // 라사이클러뷰에 넣기
                    adapter = new reqfrdlist_Adapter(mid,reqfriendInfoarr, ProfileMenuActivity.this);
                    Log.e("reqreqfriendinfo","16");
                    LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(ProfileMenuActivity.this);
                    Log.e("reqreqfriendinfo","17");
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    Log.e("reqreqfriendinfo","18");
                    rq_recyclerView.setLayoutManager(linearLayoutManager);
                    Log.e("reqreqfriendinfo","19");
                    rq_recyclerView.setAdapter(adapter);
                    Log.e("reqreqfriendinfo","20");
                } else {
                    Log.e("reqreqfriendinfo","21");
                }
                } catch (Exception e) {
                    Log.e("reqreqfriendinfo","22");
                e.printStackTrace();
                }
                }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("reqreqfriendinfo","23");
                }
                });
        Log.e("reqreqfriendinfo","24");
                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("userID", id);
        Log.e("reqreqfriendinfo","25");
                // 서버에 데이터 보내고 응답 요청
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.e("reqreqfriendinfo","26");
                requestQueue.add(smpr);
        Log.e("reqreqfriendinfo","27");
        Log.e("getThreadPoolSize",requestQueue.getThreadPoolSize()+"");
        /*
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(Fragment4.this);
            requestQueue.add(smpr);
        }else{
            requestQueue.add(smpr);
        }

         */

    }




    public void frdlistrequest(String mid){
        Log.e("frdlistrequest","1");
              // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
              String serverUrl="http://3.143.9.214/myfriendinfo.php";
        Log.e("frdlistrequest","2");
              // 파일 전송 요청 객체 생성[결과를 String으로 받음]
              SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {

                  Log.e("frdlistrequest","3");
                  /*
          try {
                  JSONObject jsonObject = new JSONObject(response);
                  Log.e("frdlistrequest","4");

                  Log.e("json",String.valueOf(jsonObject));
                  Log.e("frdlistrequest","5");
                  int fnum = jsonObject.getInt("fnum");
                  Log.e("frdlistrequest","6");
                    if(fnum>0){
                        Log.e("frdlistrequest","7");
                        myfrindInfoArrayList = new ArrayList<>();
                        Log.e("frdlistrequest","8");
                        for(int i=0;i<fnum;i++){
                            Log.e("frdlistrequest","9");
                            User user = new User();
                            Log.e("frdlistrequest","10");
                            user.setId(jsonObject.getString("friend"+i));
                            Log.e("frdlistrequest","11");
                            myfrindInfoArrayList.add(user);
                            Log.e("frdlistrequest","12");
                        }
                        Log.e("frdlistrequest","13");
                        // 라사이클러뷰에 넣기
                        myfriend_adapter = new myfriendlist_Adapter(mid,myfrindInfoArrayList,1);
                        Log.e("frdlistrequest","14");
                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(ProfileMenuActivity.this);
                        Log.e("frdlistrequest","15");
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        Log.e("frdlistrequest","16");
                        myfrd_recyclerView.setLayoutManager(linearLayoutManager);
                        Log.e("frdlistrequest","17");
                        myfrd_recyclerView.setAdapter(myfriend_adapter);
                        Log.e("frdlistrequest","18");
                    }
              } catch (Exception e) {
                  Log.e("frdlistrequest","19");
              e.printStackTrace();
                  }*/

              }
              }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
          //Log.e("frdlistrequest","onErrorResponse");
           }
              });
              //smpr.setTag(ProfileMenuActivity.this);
        Log.e("frdlistrequest","21");
              // 요청 객체에 보낼 데이터를 추가
         //     smpr.addStringParam("f_id", mid);
        Log.e("frdlistrequest","22");

              // 서버에 데이터 보내고 응답 요청
               requestQueue = Volley.newRequestQueue(ProfileMenuActivity.this);
        Log.e("frdlistrequest","23");
              requestQueue.add(smpr);
        Log.e("frdlistrequest","24");
              /*
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(Fragment4.this);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }

               */

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
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("profileMenuAct","onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("profileMenuAct","onstop");
       //requestQueue.cancelAll(ProfileMenuActivity.this);
        requestQueue.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("profileMenuAct","onDestroy");

        Log.e("threadname",Thread.activeCount()+"");
    }

    public void menuset(){
        Log.e("menuset","1");
        menurun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this, RunMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","2");
        menuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this, Fragment3.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","3");
        menuviewact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this, ViewactMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","4");
        menumy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMenuActivity.this, ProfileMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.e("menuset","5");
    }

}
