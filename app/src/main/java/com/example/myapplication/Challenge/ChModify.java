package com.example.myapplication.Challenge;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChModify extends AppCompatActivity {



    ImageView btn_distancesel;
    ImageView btn_datesel;
    Button btn_comp;
    EditText edit_chname;
    SharedPreferences Challengeshared;
    TextView view_distance;
    TextView view_date;
    float distance;
    String startdate;
    String enddate;
    SharedPreferences Loginshared;
    ChallengeInfo challengeInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengemodify);

        edit_chname =findViewById(R.id.edit_chname_chmd);
        btn_comp = findViewById(R.id.btn_comp_chmd);
        view_date = findViewById(R.id.view_date_chmd);
        view_distance = findViewById(R.id.view_distance_chmd);

        Loginshared = getSharedPreferences("Login", MODE_PRIVATE);

        distance = 0.0f;
        challengeInfo = (ChallengeInfo) getIntent().getSerializableExtra("chinfo");

        edit_chname.setText(challengeInfo.name);

//        btn_datesel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChModify.this,Challengedatemk_Activity.class);
//                startActivity(intent);
//            }
//        });
//        btn_distancesel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChModify.this,Challengedistancemk_Activity.class);
//                startActivity(intent);
//            }
//        });
        btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = null;
                // 이름을 입력 했는지 체크
                if(edit_chname.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChModify.this);
                    builder.setTitle("챌린지 이름을 채워주세요.")        // 제목 설정
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
                }else {
                    // 이름 입력 완료 시
                    name = edit_chname.getText().toString();

                    if (distance == 0.0f || startdate == null || enddate == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChModify.this);
                        builder.setTitle("모든 양식을 채워주세요.")        // 제목 설정
                                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //원하는 클릭 이벤트를 넣으시면 됩니다.
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();    // 알림창 띄우기
                    } else {
                        // 모든 양식이 채워졌을때 서버에 데이터를 보낸다
                        request(name);
                    }
                }
            }
        });
        btn_comp.setEnabled(false);

        edit_chname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit_chname.getText().toString().equals(challengeInfo.name)){
                    btn_comp.setEnabled(false);
                }else{
                    btn_comp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        // 날짜, 목표 거리 받아오기
        Challengeshared = getSharedPreferences("ch", MODE_PRIVATE);
        distance = Challengeshared.getFloat("distance", 0.0F);

        if(distance != 0.0f){
            view_distance.setText(distance+" km");
        }else {
            Log.e("g_distance",challengeInfo.g_distance+"");
            distance = challengeInfo.g_distance / 1000 + challengeInfo.g_distance % 1000;
            double mdistance = (challengeInfo.g_distance / 1000.00);
            view_distance.setText(String.format("%.2f",mdistance)+" km");
        }

        startdate = Challengeshared.getString("startdate",null);
        enddate = Challengeshared.getString("enddate",null);



        if(startdate != null && enddate != null){
            String newdate =timetodate(enddate);
            view_date.setText(startdate + " ~ " + newdate);
        }else{
            startdate = challengeInfo.s_date;
            enddate = challengeInfo.g_date;
            String newdate =timetodate(enddate);
            view_date.setText(startdate +" ~ "+newdate);
        }


    }

    public String timetodate(String olddate){
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        String newdate = null;
        try {
            date = simpleDate.parse(olddate);
            newdate =newformat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = Challengeshared.edit();
        edit.clear();
        edit.commit();
    }

    public void request(String name){

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(ChModify.this,
                "기다려주세요..", null, true, true);

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소onDateSet
        String serverUrl="http://3.12.49.32/chmodify.php";
        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        // 업로드 성공
                        Toast.makeText(ChModify.this, "수정 되었습니다.", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor edit = Challengeshared.edit();

                        challengeInfo.setId(Loginshared.getString("id", null));
                        challengeInfo.setName(name);

                        Intent intentR = new Intent();
                        intentR.putExtra("chinfo" , challengeInfo); //사용자에게 입력받은값 넣기
                        setResult(RESULT_OK,intentR); //결과를 저장

                        edit.clear();
                        edit.commit();
                        finish();
                    } else {
                        // 업로드 실패
                        Toast.makeText(ChModify.this, "수정 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ChModify.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("cno", String.valueOf(challengeInfo.cno));
        smpr.addStringParam("id",Loginshared.getString("id", null));
        smpr.addStringParam("name", name);

        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = Volley.newRequestQueue(ChModify.this);
        requestQueue.add(smpr);


    }



}
