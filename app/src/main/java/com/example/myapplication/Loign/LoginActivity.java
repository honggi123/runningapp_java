package com.example.myapplication.Loign;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Join.JoinActivity;
import com.example.myapplication.Join.snsjoinActivity;
import com.example.myapplication.MainAct;
import com.example.myapplication.R;
import com.example.myapplication.Request.IdchkRequest;
import com.example.myapplication.Request.LoginRequest;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;


import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText edit_id;
    EditText edit_pw;
    Button btn_login;
    Button btn_join;
    TextView btn_findid;
    TextView btn_pwfind;
    ImageButton btn_kakaologin;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    // 로그인 정보 담는 shared
    SharedPreferences Loginshared;
    SharedPreferences.Editor loginedit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);
        btn_findid = findViewById(R.id.btn_findid);
        btn_pwfind = findViewById(R.id.btn_findpw);

        btn_join = findViewById(R.id.btn_join);
        btn_login = findViewById(R.id.btn_login);
        btn_kakaologin = findViewById(R.id.btn_kakaologin);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        // 로그인 정보 담는 sharedpref
        Loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        loginedit = Loginshared.edit();
        if(Loginshared.getBoolean("dologin",false)){
            Intent intent = new Intent(LoginActivity.this, MainAct.class);
            startActivity(intent);
            finish();
        }
        // 카카오 로그인 버튼
        btn_kakaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });


        // 회원가입 인텐트 버튼
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mID = edit_id.getText().toString();
                String mPW = edit_pw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("json", response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 회원등록에 성공한 경우
                                loginedit.putBoolean("dologin",true);
                                loginedit.putString("logintype","standard");
                                loginedit.putString("id",mID);
                                loginedit.commit();

                                Toast.makeText(LoginActivity.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainAct.class);
                                startActivity(intent);
                                finish();
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(LoginActivity.this, "아이디나 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청을 함
                LoginRequest loginRequest = new LoginRequest(mID, mPW, Request.Method.POST, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });


        // 아이디 찾기
        btn_findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogidfind idfinddialog = new Dialogidfind(LoginActivity.this);
                idfinddialog.calldialog();
            }
        });


        // 비밀번호 찾기
        btn_pwfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogpwfind dialogpwfind = new Dialogpwfind(LoginActivity.this);
                dialogpwfind.calldialog();
            }
        });

    }




    public class SessionCallback implements ISessionCallback {
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            Long snsid = result.getId();
                            // 이 어플 내 데이터베이스에 회원가입 여부 체크
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.e("json", response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.e("json",String.valueOf(jsonObject));
                                        boolean haveid = jsonObject.getBoolean("haveid");
                                        if (haveid) { // 중복된 snsid 아이디가 있는 경우 -> 로그인 액티비티
                                            String mID = jsonObject.getString("loginid");
                                            loginedit.putBoolean("dologin",true);
                                            loginedit.putString("logintype","kakao");
                                            loginedit.putString("id",mID);
                                            loginedit.commit();

                                            Toast.makeText(LoginActivity.this, "이미 회원으로 등록되어있습니다..로그인 성공", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainAct.class);
                                            startActivity(intent);
                                            finish();
                                        } else { // 중복된 snsid 아이디가 없는 경우 -> 회원등록 액티비티
                                            Toast.makeText(LoginActivity.this, "회원으로 등록하십시오.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, snsjoinActivity.class);
                                            intent.putExtra("snsid",snsid);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            //서버로 volley를 이용해서 요청을 함
                            IdchkRequest idchkRequest = new IdchkRequest(null,String.valueOf(snsid), Request.Method.POST, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                            queue.add(idchkRequest);

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {
                                // 이메일
                                String email = kakaoAccount.getEmail();
                                Gender gender = kakaoAccount.getGender();

                                if(gender != null){
                                    Log.i("KAKAO_API", "gender: " + gender);
                                }

                                if (email != null) {
                                    Log.i("KAKAO_API", "email: " + email);

                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                                } else {
                                    // 이메일 획득 불가
                                }

                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if (profile != null) {
                                    Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능

                                } else {
                                    // 프로필 획득 불가
                                }
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}





