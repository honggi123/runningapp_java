package com.example.myapplication.viewact.Coach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.Holder>  {


    ArrayList<Plan> arrweek;
    Context context;
    int daysize = 0;

    RecyclerView my_recyclerView;
    ArrayList<String> arrday;
    PlanDayAdapter planDayAdapter;

    PlanAdapter(ArrayList<Plan> arrweek){
        this.arrweek = arrweek;
    }

    @NonNull
    @Override
    public PlanAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coachplanitem, parent, false);

        return new PlanAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.weeknum.setText(arrweek.get(position).getWeek()+"주차");

        Log.e(position+"",arrweek.get(position).getStartdate());

        holder.viewplandate.setText(arrweek.get(position).getStartdate()+" ~ " +arrweek.get(position).getEnddate());
    }

    @Override
    public int getItemCount() { return arrweek.size(); }

    class Holder extends RecyclerView.ViewHolder {
        TextView weeknum;
        RecyclerView rc_weekplan;
        TextView viewplandate;

        public Holder(View itemView){
            super(itemView);
            weeknum = itemView.findViewById(R.id.weeknum);
            rc_weekplan= itemView.findViewById(R.id.rc_weekplan);
            viewplandate = itemView.findViewById(R.id.viewplandate);

            // 라사이클러뷰에 넣기
            // 어댑터 객체 생성
            arrday = new ArrayList<>();

            Log.e("daysize",daysize+"");
            for (int i = 1; i <= daysize; i++){
                arrday.add(i+"일");
            }
            planDayAdapter = new PlanDayAdapter(arrday);
            Log.e("arrday",arrday.size()+"");
            LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rc_weekplan.setLayoutManager(linearLayoutManager);
            // 어댑터 추가
            rc_weekplan.setAdapter(planDayAdapter);
        }
    }

    public void setDaysize(int daysize) {
        this.daysize = daysize;
    }
}