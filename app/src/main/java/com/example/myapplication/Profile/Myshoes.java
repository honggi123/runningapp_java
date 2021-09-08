package com.example.myapplication.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class Myshoes extends AppCompatActivity {

    Button btn_addshoes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.viewmyshoes);

        Log.e("oncreate","myshoes");

        btn_addshoes = findViewById(R.id.btn_addshoes);

        btn_addshoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Myshoes.this,addshoes.class);
                startActivity(intent);
            }
        });

        super.onCreate(savedInstanceState);
    }
}
