package com.example.myapplication.Chat.CoachUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.gson.Gson;

public class ViewUserinfo extends AppCompatActivity {

    Userinfo userinfo;
    TextView tid;
    TextView tgoal;
    TextView tsdate;
    TextView tweeknum;
    TextView tgname;
    TextView memo;
    Button btn_mod;
    String userID;
    Gson gson = new Gson();

    Boolean mod = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        userID = getIntent().getStringExtra("userID");
        String suserinfo = getIntent().getStringExtra("userinfo");

        userinfo = gson.fromJson(suserinfo, Userinfo.class);

        tid = findViewById(R.id.tid);
        tgoal = findViewById(R.id.tgoal);
        tsdate = findViewById(R.id.tsdate);
        tweeknum = findViewById(R.id.tweeknum);
        memo = findViewById(R.id.memo);
        btn_mod = findViewById(R.id.btn_mod);
        tgname = findViewById(R.id.tgname);

        tid.setText(userID);


        setdata();

        btn_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUserinfo.this, ModUserInfo.class);
                intent.putExtra("userinfo", suserinfo);
                intent.putExtra("userID", userID);
                startActivityForResult(intent, 300);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 300)
        {
            if (resultCode != RESULT_OK) { // 값이 성공적으로 반환되었을때
                return;
            }
            // 코드 작성
            String suserinfo = data.getStringExtra("userinfo");
            Log.e("suserinfo",suserinfo);
            userinfo = gson.fromJson(suserinfo, Userinfo.class);
            setdata();
            mod = true;
        }
    }


    public void setdata(){
        tgoal.setText(userinfo.goal);
        tsdate.setText(userinfo.sdate);
        tgname.setText(userinfo.getGroup());
        tweeknum.setText(String.valueOf(userinfo.weeknum));
        memo.setText(userinfo.uniq);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mod){
        setResult(200);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("onBackPressed","true");


    }
}
