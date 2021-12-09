package com.example.myapplication.Chat.CoachUser;


// implements DatePickerDialog.OnDateSetListener

import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.Chat.MessageItem;
import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.example.myapplication.viewact.Coach.GetInfoActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SetMemInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    String roomno;
    Gson gson = new Gson();
    JSONObject quesitonjson;
    String cid;
    EditText editgoal;
    TextView editsdate;
    TextView editweekcount;
    TextView editgroup;
    EditText editmemo;
    TextView id;

    Button btn_memset;

    int groupnum;
    String userid;

    String[] arrcategory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmeminfo);

        editgoal = findViewById(R.id.editgoal);
        editsdate = findViewById(R.id.sdate);

        editweekcount = findViewById(R.id.weeknumt);
        editmemo = findViewById(R.id.editmemo);
        editgroup = findViewById(R.id.editgroup);
        id = findViewById(R.id.id);


        btn_memset = findViewById(R.id.btn_memset);


        roomno = getIntent().getStringExtra("chatroomno");
        cid = getIntent().getStringExtra("coachid");
        userid = getIntent().getStringExtra("userid");

        Log.e("roomno",roomno);
        requestquestion(roomno);

        id.setText(userid);

        getcategoryrequest(cid);


        editweekcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new weeknumpickdialog().calldialog();
            }
        });
        editgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new grouppickdialog().calldialog();
            }
        });

        editsdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("datepicker","date");
                try {
                    showdatepicker();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_memset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmeminfo();
            }
        });



    }

    public void showdatepicker() throws ParseException {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat Format = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
        Date d = Format.parse(editsdate.getText().toString());

        SimpleDateFormat yearFormat = new SimpleDateFormat ( "yyyy", Locale.KOREA );
        SimpleDateFormat dayFormat = new SimpleDateFormat ( "dd", Locale.KOREA );
        SimpleDateFormat monthFormat = new SimpleDateFormat ( "MM", Locale.KOREA );


        new DatePickerDialog(SetMemInfo.this,SetMemInfo.this,Integer.parseInt(yearFormat.format(d)),
                Integer.parseInt(monthFormat.format(d))-1,Integer.parseInt(dayFormat.format(d))).show();
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

        editsdate.setText(format.format(s_date));

    }


    public void setmeminfo(){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/setuserinfo.php";
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
                Toast.makeText(SetMemInfo.this, userid+"님을 코칭 회원으로 등록했습니다", Toast.LENGTH_SHORT).show();

                Intent intentR = new Intent();
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
            userinfo.setGoal(editgoal.getText().toString());
            userinfo.setSdate(editsdate.getText().toString());
            userinfo.setGroup(editgroup.getText().toString());
            userinfo.setUniq(editmemo.getText().toString());
            userinfo.setWeeknum(Integer.parseInt(editweekcount.getText().toString()));
            Gson gson = new Gson();
            String suserinfo = gson.toJson(userinfo);

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("cid", cid);
            smpr.addStringParam("uid", userid);
            smpr.addStringParam("category", editgroup.getText().toString());
            smpr.addStringParam("userinfo", suserinfo);


            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
            }

    public void requestquestion(String roomno){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/getquestion.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            Log.e("response",response);
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            if(success) {
                String question = jsonObject.getString("question");
                JSONObject jsonObject1 = new JSONObject(question);
                editgoal.setText(jsonObject1.getString("준비하고 싶은 레이스"));
                editsdate.setText(jsonObject1.getString("원하는 코칭 시작일"));
                String str = (jsonObject1.getString("일주일에 원하는 운동 횟수")).replaceAll("[^0-9]","");
                editweekcount.setText(str);

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
            smpr.addStringParam("rno", roomno);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
            }

    public class weeknumpickdialog {
        Dialog dig;
        Button btnset;
        Button btncan;
        NumberPicker numberpicker;
        TextView txt;

        public void calldialog() {
            dig = new Dialog(SetMemInfo.this);
            // 액티비티의 타이틀바를 숨긴다.
            dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dig.setContentView(R.layout.groupseldialog);
            txt = dig.findViewById(R.id.txt);

            btnset = dig.findViewById(R.id.btn_set);
            btncan = dig.findViewById(R.id.btn_can);
            numberpicker = dig.findViewById(R.id.groupicker);

            txt.setText("일주일동안 회원과 운동 할 횟수를 선택하세요.");

            // Number Picker Setting
            numberpicker.setMinValue(1);
            numberpicker.setMaxValue(7);
            String[] arr = new String[7];

            for(int i = 0;i < arr.length; i++ ){
                arr[i] = String.valueOf(i+1) ;
            }
            numberpicker.setDisplayedValues(arr);

            numberpicker.setValue(Integer.parseInt(editweekcount.getText().toString()));

            btnset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    editweekcount.setText(arr[numberpicker.getValue()-1]);
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


       public class grouppickdialog {
                  Dialog dig;
                  Button btnset;
                  Button btncan;
                  NumberPicker grouppicker;

                  public void calldialog() {
                      dig = new Dialog(SetMemInfo.this);
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

                      btnset.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v){
                              editgroup.setText(arrcategory[grouppicker.getValue()]);
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
                        editgroup.setText(arrcategory[0]);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
