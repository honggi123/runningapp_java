package com.example.myapplication.Chat;


import com.airbnb.lottie.L;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.myapplication.Chat.CoachUser.CChatroomActivity;
import com.example.myapplication.Chat.CoachUser.SetMemInfo;
import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.example.myapplication.Socketsingleton;
import com.example.myapplication.viewact.Coach.Coaching;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class ChatActivity extends AppCompatActivity {

    RecyclerView my_recyclerView;
    ArrayList<MessageItem> arrmsg;
    ChatAdapter chatAdapter;

    EditText etmsg;
    Button btn_send;
    SharedPreferences loginshared;
    String mid;
    String read;
    Chatroom chatroom;
    Socketsingleton socketsingleton;
    Socket socket;
    OutputStream socketoutputStream;
    PrintWriter sendWriter;
    String otherid;
    Handler notifyhandler;
    MessageItem newmessageItem;
    Boolean isService;
    MySocketService socketService;
    private String[] splited;
    ConstraintLayout layoutsetmem;

    Button btn_accept;
    Button btn_refuse;

    Intent sendintent;
    Gson gson;
    Button btn_setmem;

    String coachname;
    String question;
    TextView oid;
    TextView tid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatactivity);

        loginshared = getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);
        btn_send = findViewById(R.id.btn_msgsend);
        etmsg = findViewById(R.id.etmsg);

        btn_setmem = findViewById(R.id.btn_setmem);
        Boolean coach = Boolean.parseBoolean(getIntent().getStringExtra("coach"));
        layoutsetmem = findViewById(R.id.layoutsetmem);
        tid = findViewById(R.id.tid);

        gson = new Gson();

        sendintent = new Intent("send");
        sendintent.setPackage("com.example.myapplication");
        // 채팅방 정보
        chatroom = (Chatroom)getIntent().getSerializableExtra("chatroominfo");
        getchatroomrequest(String.valueOf(chatroom.getNo()));
        Log.e("chatroominfo",chatroom.getNo()+"");
        //  리사이클러뷰 xml id
        my_recyclerView = findViewById(R.id.rcchat);
        // 라사이클러뷰에 넣기
        // 어댑터 객체 생성
        arrmsg = new ArrayList<MessageItem>();
        if(chatroom.getCoachid().equals(mid)){
            otherid = chatroom.getUserid();
            tid.setText(otherid);
        }else{
            otherid = chatroom.getCoachid();
            tid.setText(chatroom.getCoachname());
        }


        // 질문 작성지 추가
        addquestionmsg();

        chatAdapter = new ChatAdapter(arrmsg,mid);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        my_recyclerView.setLayoutManager(linearLayoutManager);
        // 어댑터 추가
        chatAdapter.setCoachname(chatroom.getCoachname());
        chatAdapter.setCoachid(chatroom.coachuserid);
        chatAdapter.setUserid(chatroom.userid);
        chatAdapter.setRno(chatroom.getNo());

        my_recyclerView.setAdapter(chatAdapter);


        String msg = getIntent().getStringExtra("msg");

        if(!msg.equals("null")){
            Log.e("msg","notnull");
            msg = "["+msg+"]";
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(msg);
                for(int i= 0; i< jsonArray.length(); i++){
                    MessageItem messageItem = gson.fromJson(jsonArray.get(i).toString(), MessageItem.class);
                    messageItem.setName(chatroom.getCoachname());
                    Log.e("msgitem",chatroom.getCoachname());
                    Log.e("msgitemuserid",messageItem.getId());
                    arrmsg.add(messageItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            chatAdapter.notifyDataSetChanged();
        }

        my_recyclerView.scrollToPosition(my_recyclerView.getAdapter().getItemCount() - 1);

        /*
        socketsingleton  = Socketsingleton.getInstance(getApplicationContext());
        socket = socketsingleton.getSocket();
        Log.e("socketadd",socket.getLocalSocketAddress()+"");
        try {
            socketoutputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendWriter = new PrintWriter(socketoutputStream);
*/

        if(mid.equals(chatroom.getCoachid())){
            otherid = chatroom.getUserid();
        }else{
            otherid = chatroom.getCoachid();
        }
        Log.e("otherid",otherid);


        Gson gson = new Gson();

        notifyhandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                chatAdapter.notifyDataSetChanged();
                my_recyclerView.scrollToPosition(my_recyclerView.getAdapter().getItemCount() - 1);
            }
        };

        if(mid.equals(chatroom.getCoachid())){

            getcoachornot(chatroom.getCoachid(),chatroom.getUserid());

            btn_setmem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("코칭 회원으로 등록하시겠습니까?")        // 제목 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                public void onClick(DialogInterface dialog, int whichButton){
                                    //원하는 클릭 이벤트를 넣으시면 됩니다.
                                    dialog.dismiss();
                                    Intent intent = new Intent(ChatActivity.this, SetMemInfo.class);
                                    intent.putExtra("chatroomno",String.valueOf(chatroom.getNo()));
                                    intent.putExtra("coachid",chatroom.getCoachid());
                                    intent.putExtra("userid",chatroom.getUserid());
                                    startActivityForResult(intent,500);

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                                // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                                public void onClick(DialogInterface dialog, int whichButton){
                                    //원하는 클릭 이벤트를 넣으시면 됩니다.
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기
                }
            });
        }



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c1 = Calendar.getInstance();
                String strToday = sdf.format(c1.getTime());

                String msg = etmsg.getText().toString();
                etmsg.setText("");

                MessageItem messageItem = new MessageItem();
                messageItem.setId(mid);
                messageItem.setRoomid(String.valueOf(chatroom.getNo()));
                messageItem.setMessage(msg);
                messageItem.setType(1);
                messageItem.setTime(strToday);
                Log.e("arrmsg",arrmsg.size()+"");
                arrmsg.add(messageItem);
                Log.e("arrmsg",arrmsg.size()+"");
                chatAdapter.notifyDataSetChanged();
                messageItem.setType(2);
                String json = gson.toJson(messageItem);
                my_recyclerView.scrollToPosition(my_recyclerView.getAdapter().getItemCount() - 1);

                String totalmsg = "msg@"+String.valueOf(chatroom.getNo()+"@"+mid+"@"+otherid+"@"+json);

                sendintent.putExtra("send",totalmsg);
                getApplicationContext().sendBroadcast(sendintent);

                sendintent.putExtra("send","actpause@"+chatroom.getNo());
                getApplicationContext().sendBroadcast(sendintent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("action");
        registerReceiver(mBroadTestReceiver, intentFilter);
    }

    public BroadcastReceiver mBroadTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("action")) {
                // 브로드캐스트 수신받았을 때 실행할 내용
                String read = intent.getStringExtra("read");
                splited = read.split("@");
                if(splited[0].equals("receivemsg")){
                    Log.e("message","log");
                    newmessageItem = gson.fromJson(splited[1], MessageItem.class);
                    newmessageItem.setType(2);
                    Log.e("message",newmessageItem.getId()+newmessageItem.getMessage());
                    arrmsg.add(newmessageItem);

                    Message message = notifyhandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    message.setData(bundle);

                    notifyhandler.handleMessage(message);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == 500){
                    if(resultCode != RESULT_OK){ // 값이 성공적으로 반환되었을때
                        return;
                    }
                    // 코드 작성
             getcoachornot(chatroom.getCoachid(),chatroom.getUserid());

         }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("chatroom","onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void addquestionmsg(){
        // 맨 처음은 질문에 대한 답변이 들어간다.
        MessageItem qmessageItem = new MessageItem();
        qmessageItem.setName(chatroom.getUserid());
        JSONObject jsonObject;
        String msg =qmessageItem.getName()+"님이 보낸 코칭 신청서\n\n";
        Iterator i;
        try {
            jsonObject = new JSONObject(chatroom.getQuestion());
            i = jsonObject.keys();
            while(i.hasNext())
            {
                String q = i.next().toString();
                String a = jsonObject.getString(q);
                msg += ("*"+q+" : "+a) ;
                msg += "\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        qmessageItem.setId(chatroom.userid);
        qmessageItem.setTime(chatroom.getReg_date());
        qmessageItem.setMessage(msg);
        qmessageItem.setType(3);
        question = gson.toJson(qmessageItem,MessageItem.class);

        arrmsg.add(qmessageItem);
    }



    public void getchatroomrequest(String rno){
        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.12.49.32/getchatroommsg.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("chatmsg",response);
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
            }
        });

        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("rno", rno);
        smpr.addStringParam("cid", chatroom.getCoachid());

        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(smpr);
    }




    public void getcoachornot(String cid,String uid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/getcoachornot.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            if(success) {
                int num = jsonObject.getInt("num");
                Log.e("num",num+"");
                if(num > 0){
                    btn_setmem.setVisibility(View.GONE);
                }else{
                    btn_setmem.setVisibility(View.VISIBLE);
                }

            } else {
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            }
            });

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("coachID", cid);
             smpr.addStringParam("userID", uid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
            }

}
