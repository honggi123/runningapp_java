package com.example.myapplication.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatinroomAdapter extends RecyclerView.Adapter<ChatinroomAdapter.Holder> {

    ArrayList<MessageItem> arrmsg;
    String loginid;
    ViewGroup parent;

    public ChatinroomAdapter(ArrayList<MessageItem> arrmsg, String loginid) {
        this.arrmsg = arrmsg;
        this.loginid = loginid;
    }


    @NonNull
    @Override
    public ChatinroomAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.parent = parent;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        view = inflater.inflate(R.layout.chatroomitem, parent, false);

        return new ChatinroomAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return arrmsg.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(arrmsg.get(position).getName().equals(loginid)){
            return  0;
        }else{
            return  1;
        }
    }
}
