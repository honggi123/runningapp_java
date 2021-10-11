package com.example.myapplication.Profile;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class myfriendlist_Adapter extends RecyclerView.Adapter<myfriendlist_Adapter.Holder> {

    ArrayList<User> arr_myfriend;
    String f_id;
    Context context;
    int fromwhere;
    int selpos;

    public myfriendlist_Adapter(String f_id, ArrayList<User> arr_id,int fromwhere) {
        this.arr_myfriend = arr_id;
        this.f_id = f_id;
        this.fromwhere = fromwhere;

    }

    @NonNull
    @Override
    public myfriendlist_Adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.myfrienditem, parent, false);
        myfriendlist_Adapter.Holder vh = new myfriendlist_Adapter.Holder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myfriendlist_Adapter.Holder holder, int position) {
        holder.viewid_frditem.setText(arr_myfriend.get(position).getId());
        if(fromwhere == 2){
            if(arr_myfriend.get(position).getSelect()){
                holder.viewid_frditem.setTextColor(Color.GREEN);
            }else{
                holder.viewid_frditem.setTextColor(Color.BLACK);
            }
        }
    }


    @Override
    public int getItemCount() {
        return arr_myfriend.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView viewid_frditem;

        public Holder(View itemView) {
            super(itemView);
            viewid_frditem = itemView.findViewById(R.id.viewid_myfrditem);

            if(fromwhere == 2){
                viewid_frditem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i = 0;i<=arr_myfriend.size()-1;i++){
                            arr_myfriend.get(i).setSelect(false);
                        }
                        selpos = getAdapterPosition();
                        arr_myfriend.get(getAdapterPosition()).setSelect(true);
                        notifyDataSetChanged();

                        Log.e("selpos",selpos+"");
                    }
                });
            }
        }
    }

    public int getselectpos(){
        return selpos;
    }

    public void notifyset(){
        notifyDataSetChanged();
    }

}
