package com.example.myapplication.Join;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainAct;
import com.example.myapplication.R;
import com.example.myapplication.Request.IdchkRequest;
import com.example.myapplication.Request.SnsRegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class snsjoinActivity extends AppCompatActivity {

    EditText Editid;
    EditText EditKG;
    Button btn_idchk;
    Button btn_complete;
    String mID;
    Boolean idchk;
    Long snsid;
    SharedPreferences Loginshared;
    SharedPreferences.Editor loginedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("snsjoin","act");
        setContentView(R.layout.snsjoin);
        Editid = findViewById(R.id.Edit_IDsnslogin);
//       EditKG = findViewById(R.id.Edit_KGsnslogin);
        btn_idchk = findViewById(R.id.btn_idchksnslogin);
        btn_complete = findViewById(R.id.btn_compsnslogin);
        snsid = getIntent().getLongExtra("snsid",0);
        // 로그인 정보 담는 sharedpref
        Loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        loginedit = Loginshared.edit();

        Editid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idchk = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 아이디 중복검사 버튼
        btn_idchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mID = Editid.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("json", response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean haveid = jsonObject.getBoolean("haveid");
                            if (haveid) { // 중복된 회원 아이디가 있는 경우
                                Toast.makeText(snsjoinActivity.this, "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                                idchk = false;
                            } else { // 중복된 회원 아이디가 없는 경우
                                Toast.makeText(snsjoinActivity.this, "중복된 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                                Editid.setEnabled(false);
                                idchk = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청을 함
                IdchkRequest idchkRequest = new IdchkRequest(mID,null, Request.Method.POST, responseListener);
                RequestQueue queue = Volley.newRequestQueue(snsjoinActivity.this);
                queue.add(idchkRequest);
            }
        });


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mID = Editid.getText().toString();
                if(idchk){
                            // sns 계정 등록
                            Response.Listener<String> kakaoresponseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.e("KAKAOjson", response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) { // 회원등록에 성공한 경우
                                            // 로그인 정보 저장
                                            loginedit.putBoolean("dologin",true);
                                            loginedit.putString("logintype","kakao");
                                            loginedit.putString("id",mID);
                                            loginedit.commit();

                                            Toast.makeText(snsjoinActivity.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(snsjoinActivity.this, MainAct.class);
                                            startActivity(intent);
                                            ActivityCompat.finishAffinity(snsjoinActivity.this);

                                        } else { // 회원등록에 실패한 경우
                                            Toast.makeText(snsjoinActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            //서버로 volley를 이용해서 요청을 함
                            SnsRegisterRequest registerRequest = new SnsRegisterRequest(mID, null,String.valueOf(snsid),"kakao", Request.Method.POST, kakaoresponseListener);
                            RequestQueue queue = Volley.newRequestQueue(snsjoinActivity.this);
                            queue.add(registerRequest);
                }else{
                    Toast.makeText(snsjoinActivity.this,"필수사항들을 체크해주세요.",Toast.LENGTH_SHORT);
                }
            }
        });
    }






}
