package com.example.myapplication.Run;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainAct;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment1 extends Fragment implements OnMapReadyCallback {


    Button btn_runstart;
    MainAct context;
    TextView view_tdate;
    TextView view_tdistance;
    TextView view_ttime;
    Button btn_goalset;
    String tdate;
    TextView view_goalnum;
    ImageView view_goalchk;

    private MapView mapview = null;
    private GoogleMap mMap;

    public Fragment1(Context context) {
        this.context = (MainAct) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("oncreatevioew","oncreate");
        View layout = inflater.inflate(R.layout.fragment1, container, false);
        btn_runstart = layout.findViewById(R.id.btn_runstart);
        view_tdate = layout.findViewById(R.id.todaydate_frag1);
        view_tdistance = layout.findViewById(R.id.viewdistance_frag1);
        view_ttime = layout.findViewById(R.id.viewtime_frag1);
        btn_goalset = layout.findViewById(R.id.btn_goalset_frag1);
        tdate = ((MainAct) getContext()).getdate();
        view_goalnum = layout.findViewById(R.id.view_goalnum);
        view_goalchk = layout.findViewById(R.id.view_goalchk);
        view_tdate.setText(tdate);
        Log.e("oncreatevioew","error2");

        mapview = (MapView) layout.findViewById(R.id.mapview_runmenu);
        mapview.onCreate(savedInstanceState);
        mapview.onResume();
        Log.e("oncreatevioew","error3");
        // 회원 투데이 러닝 정보 가져오기
        getinfo();
        Log.e("oncreatevioew","error4");
        mapview.getMapAsync(this);
        Log.e("oncreatevioew","error5");

        btn_runstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("oncreatevioew","error6");
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // 마쉬멜로우 이상버전부터 권한을 물어본다
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                    }
                    return;
                }
                Log.e("oncreatevioew","error7");
                Intent intent = new Intent(context, Runbeforetimer_Activity.class);
                Log.e("oncreatevioew","error8");
                startActivity(intent);
                Log.e("oncreatevioew","error9");
            }
        });

        btn_goalset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicedt();
            }
        });
        Log.e("oncreatevioew","error10");
        return layout;
    }
    // 목표 설정 시 거리 목표 설정
    public void choicedt(){
        Intent intent = new Intent(context,distancegoalset_Activity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        Log.e("oncreatevioew","onresume");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.e("oncreatevioew","error11");
        super.onStart();
        Log.e("oncreatevioew","error12");
        // 회원 투데이 러닝 정보 가져오기
        getinfo();
        Log.e("oncreatevioew","error13");
    }


    @Override
    public void onStop() {
        Log.e("oncreatevioew","onstop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e("oncreatevioew","ondestory");
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("oncreatevioew","error11");
        mMap = googleMap;
        LatLng SEOUL = new LatLng(37.56, 126.97);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        Log.e("oncreatevioew","error12");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("oncreatevioew","error13");
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("oncreatevioew","error14");
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                Log.e("oncreatevioew","error15");
            }
            return;
        }
        Log.e("oncreatevioew","error16");
        mMap.setMyLocationEnabled(true);
        Log.e("oncreatevioew","error17");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("grantcode", requestCode + "ss");
        switch (requestCode) {
            case 1000:
                Log.e("grantcode", "1000");
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(context, "해당 권한을 활성화 하셔야 러닝 서비스를 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent intent = new Intent(context, Runbeforetimer_Activity.class);
                startActivity(intent);
                break;
            case 1001:
                Log.e("grantcode", "1001");
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(context, "해당 권한을 활성화 하셔야 현 위치를 확인 할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void getinfo(){

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.143.9.214/Getdailyinfo.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int distance = 0;
                    // 활동 받아오기
                    if(jsonObject.getString("info").equals("null")){
                        // 오늘의 활동이 없어서 데이터들 못 받아 온 경우
                        view_ttime.setText("00:00");
                        view_tdistance.setText("0");
                    }else{
                        // 오늘의 활동 데이터 받아 온 경우
                        boolean success = jsonObject.getBoolean("isuccess");
                        if(success) {
                            //  성공
                            int time =jsonObject.getInt("time");
                             distance =  jsonObject.getInt("distance");
                            double kmdistance = (distance / 1000.00);
                            view_ttime.setText(context.TimeToFormat(time));
                            view_tdistance.setText(String.format("%.2f",kmdistance));
                        } else {
                            // 오류로 데이터를 못 받아온 경우
                        }
                        Log.e("json",String.valueOf(jsonObject));
                    }
                // 목표 받아오기
                    if(jsonObject.getString("goalset").equals("null")){
                        // 오늘의 목표가 없어서 데이터들 못 받아 온 경우
                        view_goalnum.setText("목표를 설정해주세요.");
                        view_goalnum.setTextSize(20f);
                    }else{
                        view_goalnum.setTextSize(35f);
                        // 오늘의 목표 데이터 받아 온 경우
                        boolean success = jsonObject.getBoolean("gsuccess");
                        if(success) {
                            //  성공
                             int dgoal = jsonObject.getInt("Dgoal");
                             if(distance>=dgoal){
                                 view_goalchk.setVisibility(View.VISIBLE);
                                 btn_goalset.setEnabled(false);
                             }else{
                                 view_goalchk.setVisibility(View.INVISIBLE);
                             }
                            double Dgoal =  (jsonObject.getInt("Dgoal") / 1000.0);

                            view_goalnum.setText(String.valueOf(Dgoal) +" km");
                            
                        } else {
                            // 오류로 데이터를 못 받아온 경우
                        }
                        Log.e("json",String.valueOf(jsonObject));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("id", context.getMid());
        smpr.addStringParam("date", String.valueOf(tdate));

        // 서버에 데이터 보내고 응답 요청
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(smpr);

        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }
    }



}
