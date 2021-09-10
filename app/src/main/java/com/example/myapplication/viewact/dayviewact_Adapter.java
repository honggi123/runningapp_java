package com.example.myapplication.viewact;

import android.annotation.SuppressLint;
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
import com.example.myapplication.Run.runActivity;

import java.util.ArrayList;

public class dayviewact_Adapter extends RecyclerView.Adapter<dayviewact_Adapter.Holder> {

    ArrayList<RunInfo> arr_ch;
    Context context;

    int size;

    public dayviewact_Adapter(ArrayList<RunInfo> arr_ch,int size) {
        this.arr_ch = arr_ch;
        this.size = size;
    }

    @NonNull
    @Override
    public dayviewact_Adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewdaychallengeitem, parent, false);
        dayviewact_Adapter.Holder vh = new dayviewact_Adapter.Holder(view);
        return vh;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull dayviewact_Adapter.Holder holder, int position) {

        double kmdistance = (arr_ch.get(position).distance / 1000.00);
        holder.viewdistance.setText(String.format("%.2f",kmdistance));

        String timeformat =new runActivity().TimeToFormat(arr_ch.get(position).time);
        holder.viewtime.setText(timeformat);

        holder.viewdate.setText(arr_ch.get(position).reg_date);
        holder.btn_viewruninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,viewrunact.class);
                intent.putExtra("runinfo",arr_ch.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return size;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView viewdate;
        TextView viewdistance;
        TextView viewtime;
        ImageView btn_viewruninfo;

        public Holder(View itemView) {
            super(itemView);
            viewdistance = itemView.findViewById(R.id.viewdistance_daychitem);
            viewtime = itemView.findViewById(R.id.time_daychitem);
            viewdate = itemView.findViewById(R.id.name_shoeitem);
            btn_viewruninfo = itemView.findViewById(R.id.btn_viewruninfo);
        }
    }

}
