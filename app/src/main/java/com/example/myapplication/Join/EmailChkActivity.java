package com.example.myapplication.Join;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Loign.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.Request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailChkActivity extends AppCompatActivity {

    Button btn_sendmail;
    Button btn_mailnumchk;
    Button btn_signup;
    EditText edit_mail;
    EditText edit_mailnum;
    String Gmailcode;
    Boolean emailcodesame;
    String mID, mPW, gender, email;
    String num;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mailcheck);
        btn_mailnumchk = findViewById(R.id.btn_mailchk);
        btn_sendmail = findViewById(R.id.btn_sendmail);
        edit_mail = findViewById(R.id.edit_email);
        edit_mailnum = findViewById(R.id.edit_mailnum);
        btn_signup = findViewById(R.id.btn_singup);
        btn_mailnumchk.setVisibility(View.INVISIBLE);
        edit_mailnum.setVisibility(View.INVISIBLE);

        mID = getIntent().getStringExtra("id");
        mPW = getIntent().getStringExtra("pw");
        gender =getIntent().getStringExtra("gender");

        Log.e("gender",gender);
        btn_mailnumchk.setVisibility(View.VISIBLE);
        edit_mailnum.setVisibility(View.VISIBLE);
        // 서버 측으로 메일 보내기 요청
        btn_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AsyncTask를 통해 HttpURLConnection 수행.
                NetworkTask networkTask = new NetworkTask();
                networkTask.execute(edit_mail.getText().toString());

            }
        });


        btn_mailnumchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_mailnum.getText().toString().equals(num)){
                    email = edit_mail.getText().toString();
                    emailcodesame = true;
                    Toast.makeText(EmailChkActivity.this, "인증번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    emailcodesame = false;
                    Toast.makeText(EmailChkActivity.this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailcodesame) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response){
                            try {
                                Log.e("json", response);
                                JSONObject jsonObject = new JSONObject(response);

                                boolean success = jsonObject.getBoolean("success");
                                if (success) { // 회원등록에 성공한 경우
                                    Toast.makeText(EmailChkActivity.this, "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EmailChkActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else { // 회원등록에 실패한 경우
                                    Toast.makeText(EmailChkActivity.this, "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    //서버로 volley를 이용해서 요청을 함
                    RegisterRequest registerRequest = new RegisterRequest(mID, mPW, email, gender, Request.Method.POST, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EmailChkActivity.this);
                    queue.add(registerRequest);

                }else{
                    Toast.makeText(EmailChkActivity.this, "이메일 인증을 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public class NetworkTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EmailChkActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("json",result);
            try{
            JSONObject jsonObject = new JSONObject(result);
            boolean havemail = jsonObject.getBoolean("havemail");
                            if (havemail) { // 중복 메일이 있다면
                                Toast.makeText(EmailChkActivity.this, "[이메일 전송 불가]중복된 이메일이 있습니다. ", Toast.LENGTH_SHORT).show();
                            } else {        // 중복 메일이 없다면
                                num = jsonObject.getString("num");
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Toast.makeText(EmailChkActivity.this, "이메일 전송 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EmailChkActivity.this, "이메일 전송이 안되었습니다..", Toast.LENGTH_SHORT).show();
                                }
                            }
                progressDialog.dismiss();
            }catch (JSONException e) {
                            e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://3.12.49.32/mail.php";
            String postParameters = "email=" + searchKeyword1;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {

                errorString = e.toString();
                return null;
            }

        }
    }






}
