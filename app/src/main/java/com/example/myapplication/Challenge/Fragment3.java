package com.example.myapplication.Challenge;


import static android.app.Activity.RESULT_OK;

import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.media.Image;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ImageAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Run.RuncompleteActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class Fragment3 extends Fragment {


    Button btn_challengmake;

    Context context;
    ArrayList<ChallengeInfo> arr_ch;
    ChallengelistAdapter adapter;
    RecyclerView recyclerView;
    TextView btn_viewexch;

    ArrayList<ChallengeInfo> arr_chfamous;
    ChallengelistAdapter adapterfam;
    RecyclerView rcfamous;

    ArrayList<ChallengeInfo> arr_mych;
    ChallengelistAdapter adaptermy;
    RecyclerView rcmy;
    SharedPreferences loginshared;

    String mid;
    public Fragment3(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3,container,false);
        btn_challengmake = view.findViewById(R.id.btn_challengmake);

        recyclerView = view.findViewById(R.id.rc_challenge);

        rcfamous = view.findViewById(R.id.rc_famchallenge);

        rcmy = view.findViewById(R.id.rc_mych);
        btn_viewexch = view.findViewById(R.id.btn_viewexch);

        loginshared = context.getSharedPreferences("Login", context.MODE_PRIVATE);

        // 로그인 정보
        mid = loginshared.getString("id", null);

        super.onCreate(savedInstanceState);





        btn_challengmake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Challengemake.class);
                startActivityForResult(intent,102);
            }
        });

        btn_viewexch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,viewexchallenge.class);
                startActivity(intent);
            }
        });

        // 데이터 베이스 가져오기
        getmychinfo(mid);
        getrecentcchinfo();
        getfamchinfo();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    public void getmychinfo(String id){     //  내 챌린지

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.143.9.214/getmych.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("mycjson",String.valueOf(jsonObject));
                    int c_num = jsonObject.getInt("num");
                    int joinc_num = jsonObject.getInt("joinc_num");
                    if(c_num+joinc_num > 0) {
                        arr_mych = new ArrayList<ChallengeInfo>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for(int i= 0; i < c_num+joinc_num; i++){
                            Log.e("mychdata",String.valueOf(data.get(i)));
                            ChallengeInfo challengeInfo = new ChallengeInfo();
                            JSONObject dataJSONObject = data.getJSONObject(i);
                            challengeInfo.setCno(Integer.parseInt(dataJSONObject.getString("cno")));
                            challengeInfo.setId(dataJSONObject.getString("id"));
                            challengeInfo.setName(dataJSONObject.getString("name"));
                            challengeInfo.setG_distance(Integer.parseInt(dataJSONObject.getString("g_distance")));
                            challengeInfo.setNum_member(Integer.parseInt(dataJSONObject.getString("num_member")));
                            challengeInfo.setS_date(dataJSONObject.getString("s_date"));
                            challengeInfo.setN_distance(Integer.parseInt(dataJSONObject.getString("n_distance")));
                            challengeInfo.setG_date(dataJSONObject.getString("e_date"));
                            challengeInfo.setReg_date(dataJSONObject.getString("reg_date"));
                            arr_mych.add(challengeInfo);
                        }
                        Log.e("size",String.valueOf(arr_mych.size()));

                       Comparator<ChallengeInfo> compare = new Comparator<ChallengeInfo>() {
                           @Override public int compare(ChallengeInfo lhs, ChallengeInfo rhs) {
                               return (rhs.getReg_date()).compareTo(lhs.getReg_date());
                           }
                           };

                        //정렬 실행
                        Collections.sort(arr_mych, compare);

                        adaptermy = new ChallengelistAdapter(arr_mych,2);
                        adaptermy.setfrg(Fragment3.this);
                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        rcmy.setLayoutManager(linearLayoutManager);
                        rcmy.setAdapter(adaptermy);
                    }else {
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

        smpr.addStringParam("userID",id);

        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(smpr);

    }

    public void getrecentcchinfo(){

                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.143.9.214/getchallengeinfo.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                    int c_num = jsonObject.getInt("num");
                    if(c_num > 0) {
                        arr_ch = new ArrayList<ChallengeInfo>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for(int i= 0; i < c_num; i++){
                            Log.e("rdata",String.valueOf(data.get(i)));
                            ChallengeInfo challengeInfo = new ChallengeInfo();
                            JSONObject dataJSONObject = data.getJSONObject(i);
                            challengeInfo.setCno(Integer.parseInt(dataJSONObject.getString("cno")));
                            challengeInfo.setId(dataJSONObject.getString("id"));
                            challengeInfo.setName(dataJSONObject.getString("name"));
                            challengeInfo.setG_distance(Integer.parseInt(dataJSONObject.getString("g_distance")));
                            challengeInfo.setNum_member(Integer.parseInt(dataJSONObject.getString("num_member")));
                            challengeInfo.setS_date(dataJSONObject.getString("s_date"));
                            challengeInfo.setG_date(dataJSONObject.getString("e_date"));
                            challengeInfo.setN_distance(Integer.parseInt(dataJSONObject.getString("n_distance")));
                            arr_ch.add(challengeInfo);
                        }
                        Log.e("rsize",String.valueOf(arr_ch.size()));
                        Log.e("arr",arr_ch.get(0).name);
                        adapter = new ChallengelistAdapter(arr_ch,2);
                        adapter.setfrg(Fragment3.this);
                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }else {
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
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(smpr);

    }


    public void getfamchinfo(){     // 인기 순

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.143.9.214/getfamchinfo.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("famjson",String.valueOf(jsonObject));
                    int c_num = jsonObject.getInt("num");
                    if(c_num > 0) {

                        arr_chfamous = new ArrayList<ChallengeInfo>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for(int i= 0; i < c_num; i++){
                            Log.e("data",String.valueOf(data.get(i)));
                            ChallengeInfo challengeInfo = new ChallengeInfo();
                            JSONObject dataJSONObject = data.getJSONObject(i);
                            challengeInfo.setCno(Integer.parseInt(dataJSONObject.getString("cno")));
                            challengeInfo.setId(dataJSONObject.getString("id"));
                            challengeInfo.setName(dataJSONObject.getString("name"));
                            challengeInfo.setG_distance(Integer.parseInt(dataJSONObject.getString("g_distance")));
                            challengeInfo.setNum_member(Integer.parseInt(dataJSONObject.getString("num_member")));
                            challengeInfo.setS_date(dataJSONObject.getString("s_date"));

                            challengeInfo.setG_date(dataJSONObject.getString("e_date"));
                            challengeInfo.setN_distance(Integer.parseInt(dataJSONObject.getString("n_distance")));
                            arr_chfamous.add(challengeInfo);
                        }
                        Log.e("size",String.valueOf(arr_chfamous.size()));

                        Comparator<ChallengeInfo> compare = new Comparator<ChallengeInfo>() {
                            @Override public int compare(ChallengeInfo lhs, ChallengeInfo rhs) {
                                if(rhs.getNum_member() > lhs.getNum_member()){
                                    return 1;
                                }else{
                                    return -1;
                                }
                            }
                        };
                        //정렬 실행
                        Collections.sort(arr_chfamous, compare);


                        adapterfam = new ChallengelistAdapter(arr_chfamous,2);
                        adapterfam.setfrg(Fragment3.this);
                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        rcfamous.setLayoutManager(linearLayoutManager);
                        rcfamous.setAdapter(adapterfam);
                    }else {
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(smpr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // view challenge로 부터 종료 메시지를 받았을때
        if(requestCode == 101){
            if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                return;
            }
            Boolean modify = data.getBooleanExtra("modify",false); //  수정 여부 확인
            Boolean remove = data.getBooleanExtra("remove",false); //  수정 여부 확인
            Boolean join = data.getBooleanExtra("join",false); //  수정 여부 확인

            if(modify){
                ChallengeInfo challengeInfo = (ChallengeInfo) data.getSerializableExtra("chinfo");
                modifych(challengeInfo.cno,challengeInfo.name);
            }

            if(remove){
                ChallengeInfo challengeInfo = (ChallengeInfo) data.getSerializableExtra("chinfo");
                removech(challengeInfo.getCno());
            }
            if(join){
                // 데이터 베이스 가져오기
                getmychinfo(mid);
                getrecentcchinfo();
                getfamchinfo();
            }
        }

        // chmake로 부터 종료 메시지를 받았을때
        if(requestCode == 102){
            if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                return;
            }
            // 데이터 베이스 가져오기
            getmychinfo(mid);
            getrecentcchinfo();
            getfamchinfo();
        }


    }

    public void modifych(int cno,String name){
        for(int i =0; i < arr_mych.size(); i++){
            if( cno == arr_mych.get(i).cno ){
                adaptermy.modify(i,name);
                Log.e("admy",i+"");
                Log.e("naem",name);
            }
        }
        for(int i =0; i < arr_ch.size(); i++){
            if( cno == arr_ch.get(i).cno ){
                adapter.modify(i,name);
                Log.e("ad",i+"");
                Log.e("naem",name);
            }
        }
        for(int i =0; i < arr_chfamous.size(); i++){
            if( cno == arr_chfamous.get(i).cno ){
                adapterfam.modify(i,name);
                Log.e("adfam",i+"");
                Log.e("naem",name);
            }
        }
    }

    public void removech(int cno){
        for(int i =0; i < arr_mych.size(); i++){
            if( cno == arr_mych.get(i).cno ){
                adaptermy.remove(i);

            }
        }
        for(int i =0; i < arr_ch.size(); i++){
            if( cno == arr_ch.get(i).cno ){
                adapter.remove(i);

            }
        }
        for(int i =0; i < arr_chfamous.size(); i++){
            if( cno == arr_chfamous.get(i).cno ){
                adapterfam.remove(i);

            }
        }
    }




}
