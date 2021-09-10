package com.example.myapplication.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class Myshoes extends AppCompatActivity {

    Button btn_addshoes;

    RecyclerView my_recyclerView;
    ArrayList<Shoe> arr;
    shoes_Adapter my_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.viewmyshoes);

        Log.e("oncreate","myshoes");

        btn_addshoes = findViewById(R.id.btn_addshoes);

        btn_addshoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Myshoes.this,addshoes.class);
                startActivity(intent);
            }
        });


        //  리사이클러뷰 xml id
                my_recyclerView = findViewById(R.id.rc_shoes);
                // 라사이클러뷰에 넣기
                // 어댑터 객체 생성
                arr = new ArrayList<>();
                my_adapter = new shoes_Adapter(arr);

                LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(Myshoes.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                my_recyclerView.setLayoutManager(linearLayoutManager);
                // 어댑터 추가
                my_recyclerView.setAdapter(my_adapter);


        super.onCreate(savedInstanceState);
    }
}
