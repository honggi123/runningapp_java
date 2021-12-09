package com.example.myapplication.Run;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class MsgboxAdapter extends RecyclerView.Adapter<MsgboxAdapter.Holder> {

    ArrayList<friendmsg> arr_msg;
    Context context;
    int selpos;
    runActivity runActivity;

    MsgboxAdapter(ArrayList<friendmsg> arr_msg,runActivity runActivity) {
        this.arr_msg = arr_msg;
        this.runActivity = runActivity;
    }

    @NonNull
    @Override
    public MsgboxAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.msgboxitem, parent, false);

        return new MsgboxAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.wt.setText(arr_msg.get(position).getId());
        holder.content.setText(arr_msg.get(position).getContent());
        if(arr_msg.get(position).getLoaction() != null){
            holder.btn_locate.setVisibility(View.INVISIBLE);
        }else{
            holder.btn_locate.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public int getItemCount() {
        return arr_msg.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView wt;
        TextView content;
        ImageView btn_locate;

        public Holder(View itemView){
            super(itemView);
            wt = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.txt_msgcontent);
            btn_locate = itemView.findViewById(R.id.btn_goloaction);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgmapdialog msgmapdialog = new msgmapdialog();
                    Intent intent = new Intent(runActivity, msgmapdialog.getClass());
                    intent.putExtra("lat",arr_msg.get(getAdapterPosition()).loaction.latitude);
                    intent.putExtra("lng",arr_msg.get(getAdapterPosition()).loaction.longitude);
                    runActivity.startActivity(intent);
                }
            });
        }
    }



}