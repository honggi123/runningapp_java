package com.example.myapplication.Profile;

import android.content.Context;
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

    public myfriendlist_Adapter(String f_id, ArrayList<User> arr_id) {
        this.arr_myfriend = arr_id;
        this.f_id = f_id;
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

        }
    }
}
