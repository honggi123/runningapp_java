package com.example.myapplication.Chat.CoachUser;


import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.myapplication.MySingleton;
import com.example.myapplication.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CateuserAdapter extends RecyclerView.Adapter<CateuserAdapter.Holder> {

    ArrayList<String> arrcat;

    String cid;
    String uid;
    CManageActivity context;
    String usercat;

    public CateuserAdapter(ArrayList<String> arrcat) {
        this.arrcat = arrcat;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = (CManageActivity) parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.useritem, parent, false);
        CateuserAdapter.Holder vh = new CateuserAdapter.Holder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(arrcat.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(arrcat.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("arrsize",arrcat.size()+"");
        return arrcat.size();

    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_tv_name);
        }
    }

    public void setArrcat(ArrayList<String> arrcat) {
        this.arrcat = arrcat;
        notifyDataSetChanged();
        Log.e("arrsize",arrcat.size()+"");
    }


    public void request(String userid){
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://3.12.49.32/getcoachuserinfo2.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
                Log.e("resp",response);
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
            if(success) {
            // 업로드 성공

                String userinfo = jsonObject.getString("userinfo");
                Intent intent = new Intent(context,ViewUserinfo.class);
                intent.putExtra("userID",userid);
                intent.putExtra("userinfo",userinfo);
                context.startActivityForResult(intent,200);

            } else {
            // 업로드 실패
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
            }
            }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
            }
            });

            // 요청 객체에 보낼 데이터를 추가
             smpr.addStringParam("coachID", cid);
             smpr.addStringParam("userID", userid);

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
            }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
