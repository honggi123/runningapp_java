package com.example.myapplication.viewact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Challenge.ChallengeInfo;
import com.example.myapplication.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class viewrecentactActivity extends AppCompatActivity {

    dayviewact_Adapter adapter;
    RecyclerView recyclerView;

    ArrayList<RunInfo> arr_runinfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewrecentmyact);

        arr_runinfo = (ArrayList<RunInfo> ) getIntent  ().getSerializableExtra("arr_runinfo");
        adapter = new dayviewact_Adapter(arr_runinfo,arr_runinfo.size());

        recyclerView = findViewById(R.id.rc_allrecentact);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(viewrecentactActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
