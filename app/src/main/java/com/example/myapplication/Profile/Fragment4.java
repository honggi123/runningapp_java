package com.example.myapplication.Profile;


import static android.app.Activity.RESULT_OK;

import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainAct;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment4 extends Fragment {

    String mid;
    TextView id;
    MainAct context;
    ImageView btn_plusfrined;
    ArrayList<User> reqfriendInfoarr;
    ArrayList<User> myfrindInfoArrayList;

    reqfrdlist_Adapter adapter;
    RecyclerView rq_recyclerView;
    ImageView btn_myshoes;
    ImageView btn_setting;
    myfriendlist_Adapter myfriend_adapter;
    RecyclerView myfrd_recyclerView;

    public Fragment4(Context context){
        this.context = (MainAct) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment4,container,false);

        id = view.findViewById(R.id.id);
        btn_plusfrined = view.findViewById(R.id.btn_plusfrined);
        btn_myshoes = view.findViewById(R.id.btn_myshoes);
        btn_setting = view.findViewById(R.id.btn_set);
        // 내친구 리사이클러뷰
        myfrd_recyclerView = view.findViewById(R.id.rc_myfriend);

        // 친구 요청 목록 리사이클러뷰
        rq_recyclerView = view.findViewById(R.id.rc_reqfriend);

        // 아이디 설정
        mid = context.getMid();
        id.setText(mid);



        btn_plusfrined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, frinedsearchActivity.class);
                startActivity(intent);
            }
        });

        btn_myshoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Myshoes.class);
                startActivity(intent);
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewsetting.class);
                startActivityForResult(intent,200);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        frdlistrequest(context.getMid());
        reqreqfriendinfo(context.getMid());
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
                    }
                }
    }


    public void reqreqfriendinfo(String id){
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/callfriendinfo.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                    int frdnum = jsonObject.getInt("num");
                    Log.e("json",String.valueOf(jsonObject));
                    reqfriendInfoarr = new ArrayList<>();
                if(success) {
                    for(int i=0; i< frdnum; i++){
                       JSONObject jsonObject1 = jsonObject.getJSONObject("user"+i);
                       String uid = jsonObject1.getString("id");
                       User friendInfo = new User();
                        friendInfo.setId(uid);
                        reqfriendInfoarr.add(friendInfo);
                    }
                    
                    // 라사이클러뷰에 넣기
                    adapter = new reqfrdlist_Adapter(mid,reqfriendInfoarr,Fragment4.this);

                    LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    rq_recyclerView.setLayoutManager(linearLayoutManager);
                    rq_recyclerView.setAdapter(adapter);
                } else {
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("userID", id);

                // 서버에 데이터 보내고 응답 요청
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(smpr);
    }




    public void frdlistrequest(String mid){
              // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
              String serverUrl="http://3.143.9.214/myfriendinfo.php";

              // 파일 전송 요청 객체 생성[결과를 String으로 받음]
              SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
              try {
                  JSONObject jsonObject = new JSONObject(response);
                  Log.e("json",String.valueOf(jsonObject));
                  int fnum = jsonObject.getInt("fnum");
                    if(fnum>0){
                        myfrindInfoArrayList = new ArrayList<>();
                        for(int i=0;i<fnum;i++){
                            User user = new User();
                            user.setId(jsonObject.getString("friend"+i));
                            myfrindInfoArrayList.add(user);

                        }

                        // 라사이클러뷰에 넣기
                        myfriend_adapter = new myfriendlist_Adapter(mid,myfrindInfoArrayList);

                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        myfrd_recyclerView.setLayoutManager(linearLayoutManager);
                        myfrd_recyclerView.setAdapter(myfriend_adapter);
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
              smpr.addStringParam("f_id", mid);

              // 서버에 데이터 보내고 응답 요청
              RequestQueue requestQueue = Volley.newRequestQueue(context);
              requestQueue.add(smpr);
        }

    public void logout(){
        context.logout();
        context.finish();
    }

}
