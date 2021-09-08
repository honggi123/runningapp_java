package com.example.myapplication.Challenge;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Challengedatemk_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    SharedPreferences Chpref;
    Button comp;
    String startdate;
    String enddate;
    TextView view_startdate;
    TextView view_enddate;
    Button btn_startdate;
    Button btn_enddate;
    Date s_date;
    Date e_date;
    SimpleDateFormat format;

    int start_or_end = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengedatesel);

        view_startdate = findViewById(R.id.view_startdate_datesel);
        view_enddate = findViewById(R.id.viewenddate_datesel);
        comp = findViewById(R.id.btn_comp_chdatesel);
        btn_startdate = findViewById(R.id.btn_startdate_datesel);
        btn_enddate = findViewById(R.id.btn_enddate_datesel);
        Chpref= getSharedPreferences("ch",MODE_PRIVATE);
        format = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences.Editor edit = Chpref.edit();

        btn_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                start_or_end = 1;
                new DatePickerDialog(Challengedatemk_Activity.this,Challengedatemk_Activity.this,year,month,day).show();

            }
        });

        btn_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                start_or_end = 2;
                new DatePickerDialog(Challengedatemk_Activity.this,Challengedatemk_Activity.this,year,month,day).show();
            }
        });

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(s_date == null || e_date == null){
                   AlertDialog.Builder builder = new AlertDialog.Builder(Challengedatemk_Activity.this);
                                       builder.setTitle("시작날짜와 종료날짜를 확인해주세요.")        // 제목 설정
                                               .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                               .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                                   // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                                   public void onClick(DialogInterface dialog, int whichButton){
                                                     //원하는 클릭 이벤트를 넣으시면 됩니다.
                                                       dialog.dismiss();
                                                   }
                                               });

                                       AlertDialog dialog = builder.create();    // 알림창 객체 생성
                                       dialog.show();    // 알림창 띄우기
               }else if(s_date.compareTo(e_date) > 0){
                   // 시작 날짜를 현재 날짜보다 그 전에 설정 했을 경우
                   AlertDialog.Builder builder = new AlertDialog.Builder(Challengedatemk_Activity.this);
                   builder.setTitle("시작날짜는 종료날짜보다 빨라야합니다.")        // 제목 설정
                           .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                           .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                               // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                               public void onClick(DialogInterface dialog, int whichButton){
                                   //원하는 클릭 이벤트를 넣으시면 됩니다.
                                   dialog.dismiss();
                               }
                           });

                   AlertDialog dialog = builder.create();    // 알림창 객체 생성
                   dialog.show();    // 알림창 띄우기
               }else{
                   // 시작날짜를 현재 날짜랑 같거나 느리게 설정 했을 경우
                   edit.putString("startdate",format.format(s_date));
                   edit.putString("enddate",format.format(e_date));
                   edit.commit();
                   finish();
               }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        try {
            month += 1;
        if(start_or_end == 1){
            s_date = format.parse(year +"-"+month+"-"+day);
            if(compare_sdate()){
                    view_startdate.setText(format.format(s_date));
                    btn_enddate.setVisibility(View.VISIBLE);
            }
        }else if(start_or_end == 2){
            e_date = format.parse(year +"-"+month+"-"+day);
            if(compare_edate()){
                view_enddate.setText(format.format(e_date));
            }
        }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        start_or_end = 0;
    }

    public Boolean compare_sdate() throws ParseException {

        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );

        Date currentTime = new Date();

        String oTime = mSimpleDateFormat.format ( currentTime ); //현재시간 (String)
        Date currentDate =  mSimpleDateFormat.parse( oTime );

        Log.e("date",s_date.compareTo(currentDate)+"");
        if(s_date.compareTo(currentDate) < 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(Challengedatemk_Activity.this);
            builder.setTitle("현재 날짜보다 늦게 설정해주십시오. ")        // 제목 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton){
                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                            s_date = null;
                        }
                    });
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
            return false;
        }else{
            return true;
        }
    }

    public Boolean compare_edate(){
        if(s_date != null){
            if(e_date.compareTo(s_date) < 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(Challengedatemk_Activity.this);
                builder.setTitle("종료 날짜를 시작 날짜보다 늦게 설정해주십시오. ")        // 제목 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public void onClick(DialogInterface dialog, int whichButton){
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }



}
