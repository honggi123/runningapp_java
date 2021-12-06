package com.example.myapplication.viewact.Coach;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
// implements DatePickerDialog.OnDateSetListener


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.example.myapplication.Socketsingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    NumberPicker numberPicker;
    Button btn_runnumnext;

    ArrayList<String> arrayList;
    LinearLayout layoutweekquestion;
    LinearLayout layoutstartdate;
    int questionnum = 3;
    int nowquestionnum = 1;
    TextView choicename;
    Button btn_setstartdate;
    TextView coachstartdate;
    SimpleDateFormat format ;
    Date s_date = null;
    TextView totalqnum;
    TextView nowqnum;
    FrameLayout weeknumserframe;
    FrameLayout coachuserframe;
    FrameLayout startdateframe;

    RecyclerView rccoachuser;
    ArrayList<CoachUser> arr_coachuser;
    CoachUserAdapter coachUserAdapter;
    String coachname;
    String mid;
    SharedPreferences loginshared;
    String logintype;
    ArrayList<String> arrq;
    LinearLayout prognum;

    OutputStream socketoutputStream;
    PrintWriter sendWriter;
    Socketsingleton socketsingleton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coachinputinfo);

        coachname = getIntent().getStringExtra("coachname");
        arrayList = getIntent().getStringArrayListExtra("arrselect");
        arrq = getIntent().getStringArrayListExtra("arrquestion");

        coachstartdate = findViewById(R.id.coachstartdate);
        numberPicker = findViewById(R.id.numberpicker5);
        btn_runnumnext = findViewById(R.id.btn_runnumnext);
        layoutweekquestion = findViewById(R.id.layoutweekquestion);
        layoutstartdate = findViewById(R.id.layoutstartdate);
        totalqnum = findViewById(R.id.totalqnum);
        nowqnum = findViewById(R.id.nowqnum);
        choicename = findViewById(R.id.choicename);
        btn_setstartdate = findViewById(R.id.btn_setstartdate);
        prognum = findViewById(R.id.prognum);
        // 질문 프레임
        weeknumserframe = findViewById(R.id.weeknumserframe);
        coachuserframe = findViewById(R.id.coachuserframe);
        startdateframe = findViewById(R.id.startdateframe);

        weeknumserframe.setVisibility(View.VISIBLE);
        coachuserframe.setVisibility(View.GONE);
        startdateframe.setVisibility(View.GONE);

        rccoachuser = findViewById(R.id.rc_coachuser);

        // 소켓
        socketsingleton = socketsingleton.getInstance(getApplicationContext());
        try {
            socketoutputStream = socketsingleton.getSocket().getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendWriter = new PrintWriter(socketoutputStream);

        // 아이디 설정
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        Log.e("ProfileMenuAct","14");
        mid = loginshared.getString("id", null);

        // Number Picker Setting
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(7);
        String[] arr = new String[7];
        format = new SimpleDateFormat("yyyy-MM-dd");

        // 전체 질문 개수 초기화
        totalqnum.setText(arrayList.size()+3+"");

        // 현재 질문 개수 초기화
        nowqnum.setText(arrayList.size()+1+"");

        for(int i = 0;i < arr.length; i++ ){
            arr[i] = String.valueOf(i+1) ;
        }

        numberPicker.setDisplayedValues(arr);

        btn_runnumnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(nowquestionnum ==1){
                    arrayList.add(String.valueOf(numberPicker.getValue())+"회");
                    arrq.add("일주일에 원하는 운동 횟수");
                }
                nowquestionnum++;
                nowqnum.setText(arrayList.size()+nowquestionnum+"");
                if(nowquestionnum>questionnum){
                    /*
                    Intent intent = new Intent(GetInfoActivity.this,CoachPlanActivity.class);
                    intent.putExtra("arrselect",arrayList);
                    intent.putExtra("startdate",format.format(s_date));
                    startActivity(intent);
                     */
                    arrayList.add(coachUserAdapter.getselid());
                    request(mid);

                }else if(nowquestionnum == 2){
                    String question = "원하는 코칭 시작일을 선택해주세요.";
                    choicename.setText(question);
                    arrq.add("원하는 코칭 시작일");
                    weeknumserframe.setVisibility(View.GONE);
                    coachuserframe.setVisibility(View.GONE);
                    startdateframe.setVisibility(View.VISIBLE);
                    btn_setstartdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showdatepicker();
                        }
                    });

                }else if(nowquestionnum ==3){
                    prognum.setVisibility(View.INVISIBLE);
                    String question = "코칭을 받고싶은 코치를 선택해주세요";
                    choicename.setText(question);

                    weeknumserframe.setVisibility(View.GONE);
                    coachuserframe.setVisibility(View.VISIBLE);
                    startdateframe.setVisibility(View.GONE);

                    //  리사이클러뷰 xml id

                            // 라사이클러뷰에 넣기
                            // 어댑터 객체 생성
                            arr_coachuser = new ArrayList<CoachUser>();
                            coachUserAdapter = new CoachUserAdapter(arr_coachuser);

                            GridLayoutManager gridLayoutManager =  new GridLayoutManager(GetInfoActivity.this,2);
                            gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            rccoachuser.setLayoutManager(gridLayoutManager);
                            // 어댑터 추가
                          rccoachuser.setAdapter(coachUserAdapter);

                    coachrequest();
                }
            }
        });
    }


    public void coachrequest(){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/getcoachuser.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            Log.e("response",response);
            boolean success = jsonObject.getBoolean("success");
                if(success) {
                    int num = jsonObject.getInt("num");
                    for(int i= 0; i< num; i++){
                        JSONArray jsonObject1 = jsonObject.getJSONArray("coachuser");
                        JSONObject data = jsonObject1.getJSONObject(i);
                        CoachUser coachUser =  new CoachUser();
                        coachUser.setId(data.getString("id"));
                        coachUser.setName(data.getString("name"));
                        coachUser.setCareer(data.getString("career"));
                        coachUser.setDesc(data.getString("description"));
                        arr_coachuser.add(coachUser);
                        coachUserAdapter.notifyDataSetChanged();
                    }
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
            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(GetInfoActivity.this);
            requestQueue.add(smpr);
            }



    public void showdatepicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int start_or_end = 2;
        new DatePickerDialog(GetInfoActivity.this,GetInfoActivity.this,year,month,day).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );

        Date currentTime = new Date();
        Date currentDate = null;
        String oTime = mSimpleDateFormat.format ( currentTime ); //현재시간 (String)
        try {
             currentDate =  mSimpleDateFormat.parse( oTime );
            s_date = format.parse(year +"-"+(month+1)+"-"+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(s_date.compareTo(currentDate) < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GetInfoActivity.this);
            builder.setTitle("현재 날짜 이후로 설정 하십시오. ")        // 제목 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                            s_date = null;
                        }
                    });
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
        }else{
            coachstartdate.setText(format.format(s_date));
            arrayList.add(format.format(s_date));
        }
    }

    public void request(String mid){
        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.12.49.32/getuserinfo.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",jsonObject+"");
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        JSONObject json =jsonObject.getJSONObject("data");
                        Log.e("json",json+"");

                        String coachid = null;

                        for (int i = 0; i < arr_coachuser.size(); i++){
                            if(arr_coachuser.get(i).getSel()){
                                coachid= arr_coachuser.get(i).id;
                            }
                        }
                        String msg = "coachask@"+coachname+"@"+mid+"@"+coachid;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar c1 = Calendar.getInstance();
                        String strToday = sdf.format(c1.getTime());
                        msg+= "@"+strToday;

                        msg += "@question";

                        JSONObject qjsonObject = new JSONObject();

                        Log.e("arrq",arrq.size()+"");
                        Log.e("arrayList",arrayList.size()+"");
                        for (int i = 0; i < arrq.size(); i++){
                            qjsonObject.put(arrq.get(i), arrayList.get(i));
                        }

                        msg += "@"+qjsonObject.toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(GetInfoActivity.this);
                        String finalMsg = msg;
                        builder.setTitle("코치에게 신체정보 와 질문지를 포함한 신청서를 전송하겠습니까?")        // 제목 설정
                                                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                    // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                    public void onClick(DialogInterface dialog, int whichButton){
                                        //원하는 클릭 이벤트를 넣으시면 됩니다.

                                        // 메시지 보내기
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                super.run();
                                                try {
                                                    sendWriter.println(json);
                                                    //  sendWriter.println(n);
                                                    sendWriter.flush();

                                                }catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();

                                        finish();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                                    // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                                    public void onClick(DialogInterface dialog, int whichButton){
                                        //원하는 클릭 이벤트를 넣으시면 됩니다.
                                        finish();
                                    }
                                });

                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();    // 알림창 띄우기




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
        smpr.addStringParam("userID", mid);

        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(smpr);

    }

}
