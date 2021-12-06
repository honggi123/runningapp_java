package com.example.myapplication.viewact.Coach;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CoachPlanActivity extends AppCompatActivity {

    ArrayList<Integer> arrayList;
    ArrayList<Plan> arrweek;

    Date startdate;
    RecyclerView my_recyclerView;
    PlanAdapter planAdapter;
    SimpleDateFormat mSimpleDateFormat;

    java.util.Calendar cal;
    java.text.DateFormat format;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.coachplan);

        arrayList = getIntent().getIntegerArrayListExtra("arrselect");

        arrweek = new ArrayList<>();

        for (int i = 1; i <= arrayList.get(1)*4; i++){
            Log.e("주차",arrayList.get(1)+"");
            Log.e("날",arrayList.get(2)+"");
            Plan plan = new Plan();
            plan.setWeek(i);
            arrweek.add(plan);
        }
        dateset();
        //  리사이클러뷰 xml id
        my_recyclerView = findViewById(R.id.rc_plan);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성

        planAdapter = new PlanAdapter(arrweek);
        planAdapter.setDaysize(arrayList.get(2));

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(CoachPlanActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        my_recyclerView.setLayoutManager(linearLayoutManager);
        // 어댑터 추가
        my_recyclerView.setAdapter(planAdapter);
    }

    public void dateset(){
        mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
        String sstartdate = getIntent().getStringExtra("startdate");
        Log.e("sstartdate",sstartdate);
        cal = java.util.Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < arrweek.size(); i++){
            arrweek.get(i).setStartdate(sstartdate);
            try {
                startdate = mSimpleDateFormat.parse(sstartdate);
            } catch (ParseException e){
                e.printStackTrace();
            }
            Log.e("startdate",startdate+"");

            Log.e("startdate",startdate+"");
            cal.setTime(startdate);
            cal.add(cal.DATE, +6); // 7일(일주일)을 더한다
            Log.e("cal.getTime()",cal.getTime()+"");
            String dateStr = format.format(cal.getTime());
            Log.e("+7date",dateStr);

            arrweek.get(i).setEnddate(dateStr);

            sstartdate =  dateStr;
        }


    }

}
