package com.example.myapplication.viewact.Coach;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CoachUserAdapter extends RecyclerView.Adapter<CoachUserAdapter.Holder>  {

    ArrayList<CoachUser> arr_coachsel;
    Context context;
    int selectnum=0;

    CoachUserAdapter(ArrayList<CoachUser> arr_coachsel){
        this.arr_coachsel = arr_coachsel;
    }

    @NonNull
    @Override
    public CoachUserAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coachprofileitem, parent, false);

        return new CoachUserAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText(arr_coachsel.get(position).getName());
        holder.career.setText(arr_coachsel.get(position).getCareer());
        holder.desc.setText(arr_coachsel.get(position).getDesc());

        if(arr_coachsel.get(position).getSel()){
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }


    @Override
    public int getItemCount() { return arr_coachsel.size(); }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView career;
        TextView desc;

        public Holder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.coachname);
            career = itemView.findViewById(R.id.coachcareer);
            desc = itemView.findViewById(R.id.coachdesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < arr_coachsel.size(); i++){
                        arr_coachsel.get(i).setSel(false);
                    }
                    arr_coachsel.get(getAdapterPosition()).setSel(true);
                    selectnum = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
    public String getselid(){
        for (int i = 0; i < arr_coachsel.size(); i++){
            if(arr_coachsel.get(i).getSel()){
                 return arr_coachsel.get(i).getId();
            }
        }
        return null;
    }

    public int getSelectnum() {
        return selectnum;
    }
}