package com.example.myapplication.Challenge;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Profile.User;
import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LeaderBoard_Adapter extends RecyclerView.Adapter<LeaderBoard_Adapter.Holder> {

        ArrayList<User> arr_user;


    public LeaderBoard_Adapter(ArrayList<User> arr_user) {
        this.arr_user = arr_user;

        }


    @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.leaderboard_item, parent, false);
            return new Holder(view);
        }

         @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.viewid.setText(arr_user.get(position).getId());
        }


        @Override
        public int getItemCount() {
        return arr_user.size();
        }

        class Holder extends RecyclerView.ViewHolder {
        TextView viewid;

        public Holder(View itemView) {
            super(itemView);
            viewid = itemView.findViewById(R.id.leaderboardid);
        }
        }

}
