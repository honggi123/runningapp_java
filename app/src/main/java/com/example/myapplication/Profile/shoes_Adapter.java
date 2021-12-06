package com.example.myapplication.Profile;
import static android.content.Context.MODE_PRIVATE;

import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class shoes_Adapter extends RecyclerView.Adapter<shoes_Adapter.Holder> {

    ArrayList<Shoe> arr_shoes;
    Context context;
    View view;

    SharedPreferences loginshared;
    String mid;

    shoes_Adapter(ArrayList<Shoe> arr_shoes,Context context) {
        this.arr_shoes = arr_shoes;
        this.context = context;

        loginshared = context.getSharedPreferences("Login", MODE_PRIVATE);
        mid = loginshared.getString("id", null);
    }

        @NonNull
            @Override
            public shoes_Adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.shoesitem, parent, false);

                return new shoes_Adapter.Holder(view);
            }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.name.setText(arr_shoes.get(position).getName());
            Log.e("imgurl",arr_shoes.get(position).getImageurl());
            Glide.with(context)
                    .load(arr_shoes.get(position).getImageurl())
                    .into(holder.image);
            holder.progressBar.setMax(arr_shoes.get(position).g_distance);
            holder.progressBar.setProgress(arr_shoes.get(position).distance);

            double kmdistance = (arr_shoes.get(position).distance / 1000.00);
            holder.distance.setText(String.format("%.2f",kmdistance) + "km");
            double tkmdistance = (arr_shoes.get(position).g_distance / 1000.00);
            holder.tdistance.setText(String.format("%.2f",tkmdistance) + "km");

            Log.e("wear",arr_shoes.get(position).getWear());

            if(!Boolean.parseBoolean(arr_shoes.get(position).getWear())){
                holder.wearview.setVisibility(View.INVISIBLE);
            }else{
                holder.wearview.setVisibility(View.VISIBLE);
            }
            int nposition = position;

        }

        @Override
        public int getItemCount() { return arr_shoes.size(); }


            class Holder extends RecyclerView.ViewHolder {
                ImageView image;
                TextView name;
                ImageView wearview;
                ProgressBar progressBar;
                TextView tdistance;
                TextView distance;

                public Holder(View itemView) {
                    super(itemView);
                    image = itemView.findViewById(R.id.viewshoe_shoeitem);
                    name = itemView.findViewById(R.id.txt_writer);
                    wearview = itemView.findViewById(R.id.wear_checkitem);
                    progressBar = itemView.findViewById(R.id.bar_shoeitem);
                    distance = itemView.findViewById(R.id.distance_shoeitem);
                    tdistance = itemView.findViewById(R.id.tdistance_shoeitem);
                    view = itemView;

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                            int nposition = getAdapterPosition();

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("해당 러닝화를 장착하시겠습니까?")        // 제목 설정
                                    .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                        // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                        public void onClick(DialogInterface dialog, int whichButton){
                                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                                            wearshoerequest(mid,arr_shoes.get(nposition).shoe_id,nposition);
                                            Log.e("position",nposition+"");
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                                        // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                                        public void onClick(DialogInterface dialog, int whichButton){
                                            //원하는 클릭 이벤트를 넣으시면 됩니다.
                                        }
                                    });
                            AlertDialog dialog = builder.create();    // 알림창 객체 생성
                            dialog.show();    // 알림창 띄우기
                        }
                    });
                }

                View Viewreturn()
                {
                    return view;
                }

            }


            public void wearshoerequest(String mid,String shoe_id,int position){
                    // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
                        String serverUrl="http://3.12.49.32/wearshoe.php";

                        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
                        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                        try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        Log.e("json",jsonObject.toString());
                        if(success) {
                        // 업로드 성공
                            for(int i = 0; i<=arr_shoes.size()-1; i++){
                                arr_shoes.get(i).setWear("false");
                            }
                            arr_shoes.get(position).setWear("true");
                            notifyDataSetChanged();

                        } else {

                        }
                        } catch (Exception e) {
                        e.printStackTrace();
                        }
                        }
                        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                        });
                        Log.e("mid",mid);
                        Log.e("shoe_id",shoe_id);

                        // 요청 객체에 보낼 데이터를 추가
                        smpr.addStringParam("userID", mid);
                        smpr.addStringParam("shoe_id", shoe_id);

                        // 서버에 데이터 보내고 응답 요청
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(smpr);



                    }


    }


