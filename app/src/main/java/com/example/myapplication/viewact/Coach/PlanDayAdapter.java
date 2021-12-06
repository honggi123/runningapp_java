package com.example.myapplication.viewact.Coach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class PlanDayAdapter extends RecyclerView.Adapter<PlanDayAdapter.Holder>  {


    ArrayList<String> arrday;
    Context context;




    PlanDayAdapter(ArrayList<String> arrday){
        this.arrday = arrday;
    }

    @NonNull
    @Override
    public PlanDayAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coachplanitem2, parent, false);

        return new PlanDayAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.day.setText(arrday.get(position));
    }



    @Override
    public int getItemCount() { return arrday.size(); }

    class Holder extends RecyclerView.ViewHolder {
        TextView day;
        public Holder(View itemView){
            super(itemView);
            day = itemView.findViewById(R.id.day);
        }
    }

}