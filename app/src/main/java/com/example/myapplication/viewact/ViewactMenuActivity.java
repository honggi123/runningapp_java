package com.example.myapplication.viewact;

import static com.example.myapplication.Run.runActivity.TimeToFormat;

import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Challenge.Fragment3;
import com.example.myapplication.Profile.ProfileMenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.RequestInterface;
import com.example.myapplication.Run.RunMenuActivity;
import com.example.myapplication.Coaching;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

public class ViewactMenuActivity extends AppCompatActivity {

    ArrayList<RunInfo> arr_runinfo;

    dayviewact_Adapter adapter;
    RecyclerView recyclerView;
    Button btn_moreact;
    TextView viewtotalrun;
    TextView viewtotlatime;

    RecyclerView chrecyclerView;
    ArrayList<Coaching> arr_coach;
    CoachAdapter coachAdapter;

    TextView viewruncount;
    int max = 0;
    SharedPreferences loginshared;
    ArrayList<Float> jsonList;
    ArrayList<String> labelList ;
    BarChart barChart;
    int[] weeks_distance= new int[7];
    TextView minuteTextview;
    String mid;
    PieChart pieChart;
    ImageView btn_addruninfo;
    LinearLayoutManager linearLayoutManager;
    // 요일 별 목표 이미지 뷰
    ImageView mongoal;
    ImageView tuegoal;
    ImageView wedgoal;
    ImageView thugoal;
    ImageView frigoal;
    ImageView satgoal;
    ImageView sungoal;
    RequestQueue requestQueue;
    SimpleMultiPartRequest smpr;

    ImageView menurun;
    ImageView menuviewact;
    ImageView menuch;
    ImageView menumy;


    Context context;
    WeakReference<ViewactMenuActivity> activityWeakReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Viewact","1");
        setContentView(R.layout.fragment2);
        Log.e("Viewact","2");
        activityWeakReference = new WeakReference<>(ViewactMenuActivity.this);
        Log.e("Viewact","3");
        recyclerView = findViewById(R.id.rc_coaching);
        btn_moreact = findViewById(R.id.btn_viewrecentact_fr2);
        viewtotlatime = findViewById(R.id.view_totaltime);
        viewruncount = findViewById(R.id.view_runcount);
        btn_addruninfo = findViewById(R.id.addruninfo);

