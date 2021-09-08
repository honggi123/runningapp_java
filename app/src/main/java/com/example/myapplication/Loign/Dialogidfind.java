package com.example.myapplication.Loign;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.Request.EmailsendRequest;


import android.os.AsyncTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dialogidfind  {

    EditText edit_Email;
    EditText edit_num;
    TextView result_id;
    TextView txt1;
    Button btn_numchk;
    Button btn_sendnum;
    Button btn_dismiss;
    String num;
    TextView result;
    private  Context context;

    public Dialogidfind(Context context){
        this.context = context;
    }

    public void calldialog(){
        final Dialog dig = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dig.setContentView(R.layout.dialog_idfind);

        edit_Email = dig.findViewById(R.id.edit_findidemail);
        edit_num = dig.findViewById(R.id.edit_findidnum);
        btn_sendnum = dig.findViewById(R.id.btn_sendnumfindid);
        btn_numchk = dig.findViewById(R.id.btn_numchkfindid);
        result_id = dig.findViewById(R.id.resultfindid);
        txt1 = dig.findViewById(R.id.txt1_findid);
        btn_dismiss = dig.findViewById(R.id.btn_idfinddismiss);
        result = dig.findViewById(R.id.resultfindid);
        dig.show();


        btn_sendnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AsyncTask를 통해 HttpURLConnection 수행.
                NetworkTask networkTask = new NetworkTask();
                networkTask.execute(edit_Email.getText().toString());

            }
        });

        // 다이얼로그 종료
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dig.dismiss();

            }
        });

        btn_numchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_num.getText().toString().equals(num)){
                    Toast.makeText(context, "인증 완료.", Toast.LENGTH_SHORT).show();
                    edit_Email.setVisibility(View.INVISIBLE);
                    edit_num.setVisibility(View.INVISIBLE);
                    btn_numchk.setVisibility(View.INVISIBLE);
                    btn_sendnum.setVisibility(View.INVISIBLE);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("json", response);
                                JSONObject jsonObject = new JSONObject(response);
                                    String id = jsonObject.getString("id");
                                txt1.setVisibility(View.INVISIBLE);
                                btn_dismiss.setVisibility(View.VISIBLE);
                                result.setText("회원님의 아이디는 "+ id +"입니다.");
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //서버로 volley를 이용해서 요청을 함
                    EmailsendRequest emailsendRequest = new EmailsendRequest(edit_Email.getText().toString(), Request.Method.POST,"http://3.143.9.214/idshow.php", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(emailsendRequest);

                    result_id.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(context, "인증번호를 확인하세요.", Toast.LENGTH_SHORT).show();
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

            progressDialog = ProgressDialog.show(Dialogidfind.this.context,
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
                    Toast.makeText(context, "일치하는 이메일이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }catch (Exception e){
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://3.143.9.214/idfind.php";
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
