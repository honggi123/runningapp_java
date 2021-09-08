package com.example.myapplication.viewact;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class Pickdistance_Dialog  {

    Context context;
    Dialog dig;
    Button btn_setdistance;
    EditText pickerkm_distance;
    EditText pickerm_distance;

    public Pickdistance_Dialog(Context context){
        this.context = context;
    }

    public void calldialog(){
            dig=new Dialog(context);
            // 액티비티의 타이틀바를 숨긴다.
            dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dig.setContentView(R.layout.pickdistance_dialog);
            btn_setdistance = dig.findViewById(R.id.btn_settime);
            pickerkm_distance  = dig.findViewById(R.id.picker1_time);
            pickerm_distance = dig.findViewById(R.id.picker2_time);

            btn_setdistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickerkm_distance.getText().toString();
                    pickerm_distance.getText().toString();
                }
            });

            dig.show();

    }



}