        menurun = findViewById(R.id.btn_menurun);
        menuviewact = findViewById(R.id.btn_menuviewact);
        menuch = findViewById(R.id.btn_menuch);
        menumy = findViewById(R.id.btn_menumy);
        Log.e("Viewact","3.5");
        mongoal = findViewById(R.id.mongoal);
        tuegoal = findViewById(R.id.tuegoal);
        wedgoal = findViewById(R.id.wedgoal);
        thugoal = findViewById(R.id.thugoal);
        frigoal = findViewById(R.id.frigoal);
        satgoal = findViewById(R.id.satgoal);
        sungoal = findViewById(R.id.sungoal);

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        Log.e("Viewact","4");
        mid = loginshared.getString("id", null);
        viewtotalrun = findViewById(R.id.viewtotalrunweek);
        barChart = (BarChart) findViewById(R.id.fragment_bluetooth_chat_barchart);
        Log.e("Viewact","5");
        context = ViewactMenuActivity.this;
        Log.e("Viewact","6");
        // 메뉴 초기화
        menuset();
        //getweekruninfo(mid);
        requestrecentact(mid);
        Log.e("Viewact","7");
        //getgoalinfo(mid);
        arr_coach = new ArrayList<Coaching>();
        //getcoachinfo();
        Log.e("Viewact","8");
        btn_addruninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Viewact","9");
                Intent intent = new Intent(ViewactMenuActivity.this, addruninfo.class);
                Log.e("Viewact","10");
                startActivityForResult(intent,105);
                Log.e("Viewact","11");
            }
        });

        Log.e("Viewact","12");
        //  리사이클러뷰 xml id
        chrecyclerView = findViewById(R.id.rc_coaching1);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        Log.e("Viewact","13");
        coachAdapter = new CoachAdapter(arr_coach);
        Log.e("Viewact","14");
        linearLayoutManager =  new LinearLayoutManager(ViewactMenuActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        chrecyclerView.setLayoutManager(linearLayoutManager);
        Log.e("Viewact","15");
        // 어댑터 추가
        chrecyclerView.setAdapter(coachAdapter);
        Log.e("Viewact","16");
      //  new  RequestInterface(getApplicationContext());
       // RequestInterface.getInstance(getApplicationContext());
    }



    public void getgoalinfo(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getgoalinfo.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            Log.e("json",jsonObject+"");
            boolean success = jsonObject.getBoolean("success");
            if(success) {
                    String mon = jsonObject.getString("mon");
                    String tue = jsonObject.getString("tue");
                    String wed = jsonObject.getString("wed");
                    String thu = jsonObject.getString("thu");
                    String fri = jsonObject.getString("fri");
                    String sat = jsonObject.getString("sat");
                    String sun = jsonObject.getString("sun");

                    /*
                    chggoalview(mon,mongoal);
                    chggoalview(tue,tuegoal);
                    chggoalview(wed,wedgoal);
                    chggoalview(thu,thugoal);
                    chggoalview(fri,frigoal);
                    chggoalview(sat,satgoal);
                    chggoalview(sun,sungoal);
*/

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
            smpr.addStringParam("id", mid);

            // 서버에 데이터 보내고 응답 요청
        requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(smpr);

        /*
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            Log.e("requestQueue","nulll");
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }

         */

    }


    public void chggoalview(String day, ImageView imageView){
        if(day.equals("true")){
            imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }else if(day.equals("false")){
            imageView.setImageResource(R.drawable.ic_baseline_clear_24);
        }else {
            imageView.setImageResource(R.drawable.ic_baseline_more_horiz_24);
        }
    }



    public void requestrecentact(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getrecentact.php";
        Log.e("request","1");
            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        Log.e("request","2");
            try {
           // JSONObject jsonObject = new JSONObject(response);
                Log.e("json",String.valueOf(response));
          //  boolean success = jsonObject.getBoolean("success");
         //   int num = jsonObject.getInt("num");
                Log.e("request","3");
         //   if(success) {
                Log.e("request","4");
        /*
                arr_runinfo = new ArrayList<RunInfo>();
                JSONArray data = jsonObject.getJSONArray("data");
                for(int i= 0; i < num; i++){
                    RunInfo runInfo = new RunInfo();
                    JSONObject dataJSONObject = data.getJSONObject(i);
                    runInfo.setMemo(dataJSONObject.getString("memo"));
                    Log.e("dis",dataJSONObject.getString("distance"));
                    runInfo.setDistance(Integer.parseInt(dataJSONObject.getString("distance")));
                    runInfo.setRating(Float.parseFloat(dataJSONObject.getString("rating")));

                    runInfo.setKacl(Integer.parseInt(dataJSONObject.getString("kcal")));

                    runInfo.setReg_date(dataJSONObject.getString("reg_date"));
                    runInfo.setTime(Integer.parseInt(dataJSONObject.getString("time")));
                    runInfo.setImgList(dataJSONObject.getString("imgList"));

                    arr_runinfo.add(runInfo);
                }

                if(num > 3){
                    // 라사이클러뷰에 넣기
                    adapter = new dayviewact_Adapter(arr_runinfo,3);

                    btn_moreact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ViewactMenuActivity.this, viewrecentactActivity.class);
                            intent.putExtra("arr_runinfo",arr_runinfo);
                            startActivity(intent);
                        }
                    });
                    btn_moreact.setVisibility(View.VISIBLE);
                }else{
                    // 라사이클러뷰에 넣기
                    adapter = new dayviewact_Adapter(arr_runinfo,num);
                }

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(ViewactMenuActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                */
          //  } else {
           // }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            }
            });
        Log.e("request","5");
            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("userID", mid);
        Log.e("request","6");
            // 서버에 데이터 보내고 응답 요청
        requestQueue = Volley.newRequestQueue(getApplicationContext());
             requestQueue.add(smpr);
        Log.e("request","7");
/*
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            Log.e("requestQueue","nulll");
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }
  */

    }


            public void getweekruninfo(String mid){
        Log.e("getweekruninfo","1");
                    // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                    String serverUrl="http://3.143.9.214/getweekruninfo.php";
                Log.e("getweekruninfo","2");
                    // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                  smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getweekruninforesponse",String.valueOf(response));
                Log.e("getweekruninfo","3");
                /*
                    try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("weekjson",String.valueOf(jsonObject));
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {

                        int tdistance = jsonObject.getInt("totaldistance");
                        int totaltime = jsonObject.getInt("totaltime");
                        int count = jsonObject.getInt("count");
                        String[] weeks = {"mon","tue","wed","thu","fri","sat","sun"};

                        double kmdistance = (tdistance / 1000.0);
                        Log.e("totaldistance",String.valueOf(kmdistance));
                        viewtotalrun.setText(String.format("%.2f",kmdistance));

                        String timeformat = TimeToFormat(totaltime);
                        viewtotlatime.setText(timeformat);

                        viewruncount.setText(String.valueOf(count));

                        for(int i= 0; i<= weeks.length -1;i++){
                            JSONObject jsonObject1 = jsonObject.getJSONObject(weeks[i]);
                            weeks_distance[i] = jsonObject1.getInt("distance");

                            Log.e("weeks_distance",String.valueOf( weeks_distance[i] ));
                        }

                        // 일주일 내의 하루 운동량 최대값 구하기
                        max = 0;
                        for (int i = 0; i < weeks_distance.length-1; i++) {
                            if (weeks_distance[i] > max) {
                                max = weeks_distance[i];

                            }
                        }
                        max = max / 1000;

                        graphInitSetting();       //그래프 기본 세팅

                        BarChartGraph(labelList, jsonList);
                        barChart.setTouchEnabled(false); //확대하지못하게 막아버림! 별로 안좋은 기능인 것 같아~

                    } else {
                    }
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                    */
                Log.e("getweekruninfo","4");
                    }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("getweekruninfo","5");
                        }
                    });
                Log.e("getweekruninfo","6");
                    // 요청 객체에 보낼 데이터를 추가
                    smpr.addStringParam("userID", mid);
                Log.e("getweekruninfo","7");
                    // 서버에 데이터 보내고 응답 요청
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                Log.e("getweekruninfo","8");
                    requestQueue.add(smpr);
                Log.e("getweekruninfo","9");
                /*
                RequestQueue requestQueue = MainAct.getRequestQueue();

                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(Fragment2.this);
                    requestQueue.add(smpr);
                } else {
                    requestQueue.add(smpr);
                }
                 */
                Log.e("getweekruninfo","10");
            }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("onActivityResult","1");
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","2");
        // 활동을 더하고 난 후
        if(requestCode == 105){
            Log.e("onActivityResult","3");
            if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                return;
            }
            Log.e("onActivityResult","4");
            barChart.invalidate();
            barChart.clear();
            barChart.getXAxis().setValueFormatter(null);
            Log.e("onActivityResult","5");
           // getweekruninfo(mid);
           // requestrecentact(mid);
            Log.e("onActivityResult","6");
        }
    }

    public void graphInitSetting(){

        jsonList = new ArrayList<>(); // ArrayList 선언
        labelList = new ArrayList<>(); // ArrayList 선언

        barChart.notifyDataSetChanged();

        labelList.add("월");
        labelList.add("화");
        labelList.add("수");
        labelList.add("목");
        labelList.add("금");
        labelList.add("토");
        labelList.add("일");

        for(int i=0; i <= weeks_distance.length-1; i++){
            if(weeks_distance[i] == 0){
                jsonList.add(0.0f);
            }else{
                Float kmdistance = (weeks_distance[i] / 1000f);
                jsonList.add(kmdistance);
            }
        }


        BarChartGraph(labelList, jsonList);

        barChart.setTouchEnabled(false); //확대하지못하게 막아버림! 별로 안좋은 기능인 것 같아~

        // 차트 왼쪽 Axis 추가/제거
        Log.e("max",max+"");
//        YAxis axisRight = barChart.getAxisRight();
//        if(max < 5.0){
//            //((int)max + 1)
//            axisRight.setAxisMaxValue(2.0f);
//        }else{
//            axisRight.setAxisMaxValue((max/5 + 1) * 5.0f);
//        }


        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setEnabled(false);

//         차트 왼쪽 최소값 추가
//        leftAxis.setAxisMinValue(10);
    }

    /**
     * 그래프함수
     */
    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Float> valList) {
        // BarChart 메소드

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry(valList.get(i),i));
        }
        //valList.get(i)
        BarDataSet depenses = new BarDataSet(entries, "일일 운동량 ( km )"); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.LEFT);
        barChart.setDescription("");

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }

        BarData data = new BarData(labels, depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        data.setValueTextSize(15f);

        ArrayList<BarEntry> barEntry = new ArrayList<>();

        depenses.setColors(Collections.singletonList(Color.rgb(255, 127, 0)));

//new int[] {   Color.rgb(192, 255, 140),
//                Color.rgb(255, 247, 97),
//                Color.rgb(79, 208, 45),
//                Color.rgb(140, 234, 255),
//                Color.rgb(255, 140, 157)}
        barChart.setData(data);
        barChart.animateXY(1000, 1000);
        barChart.invalidate();

        barChart.getLegend().setTextSize(10f);
    }

    public void getcoachinfo(){
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/getcoach.php";

        ViewactMenuActivity viewactMenuActivity = activityWeakReference.get();

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
         smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public  void  onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                Log.e("coachjson",jsonObject.toString());

                if(success) {
                    int num = jsonObject.getInt("num");
                    for(int i= 0; i< num; i++){
                        JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                        JSONObject data = jsonObject1.getJSONObject(i);
                        Coaching coaching =  new Coaching();
                        coaching.setName(data.getString("name"));
                        coaching.setEndtime(data.getInt("time"));
                        coaching.setDescription(data.getString("description"));
                        coaching.setChoachingjson(data.getString("coachjson"));
                        coaching.setReg_date(data.getString("reg_date"));
                        arr_coach.add(coaching);
                        Log.e("coachjson",coaching.getChoachingjson());
                        Log.e("weeks_distance",String.valueOf( weeks_distance[i]));
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

         // 서버에 데이터 보내고 응답 요청
        requestQueue = Volley.newRequestQueue(ViewactMenuActivity.this);
        requestQueue.add(smpr);

        /*RequestQueue requestQueue = MainAct.getRequestQueue();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Viewact","17");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Viewact","18");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Frag2","onstop");
        requestQueue.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        requestQueue = null;

        loginshared = null;
        linearLayoutManager = null;
    }


    public void menuset(){
        menurun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewactMenuActivity.this, RunMenuActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Log.e("destroy1",ViewactMenuActivity.this.isDestroyed()+"");
                finish();
                Log.e("destroy2",ViewactMenuActivity.this.isDestroyed()+"");
            }
        });

        menuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewactMenuActivity.this, Fragment3.class);
                startActivity(intent);
                finish();
            }
        });

        menuviewact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewactMenuActivity.this, ViewactMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menumy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewactMenuActivity.this, ProfileMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
