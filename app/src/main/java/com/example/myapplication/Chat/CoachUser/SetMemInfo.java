package com.example.myapplication.Chat.CoachUser;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.Chat.MessageItem;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class SetMemInfo extends AppCompatActivity {

    String roomno;
    Gson gson = new Gson();
    JSONObject quesitonjson;

    EditText editgoal;
    EditText editsdate;
    EditText editweekcount;
    TextView editgroup;
    EditText editmemo;

    Button btn_setsdate;
    Button btn_setgroup;
    Button btn_memset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmeminfo);

        editgoal = findViewById(R.id.editgoal);
        editsdate = findViewById(R.id.editsdate);
        editweekcount = findViewById(R.id.editweekcount);
        editmemo = findViewById(R.id.editmemo);
        editgroup = findViewById(R.id.editgroup);

        btn_setgroup = findViewById(R.id.btn_setgroup);
        btn_setsdate = findViewById(R.id.btn_setsdate);
        btn_memset = findViewById(R.id.btn_memset);

        roomno = getIntent().getStringExtra("chatroomno");
        String sq = getIntent().getStringExtra("question");
        MessageItem question = gson.fromJson(sq,MessageItem.class);
        Log.e("getMessage",question.getMessage());
        Log.e("rn",roomno);
        try {
            quesitonjson = new JSONObject(question.getMessage());
            editgoal.setText(quesitonjson.getString("준비하고 싶은 레이스"));
            editweekcount.setText(quesitonjson.getString("일주일에 원하는 운동 횟수"));
            editsdate.setText(quesitonjson.getString("원하는 코칭 시작일"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
