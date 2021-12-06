package com.example.myapplication.Chat.CoachUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.Profile.ProfileMenuActivity;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    ArrayList<Categoryitem> arr_chatroom;

    String f_id;
    Context context;
    ProfileMenuActivity fragment4;
    Date date = null;

    public CategoryAdapter(ArrayList<Categoryitem> arr_chatroom){
        this.arr_chatroom = arr_chatroom;
    }

    @NonNull
    @Override
    public CategoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.coach_managecategoryitem, parent, false);
        CategoryAdapter.Holder vh = new CategoryAdapter.Holder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arr_chatroom.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }



}
