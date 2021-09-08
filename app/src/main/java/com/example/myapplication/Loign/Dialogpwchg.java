package com.example.myapplication.Loign;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class Dialogpwchg {
    Context context;
    EditText editnewpw;
    EditText editnewpwchk;
    TextView txt_pwnosmae;
    TextView getPwstandardfalse;
    TextView txt_pwsame;
    Boolean pwsame;
    Boolean pwchk;
    Button btn_pwchg;
    String id;
    Dialog dig;

    public Dialogpwchg(Context context,String id){
        this.context = context;
        this.id = id;
    }

    public void calldialog() {
        dig = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dig.setContentView(R.layout.dialog_chgpw);
        editnewpw = dig.findViewById(R.id.edit_pwchg);
        editnewpwchk = dig.findViewById(R.id.edit_pwchkchg);
        btn_pwchg = dig.findViewById(R.id.btn_pwchg);
        txt_pwsame = dig.findViewById(R.id.txt_pwsame);
        getPwstandardfalse = dig.findViewById(R.id.txt_pwstandardfalse);
        txt_pwnosmae = dig.findViewById(R.id.txt_pwnosame);

        btn_pwchg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwsame || pwchk) {
                    NetworkTask networkTask = new NetworkTask();
                    networkTask.execute(id, editnewpw.getText().toString());
                }else{
                    Toast.makeText(context, "양식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        editnewpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = editnewpw.getText().toString();
                //대소문자 구분 숫자 특수문자  조합 9 ~ 12 자리
                String pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";

                if(Pattern.matches(pwPattern,pw)){
                    getPwstandardfalse.setVisibility(View.GONE);
                    pwchk = true;
                }else{
                    getPwstandardfalse.setVisibility(View.VISIBLE);
                    pwchk = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 비밀번호 일치 확인
        editnewpwchk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_pwsame.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = editnewpw.getText().toString();
                String repw = editnewpwchk.getText().toString();
                if (pw.equals(repw)) {
                    txt_pwsame.setVisibility(View.VISIBLE);
                    txt_pwnosmae.setVisibility(View.GONE);
                    pwsame = true;
                } else {
                    txt_pwnosmae.setVisibility(View.VISIBLE);
                    txt_pwsame.setVisibility(View.GONE);
                    pwsame = false;
                }

            }
        });
        dig.show();
    }




    public class NetworkTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("json",result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                Boolean success = jsonObject.getBoolean("success");
                if(success){
                    Toast.makeText(context, "비밀번호 변경완료.", Toast.LENGTH_SHORT).show();
                    dig.dismiss();
                }else{
                    Toast.makeText(context, "비밀번호 변경이 안되었습니다.", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword0 = params[0];
            String searchKeyword1 = params[1];

            String serverURL = "http://3.143.9.214/pwchg.php";
            String postParameters = "id=" + searchKeyword0 + "&pw="+searchKeyword1;

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
