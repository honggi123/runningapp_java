package com.example.myapplication.Chat.CoachUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.Chat.GeneralUser.GChatRoomActivity;
import com.example.myapplication.MySingleton;
import com.example.myapplication.Profile.ProfileMenuActivity;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CChatroomAdapter extends RecyclerView.Adapter<CChatroomAdapter.Holder> {

    ArrayList<Chatroom> arr_chatroom;

    String f_id;
    Context context;
    ProfileMenuActivity fragment4;
    Date date = null;
    SimpleDateFormat input_format    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 입력포멧
    SimpleDateFormat format1 = new SimpleDateFormat ( "HH:mm");

    public CChatroomAdapter(ArrayList<Chatroom> arr_chatroom){
        this.arr_chatroom = arr_chatroom;
    }

    @NonNull
    @Override
    public CChatroomAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chatroomitem, parent, false);
        CChatroomAdapter.Holder vh = new CChatroomAdapter.Holder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CChatroomAdapter.Holder holder, @SuppressLint("RecyclerView") int position){
        holder.otherid.setText(arr_chatroom.get(position).getUserid());

        // 저장된 메시지가 아무것도 없다면 의뢰서가 도착
        if(arr_chatroom.get(position).getlastMsg().equals("question")){
             holder.lastmsg.setText("코칭 신청서가 도착했습니다!");


            try {
                date = input_format.parse(arr_chatroom.get(position).getReg_date());
                holder.lmsg_time.setText(format1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else{
            try {
                String amsg = "["+arr_chatroom.get(position).getlastMsg()+"]";
                JSONArray jsonArray = new JSONArray(amsg);
                JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.length()-1);
                String msg = jsonObject.getString("message");

                holder.lastmsg.setText(msg);

                date = input_format.parse(jsonObject.getString("time"));
                holder.lmsg_time.setText(format1.format(date));

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return arr_chatroom.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView otherid;
        TextView lastmsg;
        TextView lmsg_time;

        public Holder(View itemView) {
            super(itemView);
            otherid = itemView.findViewById(R.id.qid);
            lastmsg = itemView.findViewById(R.id.lastmsg);
            lmsg_time = itemView.findViewById(R.id.lmsg_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatroominfo",arr_chatroom.get(getAdapterPosition()));
                    intent.putExtra("coach","true");
                    getchatroomrequest(arr_chatroom.get(getAdapterPosition()).getNo(),
                            arr_chatroom.get(getAdapterPosition()).getCoachid(),getAdapterPosition());
                }
            });
        }
    }

    public void getchatroomrequest(int rno,String coachid,int position){
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
                        String msg = jsonObject.getString("msg");

                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("chatroominfo",arr_chatroom.get(position));
                        intent.putExtra("coach","false");
                        intent.putExtra("msg",msg);
                        Log.e("msgprint",msg);
                        ((CChatroomActivity) context).startActivityForResult(intent,200);

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
        smpr.addStringParam("rno", String.valueOf(rno));
        smpr.addStringParam("cid", coachid);

        // 서버에 데이터 보내고 응답 요청
        RequestQueue requestQueue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        requestQueue.add(smpr);
    }



}

