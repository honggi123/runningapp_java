package com.example.myapplication.Profile;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Challenge.viewchallenge_Activity;
import com.example.myapplication.MainAct;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class reqfrdlist_Adapter extends RecyclerView.Adapter<reqfrdlist_Adapter.Holder> {

    ArrayList<User> arr_friend;

    String f_id;
    Context context;
    Fragment4 fragment4;

    public reqfrdlist_Adapter(String f_id, ArrayList<User> arr_id, Fragment4 fragment4){
        this.arr_friend = arr_id;
        this.f_id = f_id;
        this.fragment4 = fragment4;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reqfrinedlist_item, parent, false);
        reqfrdlist_Adapter.Holder vh = new reqfrdlist_Adapter.Holder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position){
        holder.viewid_frditem.setText(arr_friend.get(position).getId());
        holder.btn_accpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(arr_friend.get(position).getId(),position);
            }
        });

        holder.btn_notaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaccept(arr_friend.get(position).getId(),position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arr_friend.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView viewid_frditem;
        ImageView btn_accpet;
        ImageView btn_notaccept;

        public Holder(View itemView) {
            super(itemView);
            viewid_frditem = itemView.findViewById(R.id.viewid_frditem);
            btn_accpet = itemView.findViewById(R.id.btn_frdaccept);
            btn_notaccept = itemView.findViewById(R.id.btn_notfrdaccept);
        }
    }


    public void accept(String id,int position){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.143.9.214/friendaccept.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("acceptfriend");
            Log.e("json",String.valueOf(jsonObject));
            if(success) {
                arr_friend.remove(position);
                notifyDataSetChanged();
                fragment4.frdlistrequest(fragment4.mid);
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

            // 요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("fid",id);
            smpr.addStringParam("tid",f_id);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(smpr);
    }

    public void notaccept(String id,int position){

        // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.143.9.214/friendnotaccept.php";

        // 파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("json",String.valueOf(jsonObject));

                    boolean acceptfriend = jsonObject.getBoolean("acceptfriend");
                    if(!acceptfriend) {
                        arr_friend.remove(position);
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

        Log.e("fid",id);
        Log.e("tid",f_id);
        // 요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("fid",id);
        smpr.addStringParam("tid",f_id);

        // 서버에 데이터 보내고 응답 요청
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(smpr);
        RequestQueue requestQueue = MainAct.getRequestQueue();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(smpr);
        } else {
            requestQueue.add(smpr);
        }
    }

}
