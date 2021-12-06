package com.example.myapplication.viewact.Coach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoachActivity extends AppCompatActivity {

    RecyclerView selectrecyclerView;
    ArrayList<Coachquestion> arr;
    CoachselAdpater my_adapter;
    Coaching coaching;
    String question;
    int selnum = 1;
    JSONObject jsonObject = null;
    TextView choicename;
    Button next;
    ArrayList<String> arr_select;
    TextView nowqnum;
    TextView totalqnum;
    int totalqcount;
    String coachname;
    ArrayList<String> arr_q;

    String[] qnum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coachselect);

        arr_select = new ArrayList<>();
        arr_q = new ArrayList<>();

        next = findViewById(R.id.btn_runnumnext);
        choicename =findViewById(R.id.choicename);
        nowqnum = findViewById(R.id.nowqnum);
        totalqnum = findViewById(R.id.totalqnum);

        coaching = (Coaching) getIntent().getSerializableExtra("coach");
        coachname = getIntent().getStringExtra("coachname");

        // 질문지 json을 받아온다.
        question = coaching.getQuestion();
        try {
            jsonObject = new JSONObject(question);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 전체 몇개 질문인지 초기화
        try {
            totalqcount = jsonObject.getInt("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        totalqnum.setText(String.valueOf(totalqcount+2));

        Log.e("question",question);
        
        
        // 질문 다음 버튼
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selnum++;
                try {
                    arr_select.add(arr.get(my_adapter.getSelectnum()).qustion);
                    setqustion(selnum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        // 질문 리사이크러뷰
        //  리사이클러뷰 xml id
        selectrecyclerView = findViewById(R.id.rc_coachsel);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arr = new ArrayList<>();
        my_adapter = new CoachselAdpater(arr);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CoachActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        selectrecyclerView.setLayoutManager(linearLayoutManager);
        // 어댑터 추가
        selectrecyclerView.setAdapter(my_adapter);
        try {
            setqustion(selnum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setqustion(int wherenum) throws JSONException {

        nowqnum.setText(String.valueOf(wherenum));
        
        if(wherenum > totalqcount){
            Intent intent = new Intent(CoachActivity.this,GetInfoActivity.class);
            intent.putExtra("coachname",coachname);
            intent.putExtra("arrquestion",arr_q);
            intent.putExtra("arrselect",arr_select);
            startActivity(intent);
            finish();
        }else{
            JSONObject jsonObject1 = jsonObject.getJSONObject("question"+wherenum);
            String name = jsonObject1.getString("name");
            Log.e("name",name);
            String unit = jsonObject1.getString("unit");
            JSONArray question = jsonObject1.getJSONArray("question");
            arr.clear();
            Log.e("question",question.toString());
            for (int i = 0; i < question.length(); i++){
                Coachquestion coachquestion = new Coachquestion();
                coachquestion.setUnit(unit);
                coachquestion.setQustion(question.getJSONObject(i).getString(String.valueOf(i+1)));
                arr.add(coachquestion);
            }
            my_adapter.notifyDataSetChanged();
            arr_q.add(jsonObject1.getString("subject"));
            choicename.setText(name);
        }


    }
}
