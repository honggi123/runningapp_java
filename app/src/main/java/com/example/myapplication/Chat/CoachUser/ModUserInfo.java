package com.example.myapplication.Chat.CoachUser;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ModUserInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    String userID;
    Userinfo userinfo;
    TextView tid;
    TextView tgoal;
    TextView tsdate;
    TextView tweekcount;
    TextView tgroup;
    TextView tmemo;

    Button btn_setsdate;
    Button btn_setgroup;
    Button btn_mod;
    Button btn_setweeknum;

    int groupnum;


    String[] arrcategory;
    SharedPreferences loginshared;
    String cid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moduserinfo);

        userID = getIntent().getStringExtra("userID");
        String suserinfo = getIntent().getStringExtra("userinfo");

        Gson gson = new Gson();
        userinfo = gson.fromJson(suserinfo, Userinfo.class);


        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        cid = loginshared.getString("id", null);

        tid = findViewById(R.id.tid);
        tgoal = findViewById(R.id.editgoal);
        tsdate = findViewById(R.id.editsdate);
        tweekcount = findViewById(R.id.editweekcount);
        tmemo = findViewById(R.id.editmemo);
        tgroup = findViewById(R.id.editgroup);

        btn_mod = findViewById(R.id.btn_mod);



        tid.setText(userID);
        tgoal.setText(userinfo.goal);
        tsdate.setText(userinfo.sdate);
        tweekcount.setText(String.valueOf(userinfo.weeknum));
        tmemo.setText(userinfo.uniq);
        tgroup.setText(userinfo.group);

        getcategoryrequest(cid);

        tgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new grouppickdialog().calldialog();
            }
        });

        tweekcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tsdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatepicker();
            }
        });



        btn_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modmeminfo();
            }
        });

    }

    public void showdatepicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(ModUserInfo.this,ModUserInfo.this,year,month,day).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );

        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = new Date();
        Date currentDate = null;
        String oTime = mSimpleDateFormat.format ( currentTime ); //현재시간 (String)
        Date s_date = null;

        try {
            currentDate =  mSimpleDateFormat.parse( oTime );
            s_date = format.parse(year +"-"+(month+1)+"-"+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(s_date.compareTo(currentDate) < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ModUserInfo.this);
            builder.setTitle("현재 날짜 이후로 설정 하십시오. ")        // 제목 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                        }
                    });
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
        }else{
            tsdate.setText(format.format(s_date));
        }
    }


    public void modmeminfo(){

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.12.49.32/moduserinfo.php";
        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response",response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        // 업로드 성공
                      //  Toast.makeText(ModUserInfo.this, userid+"님을 코칭 회원으로 등록했습니다", Toast.LENGTH_SHORT).show();
                      Intent intentR = new Intent();
                      Gson gson = new Gson();
                        userinfo.setUniq(tmemo.getText().toString());
                        userinfo.setGroup(tgroup.getText().toString());
                        userinfo.setSdate(tsdate.getText().toString());
                        userinfo.setWeeknum(Integer.parseInt(tweekcount.getText().toString()));
                        userinfo.setGoal(tgoal.getText().toString());

                      String suserinfo = gson.toJson(userinfo);

                      intentR.putExtra("userinfo",suserinfo); //사용자에게 입력받은값 넣기
                        setResult(RESULT_OK,intentR); //결과를 저장

                        finish();
                    } else {
                        // 업로드 실패
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



        Userinfo userinfo = new Userinfo();
        userinfo.setGoal(tgoal.getText().toString());
        userinfo.setSdate(tsdate.getText().toString());
        userinfo.setGroup(tgroup.getText().toString());
        userinfo.setWeeknum(Integer.parseInt(tweekcount.getText().toString()));
        userinfo.setUniq(tmemo.getText().toString());


        Gson gson = new Gson();
        String suserinfo = gson.toJson(userinfo);

        // 요청 객체에 보낼 데이터를 추가

        smpr.addStringParam("cid", cid);
        smpr.addStringParam("uid", userID);
        smpr.addStringParam("category", tgroup.getText().toString());
        smpr.addStringParam("userinfo", suserinfo);


        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(smpr);
    }


    public class grouppickdialog {
        Dialog dig;
        Button btnset;
        Button btncan;
        NumberPicker grouppicker;

        public void calldialog() {
            dig = new Dialog(ModUserInfo.this);
            // 액티비티의 타이틀바를 숨긴다.
            dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dig.setContentView(R.layout.groupseldialog);
            btnset = dig.findViewById(R.id.btn_set);
            btncan = dig.findViewById(R.id.btn_can);
            grouppicker = dig.findViewById(R.id.groupicker);

            // Number Picker Setting
            grouppicker.setMinValue(0);
            grouppicker.setMaxValue(groupnum-1);

            grouppicker.setDisplayedValues(arrcategory);

            for (int i = 0; i < arrcategory.length; i++){
                      if(arrcategory[i].equals(userinfo.group)){
                          grouppicker.setValue(i);
                      }
            }


            btnset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    tgroup.setText(arrcategory[grouppicker.getValue()]);
                    dig.dismiss();
                }
            });
            btncan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dig.dismiss();
                }
            });

            dig.show();
        }
    }


    public void getcategoryrequest(String cid){
        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.12.49.32/getcoachuserinfo.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response",response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        JSONArray jsonArray=new JSONArray(jsonObject.getString("category"));
                        groupnum = jsonArray.length();
                        arrcategory = new String[jsonArray.length()];

                        for(int i = 0;i < arrcategory.length; i++ ){
                            arrcategory[i] = jsonArray.getJSONObject(i).getString("name") ;
                            Log.e("arrcat",arrcategory[i]);
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
            }
        });

        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("coachID", cid);

        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(smpr);
    }

}
