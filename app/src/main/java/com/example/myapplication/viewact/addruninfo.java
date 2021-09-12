package com.example.myapplication.viewact;
// implements DatePickerDialog.OnDateSetListener




import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.myapplication.ImageAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class addruninfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final int GET_GALLERY_IMAGE =200;

    ImageView addmultipleimg;
    ImageView addimg;
    RecyclerView my_recyclerView;
    ImageAdapter imageAdapter;
    ArrayList<String> arr_imgpath;
    TextView btn_adddate;
    TextView view_date;
    TextView btn_adddistance;
    TextView view_time;
    TextView view_distance;
    TextView btn_addtime;
    Boolean complete_datepick;
    Boolean complet_dispick;
    EditText editmemo;
    Button addruninfo;
    SharedPreferences loginshared;
    String date;
    String mid;
    int kmdis;
    int mdis;
    int time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addruninfo);

        addmultipleimg = findViewById(R.id.addmultipleimg);
        addmultipleimg.setVisibility(View.VISIBLE);

        addimg = findViewById(R.id.addimg);
        btn_adddate = findViewById(R.id.btn_adddate);
        view_date = findViewById(R.id.viewdate_addrun);
        btn_adddistance = findViewById(R.id.btn_adddistance);
        view_distance = findViewById(R.id.viewdistance_addrun);
        view_time = findViewById(R.id.viewtime_addrun);
        btn_addtime = findViewById(R.id.btn_addtime);
        editmemo = findViewById(R.id.editmemo_addrun);
        addruninfo = findViewById(R.id.btn_addruninfo);

        // 로그인 한 유저의 아이디 가졍오기
        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);

        // 내친구 리사이클러뷰
        my_recyclerView   = findViewById(R.id.rc_addrun);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arr_imgpath = new ArrayList<>();

        complete_datepick = false;
        complet_dispick = false;

        addmultipleimg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK); //버튼을 누르면 해당 Intent 호출
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // true로 값을 입력해야 사진 다중 선택 가능
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //데이터를 uri로 받는다.
                startActivityForResult(intent, GET_GALLERY_IMAGE); //GET_GALLERY_IMAGE는
                }
        });

        addimg.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {

                int permission = ContextCompat.checkSelfPermission(addruninfo.this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
        btn_adddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatepicker();
            }
        });

        btn_adddistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pickdistance_Dialog pickdistance_dialog = new Pickdistance_Dialog();
                pickdistance_dialog.calldialog();
            }
        });
        btn_addtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picktime_Dialog picktime_dialog = new Picktime_Dialog();
                picktime_dialog.calldialog();
            }
        });
        addruninfo.setOnClickListener(new View.OnClickListener() {
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

    public void showdatepicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(addruninfo.this,addruninfo.this,year,month,day).show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        try {
            month++;
            if(compare_sdate(year,month,day)){
                // 저장 및 텍스트 설정
                date = year+"/"+month+"/"+day;
                view_date.setText(date);
                complete_datepick = true;
            }else{
                complete_datepick = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode ==GET_GALLERY_IMAGE) //이미지 다중 선택하는 것
            {
                    if(resultCode ==RESULT_OK) {
                    if (data.getClipData() == null) {
                    } else {
                        addimg.setVisibility(View.VISIBLE);
                        addmultipleimg.setVisibility(View.INVISIBLE);

                    ClipData clipData = data.getClipData();
                    if (clipData.getItemCount() == 1) { //사진을 1개 선택했을 때
                    Uri img_path = clipData.getItemAt(0).getUri(); //이미지 URI
                    arr_imgpath.add(String.valueOf(img_path));  //사진 리스트에 추가
                    }
                    else if ( clipData.getItemCount() > 1) { //사진을 1개 이상 선택했을 때
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri img_path = clipData.getItemAt(i).getUri();
                        arr_imgpath.add(String.valueOf(img_path)); }
                    }
                        }
                        imageAdapter = new ImageAdapter(arr_imgpath,addruninfo.this);

                        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(addruninfo.this);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        my_recyclerView.setLayoutManager(linearLayoutManager);
                        // 어댑터 추가
                        my_recyclerView.setAdapter(imageAdapter);
                    }
            }
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                Log.e("fileurl",String.valueOf(fileUri));
                try {
                    imageAdapter.additem(fileUri, addruninfo.this);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }


    public Boolean compare_sdate(int year,int month,int day) throws ParseException {
            SimpleDateFormat format;
            format = new SimpleDateFormat("yyyy-MM-dd");
           Date c_date = format.parse(year +"-"+month+"-"+day);

            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );

            Date currentTime = new Date();

            String oTime = mSimpleDateFormat.format ( currentTime ); //현재시간 (String)
            Date currentDate =  mSimpleDateFormat.parse( oTime );

            Log.e("date",c_date.compareTo(currentDate)+"");
            if(c_date.compareTo(currentDate) < 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(addruninfo.this);
                    builder.setTitle("현재 날짜보다 늦게 설정해주십시오. ")        // 제목 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                public void onClick(DialogInterface dialog, int whichButton){
                                        //원하는 클릭 이벤트를 넣으시면 됩니다.
                                        }
                    });
                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();    // 알림창 띄우기
                           return false;
                }else{
                    return true;
                 }
            }


    // 포스트를 서버에 업로드하는 메소드
    private void uploadPost() throws IOException {
        // urlList를 json 배열로 변환
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<arr_imgpath.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", arr_imgpath.get(i));
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
                        Toast.makeText(addruninfo.this, "정보가 등록 되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intentR = new Intent();
                        setResult(RESULT_OK,intentR); //결과를 저장

                        finish();
                    } else {
                        // 업로드 실패
                        Toast.makeText(addruninfo.this, "정보 등록 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(addruninfo.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("json",jsonArray.toString());

        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("id", mid);
        smpr.addStringParam("rating", String.valueOf(0));
        smpr.addStringParam("memo", editmemo.getText().toString());
        smpr.addStringParam("distance", String.valueOf(mdis));
        smpr.addStringParam("time", String.valueOf(time));
        smpr.addStringParam("run_date", date);
        smpr.addStringParam("url", jsonArray.toString()); // json 배열을 문자열로 변환
        smpr.addStringParam("cntImage", String.valueOf(arr_imgpath.size())); // 첨부된 사진 개수

        //이미지 파일 추가 (pathList는 첨부된 사진의 내부 uri string 리스트)
        for(int i=0; i<arr_imgpath.size(); i++) {
            // uri 절대 경로 구하기
            String[] proj= {MediaStore.Images.Media.DATA};
            CursorLoader loader= new CursorLoader(addruninfo.this, Uri.parse(arr_imgpath.get(i)), proj, null, null, null);
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
        RequestQueue requestQueue = Volley.newRequestQueue(addruninfo.this);
        requestQueue.add(smpr);
    }


    public class Pickdistance_Dialog {
        Dialog dig;
        Button btn_setdistance;
        EditText pickerkm_distance;
        EditText pickerm_distance;

        public void calldialog() {
            dig = new Dialog(addruninfo.this);
            // 액티비티의 타이틀바를 숨긴다.
            dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dig.setContentView(R.layout.pickdistance_dialog);
            btn_setdistance = dig.findViewById(R.id.btn_setdistance);
            pickerkm_distance = dig.findViewById(R.id.picker1_time);
            pickerm_distance = dig.findViewById(R.id.picker2_time);

            btn_setdistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kmdis = Integer.parseInt(pickerkm_distance.getText().toString());
                    mdis = Integer.parseInt(pickerm_distance.getText().toString());
                    view_distance.setText(kmdis + "."+mdis +" km");
                    String dis = kmdis +"."+mdis;
                    mdis = (int) (Float.parseFloat(dis) * 1000);
                    complet_dispick = true;
                    dig.dismiss();

                }
            });
            dig.show();
        }
    }

     public class Picktime_Dialog{
            Dialog dig;
            Button btn_settime;
            EditText pickerhour_time;
            EditText pickermin_time;
            EditText pickersec_time;

            public void calldialog() {
                dig = new Dialog(addruninfo.this);
                // 액티비티의 타이틀바를 숨긴다.
                dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // 커스텀 다이얼로그의 레이아웃을 설정한다.
                dig.setContentView(R.layout.picktime_dialog);
                btn_settime = dig.findViewById(R.id.btn_setdistance);
                pickerhour_time = dig.findViewById(R.id.picker1_time);
                pickermin_time = dig.findViewById(R.id.picker2_time);
                pickersec_time = dig.findViewById(R.id.picker3_time);

                btn_settime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = Integer.parseInt(pickerhour_time.getText().toString());
                        int min = Integer.parseInt(pickermin_time.getText().toString());
                        int sec = Integer.parseInt(pickersec_time.getText().toString());

                        view_time.setText(hour+":"+min+":"+sec);

                        time = (hour * 60 * 60) +(min*60) +sec;

                        dig.dismiss();
                    }
                });
                dig.show();
            }
        }



}
