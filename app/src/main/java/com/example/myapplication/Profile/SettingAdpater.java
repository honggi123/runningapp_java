package com.example.myapplication.Profile;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class SettingAdpater extends RecyclerView.Adapter<SettingAdpater.Holder> {

    ArrayList<String> arr_set;
    Context context;
    View view;

    SettingAdpater(ArrayList<String> arr_set){
        this.arr_set = arr_set;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         view = inflater.inflate(R.layout.setting_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setname.setText(arr_set.get(position));

        switch(arr_set.get(position)){
          case  "내 정보":
              holder.btn_viewset.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(context,myinfo.class);
                      context.startActivity(intent);
                  }
              });
            break;

            case "로그아웃":
                holder.btn_viewset.setVisibility(View.INVISIBLE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentR = new Intent();
                        intentR.putExtra("logout" , true); //사용자에게 입력받은값 넣기
                        ((viewsetting)context).setResult(RESULT_OK,intentR); //결과를 저장
                        ((viewsetting)context).finish();
                    }
                });

             break;
          default:
            break;
        }



    }


    @Override
    public int getItemCount() {
        return arr_set.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView setname;
        ImageView btn_viewset;
        public Holder(View itemView) {
            super(itemView);
            setname = itemView.findViewById(R.id.setname);
            btn_viewset = itemView.findViewById(R.id.btn_viewset);
        }
    }

}
