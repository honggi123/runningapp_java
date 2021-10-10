package com.example.myapplication.Run;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Coaching;
import com.example.myapplication.R;

import java.util.ArrayList;

public class RunMsgAdapter extends RecyclerView.Adapter<RunMsgAdapter.Holder> {

    ArrayList<Msg> arr_msg;
    Context context;
    int selpos;

    RunMsgAdapter(ArrayList<Msg> arr_msg) {
        this.arr_msg = arr_msg;
    }

    @NonNull
    @Override
    public RunMsgAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.runmsgitem, parent, false);

        return new RunMsgAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunMsgAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
         holder.msgname.setText(arr_msg.get(position).getMsg());

         if(arr_msg.get(position).getChoice()){
             holder.msgname.setTextColor(Color.GREEN);
         }else{
             holder.msgname.setTextColor(Color.BLACK);
         }
    }


    @Override
    public int getItemCount() {
        return arr_msg.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView msgname;

        public Holder(View itemView){
            super(itemView);
            msgname = itemView.findViewById(R.id.msg);

            msgname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choiceinit();
                    selpos = getAdapterPosition();
                    arr_msg.get(getAdapterPosition()).setChoice(true);
                    notifyDataSetChanged();
                }
            });
        }

        public void choiceinit(){
            for(int i=0; i<=arr_msg.size()-1;i++){
                arr_msg.get(i).setChoice(false);
            }
        }
    }

    public int getselpos(){
        return selpos;
    }

    public void addmsg(String msg){
        Msg msg1 = new Msg();
        msg1.setMsg(msg);
        arr_msg.add(msg1);
        notifyDataSetChanged();
    }
}