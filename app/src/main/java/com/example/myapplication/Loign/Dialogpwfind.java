package com.example.myapplication.Loign;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dialogpwfind {

    EditText edit_Email;
    EditText edit_num;
    EditText edit_id;
    Button btn_sendmail;
    Button btn_numchk;
    Boolean numchk;
    String num;

    private Context context;

    public Dialogpwfind(Context context){
        this.context = context;
    }

    public void calldialog(){
        final Dialog dig = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dig.setContentView(R.layout.dialog_pwfind);

        edit_Email = dig.findViewById(R.id.edit_emailpwfind);
        edit_num = dig.findViewById(R.id.edit_numpwfind);
        edit_id = dig.findViewById(R.id.edit_idpwfind);
        btn_sendmail = dig.findViewById(R.id.btn_sendemailpwfind);
        btn_numchk = dig.findViewById(R.id.btn_numchkpwfind);

        // 인증번호 보내기 버튼
         btn_sendmail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // AsyncTask를 통해 HttpURLConnection 수행.
                 NetworkTask networkTask = new NetworkTask();
                 networkTask.execute(edit_id.getText().toString(),edit_Email.getText().toString());
             }
         });

        // 인증 완료 버튼
        btn_numchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_num.getText().toString().equals(num)){
                    Toast.makeText(context, "인증 확인 되었습니다.", Toast.LENGTH_SHORT).show();
                    dig.dismiss();
                    Dialogpwchg dialogpwchg = new Dialogpwchg(context,edit_id.getText().toString());
                    dialogpwchg.calldialog();
                }else{
                    Toast.makeText(context, "인증 번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
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

            progressDialog = ProgressDialog.show(Dialogpwfind.this.context,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("json",result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                Boolean havemail = jsonObject.getBoolean("havemail");
                if(havemail){
                    num = jsonObject.getString("num");
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(context, "이메일 전송 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "이메일 전송이 안되었습니다..", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "일치하는 아이디, 이메일이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }catch (Exception e){
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword0 = params[0];
            String searchKeyword1 = params[1];

            String serverURL = "http://3.12.49.32/pwfind.php";
            String postParameters = "id="+searchKeyword0+"&email=" + searchKeyword1;

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
