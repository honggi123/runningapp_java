package com.example.myapplication.Profile;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ImageAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

public class shoes_Adapter extends RecyclerView.Adapter<shoes_Adapter.Holder> {

    ArrayList<Shoe> arr_shoes;

    shoes_Adapter(ArrayList<Shoe> arr_shoes) {
        this.arr_shoes = arr_shoes;
        Context context;
    }

        @NonNull
            @Override
            public shoes_Adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.image_item, parent, false);

                return new shoes_Adapter.Holder(view);
            }


        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {


//            Glide.with(context)
//                    .load(arr_photopath.get(position))
//                    .into(holder.image);
        }


            @Override
            public int getItemCount() {
                return arr_shoes.size();
            }



            class Holder extends RecyclerView.ViewHolder {
                ImageView image;
                TextView name;
                TextView wearornot;
                ProgressBar progressBar;

                public Holder(View itemView) {
                    super(itemView);
                    image = itemView.findViewById(R.id.viewshoe_shoeitem);
                    name = itemView.findViewById(R.id.name_shoeitem);
                    wearornot = itemView.findViewById(R.id.wearornot_shoeitem);
                    progressBar = itemView.findViewById(R.id.bar_shoeitem);

                }
            }



    }


