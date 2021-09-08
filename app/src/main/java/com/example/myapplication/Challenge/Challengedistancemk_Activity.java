package com.example.myapplication.Challenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class Challengedistancemk_Activity extends AppCompatActivity {

    SharedPreferences Chpref;
    EditText edit_distance;
    Button btn_comp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chdistancesel);
        btn_comp = findViewById(R.id.btn_chdistancecomp);
        edit_distance =findViewById(R.id.edit_chdistance);

        Chpref= getSharedPreferences("ch",MODE_PRIVATE);
        SharedPreferences.Editor edit = Chpref.edit();

        btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putFloat("distance",Float.parseFloat(edit_distance.getText().toString()));
                edit.commit();
                finish();
            }
        });
    }



}
