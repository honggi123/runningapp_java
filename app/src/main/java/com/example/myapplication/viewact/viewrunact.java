package com.example.myapplication.viewact;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Challenge.ChallengeInfo;
import com.example.myapplication.ImageAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Run.runActivity;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class viewrunact extends AppCompatActivity {

    RunInfo runInfo;
    TextView viewrating;
    TextView viewmemo;
    TextView viewdistance;
    TextView viewtime;
    TextView viewdate;

    ImageAdapter_viewact adapter;
    RecyclerView recyclerView;

    ArrayList<String> arr_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewrunact);
        runInfo =  (RunInfo) getIntent().getSerializableExtra("runinfo");
        viewrating = findViewById(R.id.viewrating);
        viewmemo = findViewById(R.id.viewmemo);
        viewdistance = findViewById(R.id.viewdistance);
        viewtime = findViewById(R.id.viewtime);
        viewdate = findViewById(R.id.viewdate);
        recyclerView = findViewById(R.id.rc_runinfo);

        viewrating.setText(String.valueOf(runInfo.rating));

        double kmdistance = (runInfo.distance / 1000.00);
        viewdistance.setText(String.format("%.2f",kmdistance)+ " km");


        String timeformat =new runActivity().TimeToFormat(runInfo.time);
        viewtime.setText(timeformat);
        viewmemo.setText(runInfo.memo);
        viewdate.setText(runInfo.reg_date);

        arr_img = new ArrayList<>();
        Log.e("json",runInfo.imgList);
        try {
            JSONArray ja = new JSONArray(runInfo.imgList);
            for(int i = 0; i <ja.length(); i++){
            arr_img.add(ja.getString(i));
                Log.e("json"+i,ja.getString(i));
            }
            adapter = new ImageAdapter_viewact(arr_img,viewrunact.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(viewrunact.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }




}
