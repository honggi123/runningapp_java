package com.example.myapplication.Chat.CoachUser;

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
import com.example.myapplication.Chat.GeneralUser.GChatRoomActivity;
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
                    ((CChatroomActivity) context).startActivityForResult(intent,200);
                }
            });
        }
    }



}

