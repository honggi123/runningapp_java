package com.example.myapplication.Profile;




import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.Run.runActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class frinedsearchActivity extends AppCompatActivity {

    ImageView btn_frdid;
    EditText edit_frdid;
    TextView frdidview;
    TextView frdgenderview;
    TextView frdttimeview;
    TextView frdtdistanceview;
    ImageView btn_addfrd;
    Boolean friend_or_not;

    SharedPreferences loginshared;

    ConstraintLayout frineitem;
    ArrayList<User> arr_finfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriends);
        edit_frdid = findViewById(R.id.edit_frdid);
        btn_frdid = findViewById(R.id.btn_findfrd);
        frineitem = findViewById(R.id.frinditem_layout);
        frdidview = findViewById(R.id.frdidview_findfrd);
        frdgenderview = findViewById(R.id.frdgender_findfrd);
        frdttimeview = findViewById(R.id.frdttimeview_findfrd);
        frdtdistanceview = findViewById(R.id.frdtrunview_findfrd);
        btn_addfrd = findViewById(R.id.btn_addfrd_findfrd);
        frineitem.setVisibility(View.INVISIBLE);

        // 로그인 정보
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        String f_id = loginshared.getString("id", null);


        btn_frdid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friendchk(f_id,edit_frdid.getText().toString());

                friendaddorcancel(f_id,edit_frdid.getText().toString());
            }
        });

        edit_frdid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                frineitem.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    public void friendchk(String f_id,String t_id){

                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/friendchk.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                Log.e("json",String.valueOf(jsonObject));
                boolean success = jsonObject.getBoolean("success");
                if(success) {
                    friend_or_not = jsonObject.getBoolean("friend");

                } else {
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                Toast.makeText(frinedsearchActivity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("fid", f_id);
                smpr.addStringParam("tid", t_id);

                // 서버에 데이터 보내고 응답 요청
                RequestQueue requestQueue = Volley.newRequestQueue(frinedsearchActivity.this);
                requestQueue.add(smpr);

    }



    public void friendaddorcancel(String f_id, String t_id){

                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/searchid.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                Log.e("json",String.valueOf(jsonObject));
                boolean success = jsonObject.getBoolean("success");
                boolean have = jsonObject.getBoolean("haveid");
                if(success) {
                    if(have){
                        JSONObject jsonObject2  =jsonObject.getJSONObject("user");
                             frdidview.setText(jsonObject2.getString("id"));
                        if(jsonObject2.getString("gender").equals("f")){
                            frdgenderview.setText("(여자)");
                        }


                        double kmdistance = (Double.parseDouble(jsonObject2.getString("t_distance")) / 1000.00);
                        frdttimeview.setText(String.format("%.2f",kmdistance)+"km");
                        String timeformat =new runActivity().TimeToFormat(jsonObject2.getInt("t_time"));
                        frdtdistanceview.setText(timeformat);

                        if(friend_or_not){ // 친구면
                            btn_addfrd.setVisibility(View.INVISIBLE);
                        }else{
                            if(jsonObject.getBoolean("frdchk")){      // 친구신청을 했으면
                                btn_addfrd.setImageResource(R.drawable.ic_baseline_person_remove_24);
                            }else{          // 친구 신청을 안했으면
                                btn_addfrd.setImageResource(R.drawable.ic_baseline_person_add_24);
                            }
                        }

                        btn_addfrd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_addfrd.setImageResource(R.drawable.ic_baseline_person_remove_24);
                                reqfriend(f_id,t_id);
                            }
                        });

                         frineitem.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(getApplicationContext(),"친구 정보가 없습니다.",Toast.LENGTH_SHORT);
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
                Toast.makeText(frinedsearchActivity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                });
                 // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("f_id", f_id);
                smpr.addStringParam("t_id", t_id);

                // 서버에 데이터 보내고 응답 요청
                RequestQueue requestQueue = Volley.newRequestQueue(frinedsearchActivity.this);
                requestQueue.add(smpr);
                }




    public void reqfriend(String f_id,String t_id){

                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/addfriend.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",String.valueOf(jsonObject));
                    boolean success = jsonObject.getBoolean("success");
                if(success) {
                    boolean reqfriend = jsonObject.getBoolean("reqfriend");
                    // 친구 신청 성공
                    if(reqfriend){
                        btn_addfrd.setImageResource(R.drawable.ic_baseline_person_remove_24);
                    }else{    // 친구 신청 취소
                        btn_addfrd.setImageResource(R.drawable.ic_baseline_person_add_24);
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
                 Toast.makeText(frinedsearchActivity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("fid", f_id);
                smpr.addStringParam("tid",t_id);

                // 서버에 데이터 보내고 응답 요청
                RequestQueue requestQueue = Volley.newRequestQueue(frinedsearchActivity.this);
                requestQueue.add(smpr);


    }
}
