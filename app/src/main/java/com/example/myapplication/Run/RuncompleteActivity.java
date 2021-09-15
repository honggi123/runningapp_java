package com.example.myapplication.Run;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Challenge.viewchallenge_Activity;
import com.example.myapplication.ImageAdapter;
import com.example.myapplication.MainAct;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RuncompleteActivity extends AppCompatActivity {

    ArrayList<String> arr_photopath;
    ArrayList<String> arr_storagepath;
    ImageView btn_plusimg;
    ImageAdapter adapter;
    TextView distanceView;
    TextView memo;
    Button btn_upload;
    TextView timeView;
    TextView dateView;
    int time;
    int distance;
    ImageView btn_noimgplus;
    SharedPreferences loginshared;
    String mid;
    RatingBar howrunrating;
    TextView kcalview;
    double kcal;
    private TextToSpeech tts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arr_photopath = new ArrayList<String>();
        setContentView(R.layout.runcomplete);
         kcalview = findViewById(R.id.kcalview_complete);

        btn_plusimg = findViewById(R.id.btn_plusimg);
        distanceView = findViewById(R.id.kcalview_complete);
        timeView = findViewById(R.id.timeview_complete);
        dateView = findViewById(R.id.dateView_complete);
        btn_upload = findViewById(R.id.btn_upload_runcomp);
        btn_noimgplus = findViewById(R.id.btn_noimgplus);
        memo = findViewById(R.id.memo_runcomp);
        howrunrating = findViewById(R.id.runratingBar);
        // 러닝 시간, 거리 받아오기
        time = getIntent().getIntExtra("time",0);
        distance = getIntent().getIntExtra("distance",0);
        kcal = getIntent().getDoubleExtra("kcal",0.0);

        // 로그인 한 유저의 아이디 가졍오기
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        String timeformat =new runActivity().TimeToFormat(time);
        timeView.setText(timeformat);

        double kmdistance = (distance / 1000.0);
        distanceView.setText(String.format("%.2f",kmdistance));

        dateView.setText(new MainAct().getdate());
        kcalview.setText(String.format("%.2f",kcal));

        arr_photopath = getIntent().getStringArrayListExtra("arr_photopath");
        arr_storagepath = getIntent().getStringArrayListExtra("arr_storageimg");
        display_imgview();
        speech("러닝을 종료 합니다. 달린 거리는 "+distance+"미터 이며"+time+"초 입니다.");

        adapter = new ImageAdapter(arr_photopath,arr_storagepath, RuncompleteActivity.this);
        RecyclerView recyclerView = findViewById(R.id.rcimg);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        btn_noimgplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 101);
            }
        });

        btn_plusimg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                int permission = ContextCompat.checkSelfPermission(RuncompleteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                // 권한이 열려있는지 확인
                if (permission == PackageManager.PERMISSION_DENIED) {
                    // 마쉬멜로우 이상버전부터 권한을 물어본다
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                    }
                    return;
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 101);
                }
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadPost();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        if(requestCode == 1000) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, 101);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                Log.e("fileurl",String.valueOf(fileUri));
                try {
                    adapter.additem(fileUri,RuncompleteActivity.this);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
                display_imgview();
            }
        }
    }


    // 포스트를 서버에 업로드하는 메소드
    private void uploadPost() throws IOException {
        // urlList를 json 배열로 변환
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<arr_photopath.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", arr_photopath.get(i));
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.143.9.214/insert_post.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",String.valueOf(jsonObject));
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        // 업로드 성공
                        Toast.makeText(RuncompleteActivity.this, "정보가 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 업로드 실패
                        Toast.makeText(RuncompleteActivity.this, "정보 등록 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RuncompleteActivity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("json",jsonArray.toString());

        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("id", mid);
        smpr.addStringParam("rating", String.valueOf(howrunrating.getRating()));
        smpr.addStringParam("memo", memo.getText().toString());
        smpr.addStringParam("distance", String.valueOf(distance));
        smpr.addStringParam("time", String.valueOf(time));
        smpr.addStringParam("kcal", String.valueOf(kcal));

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getdate = simpleDate.format(mDate);
        smpr.addStringParam("run_date", getdate);

        smpr.addStringParam("url", jsonArray.toString()); // json 배열을 문자열로 변환
        smpr.addStringParam("cntImage", String.valueOf(arr_photopath.size())); // 첨부된 사진 개수

        //이미지 파일 추가 (pathList는 첨부된 사진의 내부 uri string 리스트)
        for(int i=0; i<arr_photopath.size(); i++) {
            // uri 절대 경로 구하기
            String[] proj= {MediaStore.Images.Media.DATA};
            CursorLoader loader= new CursorLoader(RuncompleteActivity.this, Uri.parse(arr_photopath.get(i)), proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            if(cursor != null){
                int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String abUri= cursor.getString(column_index);
                Log.e("aburi",abUri);
                cursor.close();
                // 이미지 파일 첨부
                smpr.addFile("image"+i, abUri);
            }else{
            }
        }
        // 서버에 데이터 보내고 응답 요청
//        RequestQueue requestQueue = Volley.newRequestQueue(RuncompleteActivity.this);
//        requestQueue.add(smpr);

        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(RuncompleteActivity.this);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }

    }
    public void display_imgview(){
        if(arr_photopath.size() == 0){
            btn_noimgplus.setVisibility(View.VISIBLE);
            btn_plusimg.setVisibility(View.INVISIBLE);
        }else{
            btn_noimgplus.setVisibility(View.INVISIBLE);
            btn_plusimg.setVisibility(View.VISIBLE);
        }
    }
    public void speech(String txt){
        tts = new TextToSpeech(RuncompleteActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
                tts.speak(txt,TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
    }

}
