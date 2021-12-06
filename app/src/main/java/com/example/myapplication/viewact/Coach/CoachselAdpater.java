package com.example.myapplication.viewact.Coach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CoachselAdpater extends RecyclerView.Adapter<CoachselAdpater.Holder>  {


    ArrayList<Coachquestion> arr_coachsel;
    Context context;
    int selectnum=0;

    CoachselAdpater(ArrayList<Coachquestion> arr_coachsel){
        this.arr_coachsel = arr_coachsel;
    }

    @NonNull
    @Override
    public CoachselAdpater.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coachselitem, parent, false);

        return new CoachselAdpater.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.viewchoice.setText(arr_coachsel.get(position).getQustion());

        if(arr_coachsel.get(position).getSelect()){
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

    }


    @Override
    public int getItemCount() { return arr_coachsel.size(); }

    class Holder extends RecyclerView.ViewHolder {
        TextView viewchoice;

        public Holder(View itemView){
            super(itemView);
            viewchoice = itemView.findViewById(R.id.viewchoice);

            viewchoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < arr_coachsel.size(); i++){
                        arr_coachsel.get(i).setSelect(false);
                    }
                    arr_coachsel.get(getAdapterPosition()).setSelect(true);
                    selectnum = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public int getSelectnum() {
        return selectnum;
    }
}