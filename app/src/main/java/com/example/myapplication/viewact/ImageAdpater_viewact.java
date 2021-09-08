package com.example.myapplication.viewact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ImageAdapter;
import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



class ImageAdapter_viewact extends RecyclerView.Adapter<ImageAdapter_viewact.Holder> {

        ArrayList<String> arr_photopath;
        Context context;


    public ImageAdapter_viewact(ArrayList<String> arr_photopath,Context context) {
        this.arr_photopath = arr_photopath;
        this.context = context;
    }

    @NonNull
        @Override
        public ImageAdapter_viewact.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_item, parent, false);

            return new ImageAdapter_viewact.Holder(view);
        }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.e("arr",arr_photopath.get(position).toString());
        Glide.with(context)
                .load(arr_photopath.get(position))
                .into(holder.image);
    }


        @Override
        public int getItemCount() {
            return arr_photopath.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            ImageView image;

            public Holder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image_item);
            }
        }



}
