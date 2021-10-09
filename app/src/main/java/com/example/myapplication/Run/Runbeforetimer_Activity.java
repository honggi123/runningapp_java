package com.example.myapplication.Run;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.Locale;

public class Runbeforetimer_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runbefore_timer);
        Log.e("runbefore","timer");

        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.rubbefore_countnum);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                Intent intent = new Intent(Runbeforetimer_Activity.this,runActivity.class);
                intent.putExtra("coachjson",getIntent().getStringExtra("coachjson"));
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
