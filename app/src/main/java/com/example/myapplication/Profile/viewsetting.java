package com.example.myapplication.Profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class viewsetting extends AppCompatActivity {

    RecyclerView my_recyclerView;
    ArrayList<String> arr;
    SettingAdpater adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        //  리사이클러뷰 xml id
                my_recyclerView = findViewById(R.id.rc_set);
                // 라사이클러뷰에 넣기
                // 어댑터 객체 생성
                arr = new ArrayList<>();
                arr.add("내 정보");
                arr.add("로그아웃");

                adapter = new SettingAdpater(arr);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(viewsetting.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                my_recyclerView.setLayoutManager(linearLayoutManager);
                // 어댑터 추가
                my_recyclerView.setAdapter(adapter);
    }



}
