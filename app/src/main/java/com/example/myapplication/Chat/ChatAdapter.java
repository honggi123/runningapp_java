package com.example.myapplication.Chat;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Chat.CoachUser.SetMemInfo;
import com.example.myapplication.MySingleton;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    ArrayList<MessageItem> arrmsg;
    String loginid;
    ViewGroup parent;
    int type;
    SimpleDateFormat input_format    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 입력포멧
    SimpleDateFormat format1 = new SimpleDateFormat ( "HH:mm");
    String coachname;
    String coachid;
    Context context;
    String userid;
    String usercat;

    int rno;

    public ChatAdapter(ArrayList<MessageItem> arrmsg,String loginid) {
        this.arrmsg = arrmsg;
        this.loginid = loginid;
    }

    @NonNull
    @Override
    public ChatAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        this.parent = parent;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        type = viewType;
        switch(viewType){

          case  1:
              view = inflater.inflate(R.layout.chat_my_msgbox, parent, false);
              Log.e("view1","view1");
              return new ChatAdapter.Holder(view);
          case 2:
              view = inflater.inflate(R.layout.other_msgbox, parent, false);
              return new ChatAdapter.Holder(view);
          default:
            break;
        }
        return new ChatAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Log.e("arrmsg",arrmsg.get(position).getMessage());
        Log.e("type",type+"");

        if(type == 1){
            //holder.mid.setText(arrmsg.get(position).getId());
            holder.mmsg.setText(arrmsg.get(position).getMessage());
            Log.e("type",holder.mid.getId()+"");

            try {
                Date date = input_format.parse(arrmsg.get(position).getTime());
                holder.mtime.setText(format1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if(type ==2){
            Log.e("coachid",coachid);
            Log.e("arrmsg.get(position).getId())",arrmsg.get(position).getId());
            /*
            if(coachid.equals(arrmsg.get(position).getId())){
                holder.oid.setText(coachname);
            }else{
                holder.oid.setText(arrmsg.get(position).getId());
            }

             */

            holder.omsg.setText(arrmsg.get(position).getMessage());
            Log.e("type",holder.oid.getId()+"");

            try {
                Date date = input_format.parse(arrmsg.get(position).getTime());
                holder.otime.setText(format1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public int getItemCount() {
        return arrmsg.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView mid;
        TextView mmsg;
        TextView mtime;
        TextView oid;
        TextView omsg;
        TextView otime;
        TextView qid;
        TextView qmsg;
        TextView qtime;
        ImageView oprofile;

        public Holder(View itemView) {
            super(itemView);
            Log.e("holder","set");
            Log.e("holdertype",type+"");

            if(type == 1){
                mid = itemView.findViewById(R.id.mid);
                Log.e("type",mid.getId()+"");
                mmsg = itemView.findViewById(R.id.mmsg);
                mtime = itemView.findViewById(R.id.mmsg_time);
            }else if(type == 2){
                oid = itemView.findViewById(R.id.oid);
                omsg = itemView.findViewById(R.id.omsg);
                otime = itemView.findViewById(R.id.omsg_time);
                oprofile = itemView.findViewById(R.id.oprofile);
            }
        }
    }

    @Override
    public int getItemViewType(int position){

        Log.e("arrmsgid",arrmsg.get(position).getId());
        Log.e("loginid",loginid);
        if(arrmsg.get(position).getId().equals(loginid)){
            type = 1;
            return  1;
        }else{
            type = 2;
            return 2;
        }
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public void setCoachid(String coachid) {
        this.coachid = coachid;
    }

    public void setUsercat(String usercat) {
        this.usercat = usercat;
    }
}
