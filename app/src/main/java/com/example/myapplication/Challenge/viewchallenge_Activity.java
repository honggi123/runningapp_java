package com.example.myapplication.Challenge;







import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MySingleton;
import com.example.myapplication.Profile.ProfileMenuActivity;
import com.example.myapplication.R;
import com.example.myapplication.Run.RunMenuActivity;
import com.example.myapplication.viewact.ViewactMenuActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class viewchallenge_Activity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;

    ChallengeInfo challengeInfo;
    TextView creater;
    TextView name;
    TextView distance;
    TextView pnum;
    TextView date;
    TextView n_distance;
    ImageView view_leaderboard;
    TextView viewjoin;
    SharedPreferences loginshared;
    Button btn_join;
    String mid;
    ImageView btn_chsetting;
    ProgressBar progressBar;

    RequestQueue requestQueue;

    ImageView menurun;
    ImageView menuviewact;
    ImageView menuch;
    ImageView menumy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewchallenge);
        name = findViewById(R.id.view_name_viewch);
        distance = findViewById(R.id.view_distance_viewch);
        date = findViewById(R.id.view_date_viewch);
        pnum = findViewById(R.id.viewrating);
        n_distance = findViewById(R.id.view_ndistance_vwch);
        progressBar = findViewById(R.id.distance_progressBar);
        viewjoin = findViewById(R.id.textview_viewjoin);
        creater = findViewById(R.id.viewtime);
        btn_join = findViewById(R.id.btn_enter_viewch);
        btn_chsetting = findViewById(R.id.btn_chsetting);
        view_leaderboard = findViewById(R.id.btn_chleaderboard);
        btn_chsetting.setVisibility(View.INVISIBLE);

        int where = getIntent().getIntExtra("where",0);
        challengeInfo = (ChallengeInfo) getIntent().getSerializableExtra("chinfo");

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        // 로그인 정보
        mid = loginshared.getString("id", null);
        btn_join.setEnabled(false);

        if(where == 1){
            Log.e("whereafter",where+"");
            btn_join.setVisibility(View.INVISIBLE);
            btn_chsetting.setVisibility(View.INVISIBLE);
            view_leaderboard.setVisibility(View.INVISIBLE);
            viewjoin.setVisibility(View.INVISIBLE);
        }else{
            if (mid.equals(challengeInfo.id)) {
                btn_join.setVisibility(View.INVISIBLE);
                btn_chsetting.setVisibility(View.VISIBLE);
            }
        }

        vwchinfo(challengeInfo.cno);
        setview();

        chkjoinch();



        view_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewchallenge_Activity.this, LeaderBoard_Activity.class);
                intent.putExtra("chinfo", challengeInfo);
                startActivity(intent);
            }
        });

        btn_chsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] oItems = {"수정하기", "챌린지 완료"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(viewchallenge_Activity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                oDialog.setItems(oItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (oItems[which].equals("수정하기")) {
                            Intent intent = new Intent(viewchallenge_Activity.this, ChModify.class);
                            intent.putExtra("chinfo", challengeInfo);
                            startActivityForResult(intent,REQUEST_CODE);

                        } else if (oItems[which].equals("챌린지 완료")){

                            endch(challengeInfo.cno);

                        }

                        {
                        }

                    }
                }).show();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE) {
                Log.e("requestCode",RESULT_OK+"");

                if (resultCode != RESULT_OK) {
                    return;
                }
                challengeInfo = (ChallengeInfo) data.getSerializableExtra("chinfo");
                setview();
                Intent intentR = new Intent();
                intentR.putExtra("modify" , true);  // 전 액티비티에 수정하기를 완료했다는 boolean 값을 넣어준다.
                setResult(RESULT_OK,intentR); // 성공 결과를 저장
                intentR.putExtra("chinfo" , challengeInfo); // 수정된 challengeinfo 객체값 넘겨주기
            }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //vwchinfo(challengeInfo.cno);
    }


    public void setview(){
        name.setText(challengeInfo.name);
        String newdate =timetodate(challengeInfo.g_date);
        date.setText(challengeInfo.s_date +" ~ " + newdate);
        pnum.setText(String.valueOf(challengeInfo.num_member));
        creater.setText(challengeInfo.id);
        double mdistance = ((challengeInfo.g_distance) / 1000.00);
        distance.setText(String.format("%.2f",mdistance)+" km");
        double m_ndistance = ((challengeInfo.n_distance) / 1000.00);
        n_distance.setText(String.format("%.2f",m_ndistance)+" km");

        double olddist =  (challengeInfo.n_distance / (double)challengeInfo.g_distance);
        int d = (int) (olddist * 100);
        Log.e("dis",challengeInfo.n_distance+"");
        Log.e("dis",challengeInfo.g_distance+"");
        Log.e("dis",olddist+"");
        progressBar.setProgress(d);
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

    public void endch(int cno){
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.12.49.32/endch.php";

                ProgressDialog progressDialog;
                        progressDialog = ProgressDialog.show(viewchallenge_Activity.this,
                        "기다려주세요..", null, true, true);
                  progressDialog.show();

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
                Toast.makeText(viewchallenge_Activity.this, "해당 챌린지가 종료되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intentR = new Intent();
                    intentR.putExtra("remove" , true); //사용자에게 입력받은값 넣기
                    intentR.putExtra("chinfo" , challengeInfo); //사용자에게 입력받은값 넣기
                    setResult(RESULT_OK,intentR); //결과를 저장
                    progressDialog.dismiss();
                    finish();

                finish();
                } else {
                // 업로드 실패
                Toast.makeText(viewchallenge_Activity.this, "챌린지 종료를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                Toast.makeText(viewchallenge_Activity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
                }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("cno", String.valueOf(cno));

                // 서버에 데이터 보내고 응답 요청
             //    requestQueue = Volley.newRequestQueue(viewchallenge_Activity.this);
             //   requestQueue.add(smpr);
        MySingleton.getInstance(this).addToRequestQueue(smpr);
    }

    public void joinch(int cno, String id){
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.12.49.32/joinchallenge.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if(success) {
                // 업로드 성공
                Toast.makeText(viewchallenge_Activity.this, "챌린지에 참가했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intentR = new Intent();
                    intentR.putExtra("join" , true);  // 전 액티비티에 수정하기를 완료했다는 boolean 값을 넣어준다.
                    setResult(RESULT_OK,intentR); // 성공 결과를 저장
                finish();
                } else {
                // 업로드 실패
                Toast.makeText(viewchallenge_Activity.this, "챌린지 참가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                Toast.makeText(viewchallenge_Activity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("userID", id);
                smpr.addStringParam("cno",String.valueOf(cno));

                // 서버에 데이터 보내고 응답 요청
               // RequestQueue requestQueue = Volley.newRequestQueue(viewchallenge_Activity.this);
             //   requestQueue.add(smpr);
        MySingleton.getInstance(this).addToRequestQueue(smpr);
    }

        public void vwchinfo(int cno){     //  내 챌린지
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/viewchinfo.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("json",String.valueOf(jsonObject));
                        int c_num = jsonObject.getInt("num");
                        if(c_num == 1) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i= 0; i < c_num; i++){
                                Log.e("data",String.valueOf(data.get(i)));
                                JSONObject dataJSONObject = data.getJSONObject(i);
                                name.setText(dataJSONObject.getString("name"));
                                String d1 =timetodate(dataJSONObject.getString("e_date"));
                                date.setText(dataJSONObject.getString("s_date") +" ~ " + d1);
                                pnum.setText(String.valueOf(Integer.parseInt(dataJSONObject.getString("num_member"))));
                                creater.setText(dataJSONObject.getString("id"));
                                double kmdistance = (Integer.parseInt(dataJSONObject.getString("g_distance")) / 1000.00);
                                distance.setText(String.format("%.2f",kmdistance)+" km");
                            }
                        }else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(viewchallenge_Activity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            smpr.addStringParam("cno",String.valueOf(cno));

            // 서버에 데이터 보내고 응답 요청
          // RequestQueue requestQueue = Volley.newRequestQueue(viewchallenge_Activity.this);
           // requestQueue.add(smpr);
            MySingleton.getInstance(this).addToRequestQueue(smpr);
        }

    public void chkjoinch(){
                // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                String serverUrl="http://3.12.49.32/chkjoinchallenge.php";

                // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
                try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");
                if(success) {
                    boolean join = jsonObject.getBoolean("join");
                    if(!join){
                        btn_join.setEnabled(true);
                        btn_join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                joinch(challengeInfo.cno,mid);
                            }
                        });
                    }else{
                        btn_join.setText("이미 참여중인 챌린지 입니다.");
                    }

                } else {
                    Toast.makeText(viewchallenge_Activity.this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
                }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
                Toast.makeText(viewchallenge_Activity.this, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                });

                // 요청 객체에 보낼 데이터를 추가
                smpr.addStringParam("id", mid);
                smpr.addStringParam("cno",String.valueOf(challengeInfo.cno));

                Log.e("id",mid);
        Log.e("cno",String.valueOf(challengeInfo.cno));
                // 서버에 데이터 보내고 응답 요청
              //   requestQueue = Volley.newRequestQueue(viewchallenge_Activity.this);
               // requestQueue.add(smpr);
        MySingleton.getInstance(this).addToRequestQueue(smpr);
    }


}
