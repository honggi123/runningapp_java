package com.example.myapplication.viewact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.viewact.Coach.Coaching;
import com.example.myapplication.R;
import com.example.myapplication.viewact.Coach.CoachActivity;

import java.util.ArrayList;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.Holder> {

    ArrayList<Coaching> arr_coach;
    Context context;

    CoachAdapter(ArrayList<Coaching> arr_coach){
        this.arr_coach = arr_coach;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coachingitem, parent, false);

        return new CoachAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.coachname.setText(arr_coach.get(position).getName());
        holder.description.setText(arr_coach.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coachname",holder.coachname.getText().toString());
                if(holder.coachname.getText().equals("체중 감량")){
                    Intent intent = new Intent(context, CoachActivity.class);
                    intent.putExtra("coachname","diet");
                    intent.putExtra("coach",arr_coach.get(position));
                    context.startActivity(intent);
                }else if(holder.coachname.getText().equals("레이스 준비")){
                    Intent intent = new Intent(context, CoachActivity.class);
                    intent.putExtra("coachname","race");
                    intent.putExtra("coach",arr_coach.get(position));
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() { return arr_coach.size(); }

      class Holder extends RecyclerView.ViewHolder {
            TextView coachname;
            TextView description;
            public Holder(View itemView){
                super(itemView);
                coachname = itemView.findViewById(R.id.msg);
                description = itemView.findViewById(R.id.coaching_des);
            }
    }

}
