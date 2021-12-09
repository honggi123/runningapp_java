package com.example.myapplication.Challenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChallengelistAdapter extends RecyclerView.Adapter<ChallengelistAdapter.Holder> {

    ArrayList<ChallengeInfo> arrayList;
    Context context;
    Fragment3 fragment3;
    SharedPreferences Challengeshared;
    viewexchallenge viewexchallenge;
    int where;
    public ChallengelistAdapter(ArrayList<ChallengeInfo> arrayList,int where) {
        this.arrayList = arrayList;
        this.where = where;
    }


    public void setfrg(Fragment3 fragment3){
        this.fragment3 = fragment3;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.challengeitem, parent, false);
        ChallengelistAdapter.Holder vh = new ChallengelistAdapter.Holder(view);
        return vh;
    }

    public void remove(int position){
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    public void modify(int position,String name){
        arrayList.get(position).setName(name);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position){
        holder.name.setText(arrayList.get(position).name);
        holder.btn_viewch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewchallenge_Activity.class);
                intent.putExtra("chinfo", arrayList.get(position));
                intent.putExtra("where", where);
                if (where == 1) {
                    context.startActivity(intent);

                } else {
                    fragment3.startActivityForResult(intent, 101);
                }
            }
            });

        double kmdistance = (arrayList.get(position).g_distance / 1000.00 );
        holder.g_distance.setText(String.format("%.2f",kmdistance));

        String newdate = timetodate(arrayList.get(position).g_date);
        holder.date.setText(arrayList.get(position).s_date +" ~ "+newdate);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView g_distance;
        ImageView btn_viewch;

        public Holder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.time_daychitem2);

            g_distance = itemView.findViewById(R.id.g_distance_chitem);
            btn_viewch = itemView.findViewById(R.id.btn_viewruninfo);
        }
    }

    public String timetodate(String olddate){
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        String newdate = null;
        try {
            date = simpleDate.parse(olddate);
            newdate =newformat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }


}
