package com.example.myapplication.viewact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Coaching;
import com.example.myapplication.R;
import com.example.myapplication.Run.Runbeforetimer_Activity;

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
        holder.coachname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Runbeforetimer_Activity.class);
                intent.putExtra("coachjson",arr_coach.get(position).getChoachingjson());
                context.startActivity(intent);
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
                coachname = itemView.findViewById(R.id.coachname);
                description = itemView.findViewById(R.id.coaching_des);

            }
    }

}
