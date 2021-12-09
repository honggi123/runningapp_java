package com.example.myapplication.Chat.CoachUser;
import com.android.volley.Request;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.Chat.Chatroom;
import com.example.myapplication.MySingleton;
import com.example.myapplication.Profile.ProfileMenuActivity;
import com.example.myapplication.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    ArrayList<Categoryitem> arrcat;

    String id;
    Context context;
    ProfileMenuActivity fragment4;
    Date date = null;
    String usercat;
    CateuserAdapter adapter;
    JSONArray jsonArray;
    ArrayList<String> arrayList = new ArrayList();
    public CategoryAdapter(ArrayList<Categoryitem> arrcat){
        this.arrcat = arrcat;
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
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(arrcat.get(position).name);




        if(arrcat.get(position).getType().equals("Add")){
            holder.name.setVisibility(View.INVISIBLE);
            holder.add.setVisibility(View.VISIBLE);

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mkgroupdialog();
                }
            });
        }else{
            holder.name.setVisibility(View.VISIBLE);
            holder.add.setVisibility(View.GONE);
        }


        if(arrcat.get(position).getClick().equals("true")){
            holder.itemView.setBackgroundColor(Color.LTGRAY);


            arrayList.clear();
            try {
                jsonArray = new JSONArray(usercat);
                for (int i = 0; i < jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.e("json.string",jsonObject.getString("category"));
                    if(jsonObject.getString("category").equals(arrcat.get(position).name)){
                        arrayList.add(jsonObject.getString("userid"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("arrsize",arrayList.size()+"");
            adapter.setArrcat(arrayList);
            adapter.notifyDataSetChanged();

        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < arrcat.size(); i++){
                     arrcat.get(i).setClick("false");
                }
                arrcat.get(position).setClick("true");
            notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrcat.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        FrameLayout add;
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);
            name = itemView.findViewById(R.id.name);
        }
    }

    public void mkgroupdialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        EditText input = new EditText(context);

        builder.setTitle("추가 할 그룹명을 입력하세요");
        builder.setView(input);
        builder.setPositiveButton(("생성"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            addcategory(id,input.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(("취소"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void addcategory(String cid,String input) throws JSONException {
            // 안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl="http://3.12.49.32/addcategory.php";

            // 파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
            try {
            JSONObject jsonObject = new JSONObject(response);
            boolean success = jsonObject.getBoolean("success");
              //  arrcat.clear();
            if(success) {
                /*
                for (int i = 0; i < arrcat.size(); i++){
                    Categoryitem categoryitem = new Categoryitem();
                    categoryitem.setName(arrcat.get(i).name);
                    arrcat.add(categoryitem);
                }
                Categoryitem categoryitem = new Categoryitem();
                categoryitem.setName(input);
                arrcat.add(categoryitem);

                Categoryitem categoryitem2 = new Categoryitem();
                categoryitem2.setType("Add");
                arrcat.add(categoryitem2);
                */

                Categoryitem categoryitem = new Categoryitem();
                categoryitem.setName(input);
                arrcat.add(arrcat.size()-1,categoryitem);

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
            }
            });

           JSONArray jsonArray = new JSONArray();
            // 요청 객체에 보낼 데이터를 추가
            for (int i = 0; i < arrcat.size()-1; i++){
                JSONObject jsonObject = new JSONObject(); //JSON 오브젝트의 body 부분
                jsonObject.put("name",arrcat.get(i).getName());
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject(); //JSON 오브젝트의 body 부분
            jsonObject.put("name",input);
            jsonArray.put(jsonObject);
        Log.e("jsonarray",jsonArray.toString());

             smpr.addStringParam("cid", cid);
            smpr.addStringParam("json", jsonArray.toString());

            // 서버에 데이터 보내고 응답 요청
            RequestQueue requestQueue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
            requestQueue.add(smpr);
            }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsercat(String usercat) {
        this.usercat = usercat;
    }

    public void setAdapter2(CateuserAdapter adapter) {
        this.adapter = adapter;
    }

    public void setArrcat(ArrayList<Categoryitem> arrcat) {
        this.arrcat = arrcat;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }
}
