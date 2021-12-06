package com.example.myapplication.Chat.GeneralUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GChatroomAdapter extends RecyclerView.Adapter<GChatroomAdapter.Holder> {

    ArrayList<Chatroom> arr_chatroom;

    Context context;
    SimpleDateFormat input_format    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 입력포멧
    SimpleDateFormat format1 = new SimpleDateFormat ( "HH:mm");
    Date date;


    public GChatroomAdapter(ArrayList<Chatroom> arr_chatroom){
        this.arr_chatroom = arr_chatroom;

    }

    @NonNull
    @Override
    public GChatroomAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chatroomitem, parent, false);
        GChatroomAdapter.Holder vh = new GChatroomAdapter.Holder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GChatroomAdapter.Holder holder, @SuppressLint("RecyclerView") int position){

        holder.otherid.setText(arr_chatroom.get(position).getCoachname());

        // 저장된 메시지가 아무것도 없다면 의뢰서가 도착
        if(arr_chatroom.get(position).getlastMsg().equals("question")){
             holder.lastmsg.setText("코칭 신청서를 보냈습니다!");
            try {
                date = input_format.parse(arr_chatroom.get(position).getReg_date());
                holder.lastmsgtime.setText(format1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String amsg = "["+arr_chatroom.get(position).getlastMsg()+"]";
                JSONArray jsonArray = new JSONArray(amsg);
                JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.length()-1);
                String msg = jsonObject.getString("message");

                holder.lastmsg.setText(msg);

                date = input_format.parse(jsonObject.getString("time"));
                holder.lastmsgtime.setText(format1.format(date));

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
        TextView lastmsgtime;
        public Holder(View itemView) {
            super(itemView);
            otherid = itemView.findViewById(R.id.qid);
            lastmsg = itemView.findViewById(R.id.lastmsg);
            lastmsgtime = itemView.findViewById(R.id.lmsg_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatroominfo",arr_chatroom.get(getAdapterPosition()));
                    intent.putExtra("coach","false");
                    ((GChatRoomActivity) context).startActivityForResult(intent,200);
                }
            });
        }
    }



}

