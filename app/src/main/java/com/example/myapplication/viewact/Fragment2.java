package com.example.myapplication.viewact;






import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.EventLogTags;
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

import com.example.myapplication.Challenge.ChallengeInfo;
import com.example.myapplication.Challenge.Challengemake;
import com.example.myapplication.R;
import com.example.myapplication.Run.runActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment2 extends Fragment {

    ArrayList<RunInfo> arr_runinfo;

    dayviewact_Adapter adapter;
    RecyclerView recyclerView;
    Button btn_moreact;
    TextView viewtotalrun;
    TextView viewtotlatime;

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

    // 요일 별 목표 이미지 뷰
    ImageView mongoal;
    ImageView tuegoal;
    ImageView wedgoal;
    ImageView thugoal;
    ImageView frigoal;
    ImageView satgoal;
    ImageView sungoal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,container,false);

        recyclerView = view.findViewById(R.id.rc_recentact_fr2);
        btn_moreact = view.findViewById(R.id.btn_viewrecentact_fr2);
        viewtotlatime = view.findViewById(R.id.view_totaltime);
        viewruncount = view.findViewById(R.id.view_runcount);
        btn_addruninfo = view.findViewById(R.id.addruninfo);

        mongoal = view.findViewById(R.id.mongoal);
        tuegoal = view.findViewById(R.id.tuegoal);
        wedgoal = view.findViewById(R.id.wedgoal);
        thugoal = view.findViewById(R.id.thugoal);
        frigoal = view.findViewById(R.id.frigoal);
        satgoal = view.findViewById(R.id.satgoal);
        sungoal = view.findViewById(R.id.sungoal);

        loginshared = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);
        viewtotalrun = view.findViewById(R.id.viewtotalrunweek);
        barChart = (BarChart) view.findViewById(R.id.fragment_bluetooth_chat_barchart);

        getweekruninfo(mid);
        request(mid);

        getgoalinfo(mid);

        btn_addruninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addruninfo.class);
                startActivityForResult(intent,105);
            }
        });


        return view;
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

                    chggoalview(mon,mongoal);
                    chggoalview(tue,tuegoal);
                    chggoalview(wed,wedgoal);
                    chggoalview(thu,thugoal);
                    chggoalview(fri,frigoal);
                    chggoalview(sat,satgoal);
                    chggoalview(sun,sungoal);

            } else {
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("id", mid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(smpr);
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



    public void request(String mid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/getrecentact.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
                Log.e("json",String.valueOf(jsonObject));
            boolean success = jsonObject.getBoolean("success");
            int num = jsonObject.getInt("num");

            if(success) {
                arr_runinfo = new ArrayList<RunInfo>();
                JSONArray data = jsonObject.getJSONArray("data");
                for(int i= 0; i < num; i++){
                    RunInfo runInfo = new RunInfo();
                    JSONObject dataJSONObject = data.getJSONObject(i);
                    runInfo.setMemo(dataJSONObject.getString("memo"));
                    Log.e("dis",dataJSONObject.getString("distance"));
                    runInfo.setDistance(Integer.parseInt(dataJSONObject.getString("distance")));
                    runInfo.setRating(Float.parseFloat(dataJSONObject.getString("rating")));
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
                            Intent intent = new Intent(getContext(), viewrecentactActivity.class);
                            intent.putExtra("arr_runinfo",arr_runinfo);
                            startActivity(intent);
                        }
                    });
                    btn_moreact.setVisibility(View.VISIBLE);
                }else{
                    // 라사이클러뷰에 넣기
                    adapter = new dayviewact_Adapter(arr_runinfo,num);
                }

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);

            } else {
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("userID", mid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(smpr);
            }




            public void getweekruninfo(String mid){
                    // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                    String serverUrl="http://3.143.9.214/getweekruninfo.php";

                    // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                    SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                        String timeformat =new runActivity().TimeToFormat(totaltime);
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
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    });

                    // 요청 객체에 보낼 데이터를 추가
                    smpr.addStringParam("userID", mid);

                    // 서버에 데이터 보내고 응답 요청
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(smpr);
                    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 활동을 더하고 난 후
        if(requestCode == 105){
            if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                return;
            }
            barChart.invalidate();
            barChart.clear();
            barChart.getXAxis().setValueFormatter(null);

            getweekruninfo(mid);
            request(mid);
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





}
