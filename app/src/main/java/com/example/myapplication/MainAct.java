package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.android.volley.RequestQueue;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Challenge.Fragment3;
import com.example.myapplication.Loign.LoginActivity;
import com.example.myapplication.Profile.Fragment4;
import com.example.myapplication.Run.Fragment1;
import com.example.myapplication.Run.Runbeforetimer_Activity;
import com.example.myapplication.viewact.Fragment2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.gun0912.tedpermission.PermissionListener;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainAct extends AppCompatActivity {

    static RequestQueue requestQueue;
    public static final String TAG = "MyTag";
    Button btn_logout;
    String mid;
    String logintype;
    BottomNavigationView bottomNavigationView;
    static final int MY_PERMISSION_STORAGE = 101;
    PermissionListener permissionlistener;
    SharedPreferences loginshared;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);

        requestQueue = Volley.newRequestQueue(MainAct.this);
        Log.e("Mainact","error1");


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // 로그인 정보
        logintype = loginshared.getString("logintype", null);
        mid = loginshared.getString("id", null);

        Log.e("Mainact","error2");
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainAct.this, "권한 허가", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainAct.this, Runbeforetimer_Activity.class);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainAct.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        Log.e("Mainact","error3");
    //처음화면
    getSupportFragmentManager().beginTransaction().add(R.id.mainframe, new Fragment1(MainAct.this)).commit(); //FrameLayout에 fragment.xml 띄우기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new Fragment1(MainAct.this)).commit();
                        break;
                    case R.id.fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new Fragment2()).commit();
                        break;
                    case R.id.fragment3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new Fragment3(MainAct.this)).commit();
                        break;
                    case R.id.fragment4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new Fragment4(MainAct.this)).commit();
                        break;
                }
                return true;
            }
        });
        Log.e("Mainact","error4");
    }

    public void logout(){
        SharedPreferences.Editor edit = loginshared.edit();
        edit.clear();
        edit.commit();
        // 카카오로 로긴한 유저 로그아웃
        if(logintype.equals("kakao")){
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                }
            });
        }
        Intent intent = new Intent(MainAct.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public String getMid() {
        return mid;

    }

    public void cancelreq(){
        requestQueue.cancelAll(TAG);
    }

    public void checkPermission(int resquestcode){
        if (ActivityCompat.checkSelfPermission(MainAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, resquestcode);
            }
            return;
        }
    }

    public static String getdate(){
        long now = System.currentTimeMillis();

        Date date1 = new Date(now);

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDate.format(date1);
        return date;
    }

    public static String TimeToFormat(int time){
        String totaltime = null;
        int sec = time % 60;
        if (time > 3600) {
            int bun = time / 60 % 60;
            int hour = time / 3600;
            totaltime = String.format("%02d", hour) + ":" + String.format("%02d", bun) + ":" + String.format("%02d", sec);
        } else {
            int bun = time / 60 % 60;
            totaltime = String.format("%02d", bun) + ":" + String.format("%02d", sec);
        }
        return totaltime;
    }

    public String timetodate(String olddate){
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        String newdate = null;
        try {
            date = simpleDate.parse(olddate);
            newdate =newformat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
