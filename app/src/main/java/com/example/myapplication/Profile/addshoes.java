package com.example.myapplication.Profile;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addshoes extends AppCompatActivity {

    EditText edit_shoesname;
    Button btn_search;
    ImageView btn_camera;
    File photofile;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.addshoes);
        btn_camera = findViewById(R.id.btn_camera);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final CharSequence[] oItems = {"사진 찍기", "사진 보관함"};

                                AlertDialog.Builder oDialog = new AlertDialog.Builder(addshoes.this,
                                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                oDialog.setTitle("종류를 선택하세요");
                oDialog.setItems(oItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (oItems[which].equals("사진 찍기")) {
                                         int permission = ContextCompat.checkSelfPermission(addshoes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                         int permission2 = ContextCompat.checkSelfPermission(addshoes.this, Manifest.permission.CAMERA);
                                            // 권한이 열려있는지 확인
                                            if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
                                                // 마쉬멜로우 이상버전부터 권한을 물어본다
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                                                    requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
                                                } return;
                                            }else{
                                                Intent captureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                // 임시로 사용할 파일
                                                File tempDir = getCacheDir();
                                                String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
                                                String fileName = "running"+timeStamp;

                                                File imageDir = null;
                                                try {
                                                    imageDir = File.createTempFile(
                                                            fileName,       // 파일 이름
                                                            ".jpg",     // 파일 형식
                                                            tempDir
                                                    );
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                mCurrentPhotoPath = imageDir.getAbsolutePath();
                                                Log.e("photopath",mCurrentPhotoPath);
                                                photofile = imageDir;

                                                if(photofile != null){
                                                        // Uri 가져오기
                                                        Uri photoURI = FileProvider.getUriForFile(addshoes.this,"com.example.myapplication.fileprovider",photofile);
                                                        captureintent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                                        startActivityForResult(captureintent, 101);
                                                }
                                            }


                        } else if (oItems[which].equals("사진 보관함")) {
                                    int permission = ContextCompat.checkSelfPermission(addshoes.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                                    // 권한이 열려있는지 확인
                                    if (permission == PackageManager.PERMISSION_DENIED) {
                                        // 마쉬멜로우 이상버전부터 권한을 물어본다
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                                        }
                                        return;
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                        startActivityForResult(intent, 101);
                                    }
                        }

                    }
                });
                oDialog.setCancelable(false);
                oDialog.show();
            }
        });

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
                    if(requestCode == 101 && resultCode == RESULT_OK) {
                    Uri test = Uri.fromFile(new File(mCurrentPhotoPath));
                    }
            }


    public void searchshoes(String shoesname){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/shoessearch.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            if(success) {


            } else {

            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            Toast.makeText(addshoes.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("shoesname", shoesname);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(addshoes.this);
            requestQueue.add(smpr);
            }
}
