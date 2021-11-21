package com.example.myapplication.Profile;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addshoes extends AppCompatActivity {

    EditText edit_shoesname;
    Button btn_reg;
    ImageView btn_camera;
    File photofile;
    String mCurrentPhotoPath;
    ImageView viewshoe;
    SharedPreferences loginshared;
    String mid;
    Uri imgurl;
    ImageView btn_setdistance;
    TextView edit_shoedistacne;
    String result;
    int gdis;
    Boolean setd;
    Boolean setn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.addshoes);
        btn_camera = findViewById(R.id.btn_camera);
        viewshoe = findViewById(R.id.viewshoe);
        btn_reg = findViewById(R.id.btn_reg);
        edit_shoesname = findViewById(R.id.edit_shoename);
        btn_setdistance = findViewById(R.id.btn_setshoedistance);
        edit_shoedistacne = findViewById(R.id.edit_shoedistacne);


        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);
        result = null;

        btn_setdistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new customdialog().calldialog();
                if(result != null) {
                    edit_shoedistacne.setText(result + "km");
                }

            }
        });



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
                                        startActivityForResult(intent, 102);
                                    }
                        }
                    }
                });
                oDialog.setCancelable(false);
                oDialog.show();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edit_shoesname.getText().toString().equals("") && !edit_shoedistacne.getText().toString().equals("")){
                    addshoes(edit_shoesname.getText().toString());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(addshoes.this);
                    builder.setTitle("이름 또는 목표거리를 설정해주세요.")        // 제목 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                public void onClick(DialogInterface dialog, int whichButton){
                                    //원하는 클릭 이벤트를 넣으시면 됩니다.
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기
                }
            }
        });


        super.onCreate(savedInstanceState);
    }


     public class customdialog {
            Dialog dig;
            Button btn_setdistance;
            EditText picker1_time;
            EditText picker2_time;


            public void calldialog() {
                dig = new Dialog(addshoes.this);
                // 액티비티의 타이틀바를 숨긴다.
                dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // 커스텀 다이얼로그의 레이아웃을 설정한다.
                dig.setContentView(R.layout.pickdistance_dialog);
                picker1_time = dig.findViewById(R.id.picker1_time);
                picker2_time = dig.findViewById(R.id.picker2_time);
                btn_setdistance = dig.findViewById(R.id.btn_setdistance);

                btn_setdistance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dig.dismiss();
                        result = picker1_time.getText().toString()+"."+picker2_time.getText().toString();
                        edit_shoedistacne.setText(result+" km");
                        gdis = (int) (Float.parseFloat(result) * 1000);
                    }
                });

                dig.show();
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
                    if(requestCode == 101 && resultCode == RESULT_OK) {
                    Uri test = Uri.fromFile(new File(mCurrentPhotoPath));
                        viewshoe.setVisibility(View.VISIBLE);
                        btn_camera.setVisibility(View.INVISIBLE);
                        Glide.with(addshoes.this)
                                .load(test)
                                .into(viewshoe);
                             saveFile(test);
                        Log.e("imgurl",imgurl+"");
                    }

               if (requestCode == 102) {
                   if (resultCode == RESULT_OK) {
                       Uri fileUri = data.getData();
                       Log.e("fileurl",String.valueOf(fileUri));
                       try {
                           btn_camera.setVisibility(View.INVISIBLE);
                           viewshoe.setVisibility(View.VISIBLE);
                                   Glide.with(addshoes.this)
                                   .load(fileUri)
                                   .into(viewshoe);

//                       adapter.additem(fileUri,addshoes.this);
                       } catch (Exception e) {
                       Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                       }
                       imgurl = fileUri;
                   }

               }
    }


    // 파일 저장
    private void saveFile(Uri image_uri) {
        String fileName;
            ContentValues values = new ContentValues();
            fileName =  "Run"+System.currentTimeMillis()+".png";
            values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
            }

            ContentResolver contentResolver = getContentResolver();
            Uri item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        imgurl = item;
            try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
            if (pdf == null) {
            Log.d("Run", "null");
            } else {
            byte[] inputData = getBytes(image_uri);
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                fos.write(inputData);
                fos.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                contentResolver.update(item, values, null, null);
            }
                // 갱신
                galleryAddPic(fileName);
            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("Run", "FileNotFoundException  : "+e.getLocalizedMessage());
            } catch (Exception e) {
                Log.d("Run", "FileOutputStream = : " + e.getMessage());
            }
            }

    private void galleryAddPic(String Image_Path) {

                String SaveFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Image_Path;
                Log.e("galleryaddpic_Image_Path",Image_Path);

                File file = new File(Image_Path);
                MediaScannerConnection.scanFile(addshoes.this,
                new String[]{file.toString()},
                null, null);

                Log.e("save",file.toString());
            }


    public byte[] getBytes(Uri image_uri) throws IOException {
        InputStream iStream = getContentResolver().openInputStream(image_uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024; // 버퍼 크기
        byte[] buffer = new byte[bufferSize]; // 버퍼 배열

        int len = 0;
        // InputStream에서 읽어올 게 없을 때까지 바이트 배열에 쓴다.
        while ((len = iStream.read(buffer)) != -1)
            byteBuffer.write(buffer, 0, len);
        return byteBuffer.toByteArray();
    }


    public void addshoes(String shoesname){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/addshoes.php";
            ProgressDialog progressDialog;
                    progressDialog = ProgressDialog.show(addshoes.this,
                    "기다려주세요..", null, true, true);
                    progressDialog.show();

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            Log.e("json",jsonObject+"");
                boolean success = jsonObject.getBoolean("success");
                if(success) {
                    Toast.makeText(addshoes.this, "등록 되었습니다.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intentR = new Intent();
                            setResult(RESULT_OK,intentR); //결과를 저장
                    finish();
                } else {
                    Toast.makeText(addshoes.this, "등록이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
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

            //이미지 파일 추가 (pathList는 첨부된 사진의 내부 uri string 리스트)
                    // uri 절대 경로 구하기
        if(imgurl!= null){
            String[] proj= {MediaStore.Images.Media.DATA};
            CursorLoader loader= new CursorLoader(addshoes.this, imgurl, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            if(cursor != null){
                int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String abUri= cursor.getString(column_index);
                Log.e("aburi",abUri);
                cursor.close();

                // 이미지 파일 첨부
                smpr.addFile("image", abUri);
            }else{
            }
        }

            Log.e("shoename",shoesname+"shoe");
            // 서버에 데이터 보내고
            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("id",mid);
            smpr.addStringParam("gdistance", String.valueOf(gdis));
            smpr.addStringParam("shoesname", shoesname);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(addshoes.this);
            requestQueue.add(smpr);


    }
}
