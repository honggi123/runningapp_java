package com.example.myapplication.Join;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.Request.IdchkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {


    private static String IP_ADDRESS = "";
    private String mID, mPW;
    private EditText mEditId;
    private EditText mEditPw;
    private EditText mEditPwchk;

    TextView showpwsame;
    TextView text_rule;
    TextView text_diffpassword;
    TextView textemailfail;
    TextView show_pinfopolicy;
    TextView pwchktxt;
    TextView textemailsuc;
    TextView show_personalprivacy;
    CheckBox rule_checkBox;

    RadioGroup genderGroup;
    RadioButton m_btn,f_btn;

    Button btn_phonechk;
    Button btn_signup;
    Button btn_idchk;
    int idchk;

    String email;
    String gender;
    Boolean pwsame;
    Boolean pwchk;
    Boolean emailchk;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        mEditId = (EditText) findViewById(R.id.edit_signid);
        mEditPw = (EditText) findViewById(R.id.edit_signpw);
        mEditPwchk = (EditText) findViewById(R.id.edit_signpwchk);
        btn_signup = (Button) findViewById(R.id.btn_joinnext);
        btn_idchk = (Button) findViewById(R.id.btn_idchk);
        showpwsame = (TextView) findViewById(R.id.text_idsuccess);
        text_rule = findViewById(R.id.textrule);
        rule_checkBox = findViewById(R.id.rule_checkBox);
        text_diffpassword = findViewById(R.id.text_diffpassword);
        genderGroup = findViewById(R.id.genderGroup);
        m_btn = findViewById(R.id.btn_male);
        f_btn = findViewById(R.id.btn_female);
        pwchktxt = findViewById(R.id.txt_pwchk);
        show_pinfopolicy = findViewById(R.id.show_personprivacy);
        show_personalprivacy = findViewById(R.id.show_personprivacy);
        // 회원가입 항목 체크에 쓰일 sharedpref
        sharedPreferences = getSharedPreferences("join", MODE_PRIVATE);

        // 비밀 번호 조합 확인
        mEditPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = mEditPw.getText().toString();
                //대소문자 구분 숫자 특수문자  조합 9 ~ 12 자리
                String pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";

                if(Pattern.matches(pwPattern,pw)){
                    pwchktxt.setVisibility(View.GONE);
                    pwchk = true;
                }else{
                    pwchktxt.setVisibility(View.VISIBLE);
                    pwchk = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 비밀번호 일치 확인
        mEditPwchk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showpwsame.setVisibility(View.INVISIBLE);
                text_diffpassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = mEditPw.getText().toString();
                String repw = mEditPwchk.getText().toString();
                if (pw.equals(repw)) {
                    showpwsame.setVisibility(View.VISIBLE);
                    pwsame = true;
                } else {
                    text_diffpassword.setVisibility(View.VISIBLE);
                    pwsame = false;
                }

            }
        });


        // 아이디 값 변경될때
        mEditId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idchk = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 아이디 중복검사
        btn_idchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mID = mEditId.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("json", response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean haveid = jsonObject.getBoolean("haveid");
                            if (haveid) { // 중복된 회원 아이디가 있는 경우
                                Toast.makeText(JoinActivity.this, "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                                idchk = 0;
                            } else { // 중복된 회원 아이디가 없는 경우
                                Toast.makeText(JoinActivity.this, "중복된 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JoinActivity.this);
                                idchk = 1;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청을 함
                IdchkRequest idchkRequest = new IdchkRequest(mID,null, Request.Method.POST, responseListener);
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                queue.add(idchkRequest);
            }
        });

        // 성별 입력
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.btn_male){
                    gender = "m";
                }else if(checkedId == R.id.btn_female){
                    gender = "f";
                }
            }
        });



        // 이용약관 확인
        text_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(JoinActivity.this);
                dlg.setTitle("이용약관"); //제목
                dlg.setMessage(txt); // 메시지
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //토스트 메시지
                    }
                });
                dlg.show();
            }
        });
        // 개인 정보 처리 방침 보여주기
        show_personalprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(JoinActivity.this);
                dlg.setTitle("개인 정보 처리"); //제목
                dlg.setMessage(personal_privacy); // 메시지
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //토스트 메시지
                    }
                });
                dlg.show();
            }
        });

        // 회원가입 버튼 누를경우
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 입력된 정보를 string으로 가져오기
                mID = mEditId.getText().toString();
                mPW = mEditPw.getText().toString();

                // 이용약관 확인 체크박스 체크여부
                if (rule_checkBox.isChecked()) {
                        if (idchk == 1 && pwchk && pwsame && gender != null) { // 아이디 중복검사 후 동일한 아이디가 없을 경우, 비밀번호 비밀번호 확인이 일치한경우
                                Intent intent = new Intent(JoinActivity.this,EmailChkActivity.class);
                                intent.putExtra("id",mID);
                                intent.putExtra("pw",mPW);
                                intent.putExtra("gender",gender);
                                startActivity(intent);
                        } else {      // 요구사항이 충족되지 않았을때
                            Toast.makeText(JoinActivity.this, "필수사항들을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                } else {  // 이용약관 체크 안 했을때
                    Toast.makeText(JoinActivity.this, "이용약관 체크해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }

    // 약관
    String txt = "제1조 목적\n" +
            "이 약관은 러닝 리멤버가 디바이스를 통해 제공하는 모바일서비스(이하 ‘서비스’)를 이용하는 서비스 이용자(이하 ‘이용자’)와 회사간에 서비스의 이용에 관한 기본적인 사항을 규정하는 것을 목적으로 합니다.\n" +
            "\n" +
            "제2조 용어의 정의\n" +
            "① 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
            "1. \"이용자\"란 본 약관에 따라 이용계약을 체결하고, 회사가 제공하는 모든 콘텐츠 및 제반 서비스를 이용하는 회원을 말합니다. \n" +
            "2. \"콘텐츠\"란 디바이스 등을 통하여 이용할 수 있도록 회사가 제공하는 모든 서비스와 관련되어 제작 및 내용물 일체를 말합니다.\n" +
            "3. \"디바이스\"란 콘텐츠를 다운로드받거나 설치하여 사용할 수 있는 휴대전화, TV, 디지털 기기 등 회사가 제공하는 앱이나 웹사이트, 프로그램을 설치하여 회사의 서비스에 접속할 수 있는 기기를 말합니다. \n" +
            "4. \"서비스\"란 유무선네트워크를 통하여 이용자가 콘텐츠를 이용할 수 있도록 회사가 제공하는 서비스 일체를 말합니다.\n" +
            "5. \uFEFF“그룹 만들기”란 다수의 참가자가 정해진 기간 동안 회사가 정하는 바에 따라 다양한 목표를 도전하는 서비스를 말합니다.\n" +
            "6. “러닝 통계보기”란 내가 직접 러닝을 한 정보를 활용해 시각화해서 보여주는 서비스를 말합니다.\n" +
            "7. “러닝 시작” 란 직접 러닝을 하면서 위치 정보를 활용해 거리를 측정하고, 시간, 칼로리, 페이스를 측정해 실시간을 확인 할 수 있는 서비스입니다.\n" +
            "\n" +
            "② 이 약관에서 사용하는 용어의 정의는 본 조 제1항에서 정하는 것을 제외하고는 관계법령 및 서비스별 정책에서 정하는 바에 의하며, 이에 정하지 아니한 것은 일반적인 상관례에 따릅니다. \n" +
            "\n" +
            "제3조 약관의 효력 및 적용과 변경 \n" +
            "① 이 약관은 회사에서 운영하는 각 서비스 초기화면에 온라인으로 공시함으로써 효력이 발생합니다. 또한, 회사의 애플리케이션을 다운로드 받거나 콘텐츠 및 네트워크 서비스를 이용하면서 본 약관의 동의 버튼을 클릭하여 콘텐츠 실행 시, 본 약관에 동의한 것으로 간주합니다. 약관에 동의한 시점부터 약관의 적용을 받고 약관의 변경이 있을 경우에는 변경의 효력이 발생한 시점부터 변경된 약관의 적용을 받습니다.\n" +
            "② 이 약관에 동의하는 것은 정기적으로 콘텐츠를 실행하여 약관의 변경사항을 확인하는 것에 동의하는 것을 의미합니다.\n" +
            "③ 회사는 관계 법령 또는 상관습에 위배하지 않는 범위에서 이 약관을 개정할 수 있습니다.\n" +
            "④ 회사는 필요하다고 인정되는 경우 이 약관을 변경할 수 있습니다. 회사는 약관이 변경되는 경우에 변경된 약관의 내용과 적용일을 정하여, 적용일 7일전 서비스 초기화면에 온라인으로 공시합니다. 다만, 이용자에게 불리하게 약관 내용을 변경하는 경우에는 적용일로부터 30일전 서비스 초기화면에 온라인으로 공시하고 콘텐츠 내 네트워크 서비스 접속 시 확인할 수 있게 공지합니다. 변경된 약관은 공시하거나 고지한 적용일로부터 효력이 발생합니다.\n" +
            "⑤ 회원은 변경된 약관에 대해 거부할 권리가 있습니다. 본 약관의 변경에 대해 이의가 있는 회원은 서비스 이용을 중단하고 이용계약을 해지(회원탈퇴)할 수 있습니다. \n" +
            "회사가 전항에 따라 변경된 약관을 공시 또는 고지하면서 회원에게 기간 내에 의사표시를 하지 않으면 변경된 약관에 동의한 것으로 본다는 뜻을 명확하게 따로 공시 또는 고지하였음에도 회원이 명시적으로 거부의사를 표명하지 아니한 경우 회원이 변경된 약관에 동의한 것으로 간주합니다.\n" +
            "⑥ 기본적으로 이용자는 회사 약관 이용에 최초 1회 동의 시 이후 회사에서 서비스되는 콘텐츠를 별도의 동의 없이 이용할 수 있습니다. 다만, 연령확인이 필요한 서비스 또는 별도의 법령에서 정하는 바에 따라 본인 확인 및 동의절차를 거쳐야 하는 콘텐츠의 경우는 그러하지 않습니다.\n" +
            "⑦ 이 약관은 대한민국 내에서만 유효합니다.\n" +
            "\n" +
            "제4조 약관 외 준칙 \n" +
            "본 약관에 명시하지 아니한 사항에 대해서는 별도의 약관 혹은 정책을 둘 수 있으며, 약관 및 정책은 정부가 제정한 관계 법령 또는 상관례에 따릅니다.\n" +
            "\n" +
            "제2장 이용계약\n" +
            "제5조 개인정보의 보호 및 사용\n" +
            "① 회사는 관계 법령이 정하는 바에 따라 이용자의 개인정보를 보호하기 위해 노력하며, 개인정보의 보호 및 사용에 대해서는 관련 법령 및 회사의 개인정보취급방침에 따릅니다. 단, 회사의 공식 사이트 이외의 링크된 사이트에서는 회사의 개인정보취급방침이 적용되지 않습니다. \n" +
            "② 서비스의 특성에 따라 이용자의 별명, 사진 등 자신을 소개하는 내용이 공개될 수 있습니다.\n" +
            "③ 회사는 본인확인을 위해 필요한 경우, 이용자에게 그 이유(용도)를 고지하고 이용자의 신분증 사본 또는 이에 갈음하는 증서를 요구할 수 있습니다. 회사는 이를 미리 고지한 목적 이외로 사용할 수 없으며, 목적 달성 시 지체 없이 파기합니다.\n" +
            "④ 회사는 관계 법령에 의해 관련 국가기관 등의 요구가 있는 경우를 제외하고는 이용자의 개인정보를 본인의 승낙 없이 타인에게 제공하지 않습니다.\n" +
            "⑤ 회사는 이용자의 귀책사유로 인해 노출된 이용자의 계정정보를 비롯한 모든 정보에 대해서 일체의 책임을 지지 않습니다.\n" +
            "⑥ 회사는 정보통신망 이용촉진 및 정보보호 등에 관한 법률 및 그 시행령에 따라 대통령령으로 정하는 기간 동안 이용하지 않는 이용자의 개인정보는 대통령령으로 정하는 바에 따라 개인정보의 파기 등 필요한 조치를 취합니다.\n" +
            "제6조 개인정보의 관리 및 변경\n" +
            "회원은 본 서비스의 이용을 위해 자신의 개인정보를 성실히 관리해야 하며 개인정보에 변동사항이 있을 경우 이를 변경해야 합니다. 회원의 개인정보변경이 지연되거나 누락되어 발생되는 손해는 회원의 책임으로 합니다. \n" +
            "\n" +
            "제3장 계약당사자의 의무\n" +
            "\n" +
            "제 7조 회사의 의무\n" +
            "① 회사는 관련법과 본 약관에서 금지하는 행위를 하지 않으며, 계속적이고 안정적으로 서비스를 제공하기 위하여 최선을 다하여 노력합니다.\n" +
            "② 회사는 이용자의 개인정보보호를 위한 보안의무를 게을리하지 않습니다.\n" +
            "③ 회사는 이용자로부터 제기되는 의견이나 불만이 정당하다고 객관적으로 인정될 경우에는 적절한 절차를 거쳐 즉시 처리하여야 합니다. 다만, 즉시 처리가 곤란한 경우에는 이용자에게 그 사유와 처리 일정을 통보 하여야 합니다.\n" +
            "\n" +
            "제 8조 이용자의 의무\n" +
            "① 이용자는 본 약관에서 규정하는 사항과 기타 회사가 정한 제반 규정, 회사가 공지하는 사항을 준수하여야 합니다. 또한 이용자는 회사의 업무에 방해가 되는 행위, 회사의 명예를 손상시키는 행위를 해서는 안됩니다.\n" +
            "② 이용자는 청소년보호법 등 관계 법령을 준수하여야 합니다. 이용자가 청소년 보호법 등 관계 법령을 위반한 경우는 해당 법령에 의거 처벌을 받게 됩니다.\n" +
            "③ 이용자는 회사의 사전 승낙 없이 서비스를 이용하여 영업 활동을 할 수 없으며, 그 영업 활동의 결과에 대한 책임은 이용자에게 있습니다. 또한 이용자가 이와 같은 영업 활동으로 회사에 손해를 끼칠 경우, 회사는 해당 이용자에 대해 서비스 이용 제한 및 적법한 절차를 거쳐 손해배상 등을 청구할 수 있습니다.\n" +
            "\n" +
            "제 9조 저작권 등의 귀속\n" +
            "① 서비스 내 회사가 제작한 콘텐츠에 대한 저작권 기타 지적재산권은 회사의 소유입니다.\n" +
            "② 이용자는 회사가 제공하는 서비스를 이용함으로써 얻은 정보 중 회사 또는 해당 정보의 제공업체에 지적재산권이 귀속된 정보를 회사 또는 제공업체의 사전 승낙 없이 복제, 전송, 출판, 배포, 방송 기타 방법에 의하여 영리목적으로 이용하거나 제3자에게 이용하게 하여서는 안 됩니다.\n" +
            "③ 이용자는 서비스 내에서 보여지거나 서비스와 관련하여 이용자 또는 다른 이용자가 서비스를 통해 업로드 또는 전송하는 대화 텍스트를 포함한 커뮤니케이션, 이미지, 사운드 및 모든 자료 및 정보(이하 ‘이용자콘텐츠’ )에 대하여 회사가 다음과 같은 방법과 조건으로 이용하는 것을 허락합니다.\n" +
            "1. 해당 이용자콘텐츠를 이용하거나 편집 형식의 변경 및 기타의 방법으로 변형하는 것(공표, 복제, 공연, 전송, 배포, 방송, 2차적저작물 작성 등 어떠한 형태로든 이용 가능하며 이용기간과 지역에는 제한이 없음).>\n" +
            "2. 이용자콘텐츠를 제작한 이용자의 사전 동의 없이 거래를 목적으로 이용자콘텐츠를 판매, 대여, 양도하지 않음.\n" +
            "④ 서비스 내에서 보여지지 않고 서비스와 일체화 되지 않은 이용자의 콘텐츠(예컨대, 일반게시판 등에서의 게시물)에 대하여 회사는 이용자의 명시적 동의가 없이는 상업적으로 이용하지 않으며, 이용자는 언제든지 이러한 이용자의 콘텐츠를 삭제할 수 있습니다.\n" +
            "⑤ 회사는 이용자가 게시하거나 등록하는 서비스 내의 게시물 또는 게시 내용에 대해 이 약관에서 정하는 금지행위에 해당된다고 판단하는 경우 사전 통지 없이 이를 삭제하거나 이동 또는 등록을 거부할 수 있습니다.\n" +
            "⑥ 회사가 운영하는 게시판 등에 게시된 정보로 인하여 법률상 이익이 침해된 이용자는 회사에게 당해 정보의 삭제 또는 반박 내용의 게재를 요청할 수 있습니다. 이 경우 회사는 신속하게 필요한 조치를 취하고 이를 신청인에게 통지합니다.\n" +
            "⑦ 상기 ③은 회사가 서비스를 운영하는 동안 유효하며 이용자의 해지 또는 회원 탈퇴 후에도 지속적으로 적용됩니다.\n" +
            "제 10조 정보의 제공 및 광고 게재\n" +
            "① 회사는 본 서비스 등을 유지하기 위하여 광고를 게재할 수 있으며, 이용자는 서비스 이용 시 노출되는 광고게재에 대하여 동의합니다.\n" +
            "② 회사가 제공하는, 제3자가 주체인, 제1항의 광고에 이용자가 참여하거나 교신 또는 거래를 함으로써 발생하는 손실 또는 손해에 대해서 회사는 어떠한 책임도 부담하지 않습니다.\n" +
            "③ 회사는 서비스 개선 및 이용자 대상 서비스 소개 등을 위한 목적으로 이용자 개인에 대한 추가정보를 요구할 수 있으며, 동 요청에 대해 이용자는 승낙하여 추가정보를 제공하거나 거부할 수 있습니다.\n" +
            "④ 회사는 이용자의 사전 동의 하에 이용자로부터 수집한 개인정보를 활용하여 제1항의 광고 및 제3항의 정보 등을 제공하는 경우 SMS, 스마트폰 알림, e-mail을 활용하여 발송할 수 있으며, 이용자는 원하지 않을 경우 언제든지 수신을 거부할 수 있습니다.\n" +
            "제4장 서비스 일반\n" +
            "제11조 서비스의 제공\n" +
            "① 회사는 다음과 같은 서비스를 제공합니다.\n" +
            "1. 이용자의 위치정보 활용 러닝 계측 정보 저장 및 제공\n" +
            "2. \uFEFF내 러닝 통계 보여주기\n" +
            "3. \uFEFF커뮤니티 서비스\n" +
            "4. 기타 회사가 추가 개발하거나 다른 회사와의 제휴계약을 통해 이용자에게 제공하는 일체의 서비스\n" +
            "② \uFEFF이용자가 본 약관에 대한 동의 또는 회사의 콘텐츠를 다운로드하거나 네트워크를 이용하여 콘텐츠를 이용하는 시점에 서비스 이용계약이 성립한 것으로 봅니다. 단, 일부 서비스의 경우 회사의 필요에 따라 지정된 일자부터 서비스를 개시할 수 있습니다..\n" +
            "③ 회사는 이용자에게 서비스를 제공할 수 있고, 서비스를 제공함에 있어 본 약관에 정하고 있는 서비스를 포함하여 기타 부가적인 서비스를 함께 제공할 수 있습니다.\n" +
            "④ 회사는 특정 서비스에 대해서는 유료로 제공할 수 있습니다.\n" +
            "⑤ 회사는 이용자의 등급을 구분하여 이용시간, 이용회수, 제공 서비스의 범위 등을 세분화하여 이용에 차등을 둘 수 있습니다.\n" +
            "⑥ 회사는 서비스를 제공함에 있어 회원에게 본 약관 외에 별도 서비스별 약관의 체결을 요구할 수 있습니다. 별도의 약관이 요구되는 서비스는 회원이 해당 서비스의 약관에 동의한 후 이용신청을 하고 그러한 내용에 대해 회사가 승인함으로써 이용할 수 있습니다.\n" +
            "제12조 서비스의 이용\n" +
            "① 회사는 서비스를 업무상 또는 기술상 특별한 지장이 없는 한 연중 무휴, 1일 24시간 제공하는 것을 원칙으로 합니다. 단, 시스템 정기점검, 서버의 증설 및 교체, 네트워크의 불안정 등 운영상 필요하다고 판단되는 경우에는 일정기간 동안 서비스를 일시 중지할 수 있으며 이 경우 회사는 사전에 이를 각 서비스의 초기화면 또는 콘텐츠 내 공지사항에 공지합니다. \n" +
            "② 본 조 제1항에도 불구하고 회사는 긴급하고 부득이한 사유로 인해 예고 없이 일시적으로 서비스를 제한하거나 중단할 수 있으며, 이 경우 회사는 사후에 이를 공지할 수 있습니다. \n" +
            "③ 회사는 서비스의 안정적인 제공을 위하여 필요할 경우 정기점검을 실시 할 수 있으며, 정기점검의 일정과 시간은 각 서비스의 초기화면 또는 콘텐츠 내 공지사항에 공지합니다.\n" +
            "④ 회사는 전시, 사변, 천재지변 또는 이에 준하는 국가비상상태 등 회사가 통제할 수 없는 상황이나 정전, 서비스 설비의 장애 또는 서비스 이용폭주, 기간통신사업자의 설비 보수 또는 점검 등으로 인하여 정상적인 서비스 제공이 불가능하다고 판단되는 경우 서비스의 전부 또는 일부를 중지할 수 있으며, 이 경우 그 사유와 기간을 사전 또는 사후에 각 서비스의 초기화면 또는 콘텐츠 내 공지사항을 통하여 이용자에게 공지합니다.\n" +
            "⑤ 회사는 디바이스를 위한 전용 애플리케이션 또는 네트워크를 이용하여 서비스를 제공하며, 이용자는 애플리케이션을 다운로드하여 설치하거나 네트워크를 이용하여 무료로 서비스를 이용할 수 있습니다. 단, 회사는 특정 서비스는 유료로 제공할 수 있으며, 이 경우 이용자는 회사가 정하는 가격으로 해당 유료서비스를 구매한 후 디바이스를 위한 전용 애플리케이션을 다운로드하여 설치하거나 네트워크를 이용하여 서비스를 이용할 수 있습니다. 회사가 서비스 내에서 유료로 제공하는 인앱 유료콘텐츠는 서비스를 이용하는 과정에서 회사가 정하는 가격으로 해당 인앱 유료콘텐츠를 구매한 후 이용할 수 있습니다.\n" +
            "⑦ 다운로드하여 설치한 애플리케이션 또는 네트워크 서비스를 통해 이용하는 서비스의 경우 디바이스 또는 이동통신사의 특성에 맞도록 제공되며 디바이스의 변경, 번호 변경 및 해외 로밍의 경우 콘텐츠의 전부 또는 일부 기능을 이용할 수 없으며 이 경우 회사는 어떠한 책임도 부담하지 않습니다.\n" +
            "⑧ 회사는 관련 법령, 정부 정책 등에 따라 서비스 또는 이용자에 따라 서비스 이용시간을 제한할 수 있으며, 이러한 제한 사항 및 제한에 따라 발생하는 서비스 이용 관련 제반 사항에 대해서는 회사가 책임을 부담하지 않습니다.\n" +
            "\n" +
            "제13조 서비스의 변경 및 중지\n" +
            "① 회사는 운영상 또는 기술상의 필요에 따라 제공하고 있는 서비스를 변경할 수 있습니다. 변경될 서비스의 내용 및 제공일자 등에 대해서는 서비스 초기화면에서 이용자에게 사전에 통지합니다. 단, 회사가 사전에 통지할 수 없는 치명적인 버그 발생, 서버기기 결함, 긴급 보안문제 해결 등의 부득이한 사정이 있는 경우는 사후에 통지할 수 있습니다.\n" +
            "② 회사는 시장 환경의 변화, 기술적 필요, 개별 서비스 이용자의 선호 감소 기타 서비스의 기획이나 운영상 또는 회사의 긴박한 상황 등에 의해 서비스 전부를 중단할 필요가 있는 경우 30일 전에 각 서비스 초기화면 또는 콘텐츠 내 공지사항에 이를 공지하고 서비스의 제공을 중단할 수 있습니다. 이용자는 서비스 종료 시 사용기간이 남아 있지 않은 유료정액제 또는 기간제 유료아이템에 대하여 배상을 청구할 수 없습니다. 보증기간이 ‘영구’로 표시된 유료아이템이나 보증기간이 표시되지 않은 유료아이템의 경우, 서비스 중단 공지 시 공지된 서비스 종료일까지를 아이템의 이용 기간으로 봅니다.\n" +
            "③ 회사는 다음 각호에 해당하는 경우 서비스의 전부 또는 일부를 제한하거나 중지할 수 있습니다.\n" +
            "1. 전시, 사변, 천재지변 또는 국가비상사태 등 불가항력적인 사유가 있는 경우\n" +
            "2. 정전, 제반 설비의 장애 또는 이용량의 폭주 등으로 정상적인 서비스 이용에 지장이 있는 경우\n" +
            "3. 서비스용 설비의 보수 등 공사로 인한 부득이한 경우\n" +
            "4. 회사의 제반 사정으로 서비스를 할 수 없는 경우\n" +
            "④ 회사는 서비스의 변경 및 중지로 발생하는 문제에 대해서는 책임을 지지 않습니다.\n" +
            "\n" +
            "제 14조 서비스 이용 제한\n" +
            "① 이용자는 다음 각 호에 해당하는 행위를 하여서는 안되며, 해당 행위를 하는 경우에 회사는 이용자의 서비스 이용제한, 관련 정보(글, 사진, 영상 등) 삭제 및 기타의 조치를 포함한 이용제한 조치를 가할 수 있습니다.\n" +
            "1. 각종 신청, 변경, 등록 시 허위의 내용을 등록하거나, 타인을 기망하는 행위\n" +
            "2. 타인의 정보를 도용한 행위\n" +
            "3. 회사로부터 특별한 권리를 받지 않고 회사의 프로그램을 변경하거나, 회사의 서버를 해킹하거나 웹사이트 또는 게시된 정보의 일부분 또는 전체를 임의로 변경하거나, 회사의 서비스를 비정상적인 방법으로 사용하는 행위\n" +
            "4. 회사 프로그램상의 버그를 악용하거나 서비스와 관련된 설비의 오작동이나 정보 등의 파괴 및 혼란을 유발시키는 컴퓨터바이러스 감염자료를 등록 또는 유포하는 행위\n" +
            "5. 관련 법령에 의하여 그 전송 또는 게시가 금지되는 정보(컴퓨터프로그램) 또는 컴퓨터소프트웨어, 하드웨어, 전기통신 장비의 정상적인 가동을 방해, 파괴할 목적으로 고안된 소프트웨어 바이러스, 기타 다른 컴퓨터 코드, 파일, 프로그램을 포함하고 있는 자료를 전송, 게시, 유포, 사용하는 행위\n" +
            "6. 회사 서비스 운영을 고의로 방해하거나 서비스의 안정적 운영을 방해할 수 있는 정보 및 수신자의 명시적인 수신거부 의사에 반하여 광고성 정보를 전송하는 행위\n" +
            "7. 정상적이지 아니한 방법으로 사이버 자산(ID, 쿠폰, 포인트, 배지 ,리워드 등)을 취득, 양도 또는 매매하는 행위\n" +
            "8. 서비스에 위해를 가하거나 서비스를 고의로 방해하는 행위\n" +
            "9. 회사의 사전 승낙 없이 서비스를 이용하여 영업활동을 하는 행위\n" +
            "10. 본 서비스를 통해 얻은 정보를 회사의 사전 승낙 없이 서비스 이용 외의 목적으로 복제하거나, 이를 출판 및 방송 등에 사용하거나, 제3자에게 제공하는 행위\n" +
            "11. 타인의 특허, 상표, 영업비밀, 저작권, 기타 지적재산권을 침해하는 내용을. 전송, 게시 또는 \n" +
            "기타의 방법으로 타인에게 유포하는 행위\n" +
            "12. 청소년보호법 또는 법에 위반되는 저속, 음란한 내용의 정보, 문장, 도형, 음향, 동영상을 전송, 게시 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
            "13. 심히 모욕적이거나 개인신상에 대한 내용이어서 타인의 명예나 프라이버시를 침해할 수 있는 \n" +
            "내용을 전송, 게시 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
            "14. 다른 이용자를 희롱 또는 위협하거나, 특정 이용자에게 지속적으로 고통 또는 불편을 주는 행위\n" +
            "15. 음란, 저속한 정보를 교류, 게재하거나 음란사이트를 연결(링크)하거나 승인되지 않은 광고 및 홍보물을 게재하는 행위\n" +
            "16. 회사의 승인을 받지 않고 다른 사용자의 개인정보를 수집 또는 저장하는 행위\n" +
            "17. 회사의 직원이나 운영자를 가장하거나 사칭, 또는 타인의 명의를 도용하여 글을 게시하거나 메일을 발송하는 행위\n" +
            "18. 범죄와 결부된다고 객관적으로 판단되는 행위\n" +
            "19. 기타 관계 법령에 위배되는 행위 \n" +
            "② 이용자의 본 조 1항에서 금지한 행위로 인해 회사에 손해가 발생한우 이용자는 일체의 손해배상 의무를 부담합니다.\n" +
            "\n" +
            "\n" +
            "제 14조 계약 해지 및 서비스 이용 중지 등\n" +
            "① 이용자는 언제든지 서비스 이용을 원하지 않는 경우 회원탈퇴를 통해 계약을 해지할 수 있습니다. 탈퇴는 즉시 처리되며 탈퇴 시 이용자가 보유한 콘텐츠 정보는 모두 삭제되어 복구가 불가능합니다. \n" +
            "② 이용자가 다음 각 호 또는 제8조, 제15조 1항, 각 서비스에서 별도로 정한 운영 정책 위반에 해당하는 행위를 한 경우, 이용계약을 해지하거나 또는 기간을 정하여 서비스 이용을 중지 할 수 있습니다.\n" +
            "1. 서비스 신청 시에 허위 내용을 등록한 경우 \n" +
            "2. 서비스 운영을 고의로 방해한 경우\n" +
            "3. 타인의 서비스 아이디 및 비밀번호를 도용한 경우 \n" +
            "4. 서비스의 안정적 운영을 방해할 목적으로 다량의 정보를 전송하거나 광고성 정보를 전송하는 경우\n" +
            "5. 회사 및 이용자에게 피해를 유발시키는 컴퓨터 바이러스 프로그램 등을 유포하는 경우\n" +
            "6. 그 외 서비스정책에 위배되는 사항\n" +
            "③ 전항의 규정에 따라 이용계약을 해지하거나 중단하는 경우 이용자는 다운로드 받은 콘텐츠를 사용할 수 없으며, 기 지급한 데이터 통화료 및 월정액 서비스 이용료 등을 반환 받을 수 없습니다.\n" +
            "\n" +
            "제8장 손해배상 및 면책\n" +
            "제 15조 손해 배상\n" +
            "①    ① 회사는 회사가 제공하는 무료 서비스 이용과 관련하여 이용자에게 발생한 어떠한 손해에 대해서도 책임을 지지 않습니다.\n" +
            "②    이용자가 본 약관의 규정을 위반함으로 인하여 회사에 손해가 발생하게 되는 경우, 이 약관을 위반한 이용자는 회사에 발생하는 모든 손해를 배상할 책임이 있습니다.\n" +
            "③    이용자가 서비스를 이용함에 있어 행한 불법행위나 본 약관 위반행위로 인하여 회사가 당해 이용자 이외의 제3자로부터 손해배상 청구 또는 소송을 비롯한 각종 이의제기를 받는 경우 당해 이용자는 자신의 책임과 비용으로 회사를 면책시켜야 하며, 회사가 면책되지 못한 경우 당해 이용자는 그로 인하여 회사에 발생한 모든 손해를 배상할 책임이 있습니다.\n" +
            "④    회사가 개별서비스 제공자와 제휴 계약을 맺고 이용자에게 개별서비스를 제공함에 있어 이용자가 개별서비스 이용약관에 동의를 한 뒤 개별서비스 제공자의 귀책 사유로 인해 손해가 발생할 경우 관련 손해에 대해서는 개별서비스 제공자가 책임을 집니다.\n" +
            "제 22조 면책조항\n" +
            "①     회사는 천재지변 또는 이에 준하는 불가항력으로 인하여 서비스를 제공할 수 없는 경우에는 서비스 제공에 관한 책임이 면제됩니다.\n" +
            "②     회사는 서비스용 설비의 보수, 교체, 정기점검, 공사 등 부득이한 사유로 발생한 손해에 대한 책임이 면제됩니다.\n" +
            "③     회사는 이용자의 귀책사유로 인한 서비스 이용의 장애에 대하여는 책임이 면제됩니다. 단, 이용자에게 부득이하거나 정당한 사유가 있는 경우에는 그러하지 아니합니다.\n" +
            "④     회사는 이동통신 사업자가 네트워크서비스를 중지하거나 정상적으로 제공하지 아니하여 손해가 발생한 경우에는 책임이 면제됩니다.\n" +
            "⑤     회사는 이용자의 디바이스 환경으로 인하여 발생하는 제반 문제 또는 회사의 귀책사유가 없는 네트워크 환경으로 인하여 발생하는 문제에 대해서는 일체 책임을 지지 않습니다.\n" +
            "⑥     회사는 이용자가 서비스와 관련하여 게재한 정보, 자료, 사실의 신뢰도, 정확성 등의 내용에 관하여는 책임을 지지 않습니다.\n" +
            "⑦     회사는 이용자간 또는 이용자와 제3자 상호간에 서비스를 매개로 하여 거래 등을 한 경우에는 책임이 면제됩니다.\n" +
            "⑧     회사는 무료로 제공되는 서비스 이용과 관련하여 관련법에 특별한 규정이 없는 한 책임을 지지 않습니다.\n" +
            "⑨     회사는 이용자가 서비스를 이용하여 기대하는 이익을 얻지 못하거나 상실한 것에 대하여 책임을 지지 않습니다.\n" +
            "⑩     회사는 이용자가 신상정보를 부정확하게 기재하거나 미기재하여 손해가 발생한 경우에 대하여 책임을 부담하지 않습니다. \n" +
            "\n" +
            "제 16조 재판권 및 준거법\n" +
            "①    서비스 이용과 관련하여 회사와 이용자간에 발생한 분쟁에 대하여는 양 당사자 간의 합의에 의해 원만히 해결하여야 합니다.\n" +
            "②    이 약관에 명시되지 않은 사항이 관계법령에 규정되어 있을 경우에는 해당 규정에 따릅니다.\n" +
            "③    서비스 이용으로 발생한 분쟁에 대해 소송이 제기되는 경우 법령에 정한 절차에 따른 법원을 관할 법원으로 합니다.\n" +
            "\n" +
            "\n" +
            "<부칙> \n" +
            "본 약관은 2021년 8월 01일부터 시행됩니다.\n";
   String personal_privacy = "< 러닝 리멤버 >('www.example.com'이하 'www.example.com')은(는) 「개인정보 보호법」 제30조에 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.\n" +
           "\n" +
           "\n" +
           "\n" +
           "제1조(개인정보의 처리목적)\n" +
           "\n" +
           "< 러닝 리멤버 >(이)가 개인정보 보호법 제32조에 따라 등록․공개하는 개인정보파일의 처리목적은 다음과 같습니다.\n" +
           "\n" +
           "1. 개인정보 파일명 : 러닝 리멤버 개인 데이터 수집\n" +
           "개인정보의 처리목적 : 개인 정보 데이터 활용을 통한 러닝 데이터 제공\n" +
           "보유근거 : 개인 정보 활용을 통해 앱 내 서비스 제공 \n" +
           "보유기간 : 1년\n" +
           "관련법령 : 신용정보의 수집/처리 및 이용 등에 관한 기록 : 3년\n" +
           "\n" +
           "\n" +
           "제2조(개인정보의 제3자 제공)\n" +
           "\n" +
           "① < 러닝 리멤버 >은(는) 개인정보를 제1조(개인정보의 처리 목적)에서 명시한 범위 내에서만 처리하며, 정보주체의 동의, 법률의 특별한 규정 등 「개인정보 보호법」 제17조 및 제18조에 해당하는 경우에만 개인정보를 제3자에게 제공합니다.\n" +
           "\n" +
           "② < 러닝 리멤버 >은(는) 다음과 같이 개인정보를 제3자에게 제공하고 있습니다.\n" +
           "\n" +
           "1. < 러닝 리멤버 >\n" +
           "개인정보를 제공받는 자 : 러닝 리멤버\n" +
           "제공받는 자의 개인정보 이용목적 : 이메일, 비밀번호, 로그인ID, 성별\n" +
           "제공받는 자의 보유.이용기간: 1년\n" +
           "\n" +
           "\n" +
           "제3조(정보주체와 법정대리인의 권리·의무 및 그 행사방법)\n" +
           "\n" +
           "\n" +
           "\n" +
           "① 정보주체는 러닝 리멤버에 대해 언제든지 개인정보 열람·정정·삭제·처리정지 요구 등의 권리를 행사할 수 있습니다.\n" +
           "\n" +
           "② 제1항에 따른 권리 행사는러닝 리멤버에 대해 「개인정보 보호법」 시행령 제41조제1항에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며 러닝 리멤버은(는) 이에 대해 지체 없이 조치하겠습니다.\n" +
           "\n" +
           "③ 제1항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다.이 경우 “개인정보 처리 방법에 관한 고시(제2020-7호)” 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.\n" +
           "\n" +
           "④ 개인정보 열람 및 처리정지 요구는 「개인정보 보호법」 제35조 제4항, 제37조 제2항에 의하여 정보주체의 권리가 제한 될 수 있습니다.\n" +
           "\n" +
           "⑤ 개인정보의 정정 및 삭제 요구는 다른 법령에서 그 개인정보가 수집 대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.\n" +
           "\n" +
           "⑥ 러닝 리멤버은(는) 정보주체 권리에 따른 열람의 요구, 정정·삭제의 요구, 처리정지의 요구 시 열람 등 요구를 한 자가 본인이거나 정당한 대리인인지를 확인합니다.\n" +
           "\n" +
           "\n" +
           "\n" +
           "제4조(처리하는 개인정보의 항목 작성)\n" +
           "\n" +
           "① < 러닝 리멤버 >은(는) 다음의 개인정보 항목을 처리하고 있습니다.\n" +
           "\n" +
           "1< 어플약관 >\n" +
           "필수항목 : 이메일, 로그인ID, 성별\n" +
           "선택항목 :\n" +
           "\n" +
           "\n" +
           "제5조(개인정보의 파기)\n" +
           "\n" +
           "\n" +
           "① < 러닝 리멤버 > 은(는) 개인정보 보유기간의 경과, 처리목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체없이 해당 개인정보를 파기합니다.\n" +
           "\n" +
           "② 정보주체로부터 동의받은 개인정보 보유기간이 경과하거나 처리목적이 달성되었음에도 불구하고 다른 법령에 따라 개인정보를 계속 보존하여야 하는 경우에는, 해당 개인정보를 별도의 데이터베이스(DB)로 옮기거나 보관장소를 달리하여 보존합니다.\n" +
           "1. 법령 근거 :\n" +
           "2. 보존하는 개인정보 항목 : 계좌정보, 거래날짜\n" +
           "\n" +
           "③ 개인정보 파기의 절차 및 방법은 다음과 같습니다.\n" +
           "1. 파기절차\n" +
           "< 러닝 리멤버 > 은(는) 파기 사유가 발생한 개인정보를 선정하고, < 러닝 리멤버 > 의 개인정보 보호책임자의 승인을 받아 개인정보를 파기합니다.\n" +
           "\n" +
           "2. 파기방법\n" +
           "\n" +
           "전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용합니다\n" +
           "\n" +
           "\n" +
           "\n" +
           "제6조(개인정보의 안전성 확보 조치)\n" +
           "\n" +
           "< 러닝 리멤버 >은(는) 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.\n" +
           "\n" +
           "1. 정기적인 자체 감사 실시\n" +
           "개인정보 취급 관련 안정성 확보를 위해 정기적(분기 1회)으로 자체 감사를 실시하고 있습니다.\n" +
           "\n" +
           "2. 개인정보의 암호화\n" +
           "이용자의 개인정보는 비밀번호는 암호화 되어 저장 및 관리되고 있어, 본인만이 알 수 있으며 중요한 데이터는 파일 및 전송 데이터를 암호화 하거나 파일 잠금 기능을 사용하는 등의 별도 보안기능을 사용하고 있습니다.\n" +
           "\n" +
           "\n" +
           "\n" +
           "\n" +
           "제7조(개인정보 자동 수집 장치의 설치•운영 및 거부에 관한 사항)\n" +
           "\n" +
           "\n" +
           "\n" +
           "① 러닝 리멤버 은(는) 이용자에게 개별적인 맞춤서비스를 제공하기 위해 이용정보를 저장하고 수시로 불러오는 ‘쿠키(cookie)’를 사용합니다.\n" +
           "② 쿠키는 웹사이트를 운영하는데 이용되는 서버(http)가 이용자의 컴퓨터 브라우저에게 보내는 소량의 정보이며 이용자들의 PC 컴퓨터내의 하드디스크에 저장되기도 합니다.\n" +
           "가. 쿠키의 사용 목적 : 이용자가 방문한 각 서비스와 웹 사이트들에 대한 방문 및 이용형태, 인기 검색어, 보안접속 여부, 등을 파악하여 이용자에게 최적화된 정보 제공을 위해 사용됩니다.\n" +
           "나. 쿠키의 설치•운영 및 거부 : 웹브라우저 상단의 도구>인터넷 옵션>개인정보 메뉴의 옵션 설정을 통해 쿠키 저장을 거부 할 수 있습니다.\n" +
           "다. 쿠키 저장을 거부할 경우 맞춤형 서비스 이용에 어려움이 발생할 수 있습니다.\n" +
           "\n" +
           "제8조 (개인정보 보호책임자)\n" +
           "\n" +
           "① 러닝 리멤버 은(는) 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.\n" +
           "\n" +
           "▶ 개인정보 보호책임자\n" +
           "성명 :김홍기\n" +
           "직책 :개발자\n" +
           "직급 :개발자\n" +
           "연락처 :01032967526, ghdrl7526@gmail.com, 0000\n" +
           "※ 개인정보 보호 담당부서로 연결됩니다.\n" +
           "\n" +
           "▶ 개인정보 보호 담당부서\n" +
           "부서명 :\n" +
           "담당자 :\n" +
           "연락처 :, ,\n" +
           "② 정보주체께서는 러닝 리멤버 의 서비스(또는 사업)을 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리, 피해구제 등에 관한 사항을 개인정보 보호책임자 및 담당부서로 문의하실 수 있습니다. 러닝 리멤버 은(는) 정보주체의 문의에 대해 지체 없이 답변 및 처리해드릴 것입니다.\n" +
           "\n" +
           "제9조(개인정보 열람청구)\n" +
           "정보주체는 ｢개인정보 보호법｣ 제35조에 따른 개인정보의 열람 청구를 아래의 부서에 할 수 있습니다.\n" +
           "< 러닝 리멤버 >은(는) 정보주체의 개인정보 열람청구가 신속하게 처리되도록 노력하겠습니다.\n" +
           "\n" +
           "▶ 개인정보 열람청구 접수·처리 부서\n" +
           "부서명 : 러닝 리멤버\n" +
           "담당자 : 김홍기\n" +
           "연락처 : 01032967526, ghdrl7526@gmail.com, 0000\n" +
           "\n" +
           "\n" +
           "제10조(권익침해 구제방법)\n" +
           "\n" +
           "\n" +
           "\n" +
           "정보주체는 개인정보침해로 인한 구제를 받기 위하여 개인정보분쟁조정위원회, 한국인터넷진흥원 개인정보침해신고센터 등에 분쟁해결이나 상담 등을 신청할 수 있습니다. 이 밖에 기타 개인정보침해의 신고, 상담에 대하여는 아래의 기관에 문의하시기 바랍니다.\n" +
           "\n" +
           "1. 개인정보분쟁조정위원회 : (국번없이) 1833-6972 (www.kopico.go.kr)\n" +
           "2. 개인정보침해신고센터 : (국번없이) 118 (privacy.kisa.or.kr)\n" +
           "3. 대검찰청 : (국번없이) 1301 (www.spo.go.kr)\n" +
           "4. 경찰청 : (국번없이) 182 (cyberbureau.police.go.kr)\n" +
           "\n" +
           "「개인정보보호법」제35조(개인정보의 열람), 제36조(개인정보의 정정·삭제), 제37조(개인정보의 처리정지 등)의 규정에 의한 요구에 대 하여 공공기관의 장이 행한 처분 또는 부작위로 인하여 권리 또는 이익의 침해를 받은 자는 행정심판법이 정하는 바에 따라 행정심판을 청구할 수 있습니다.\n" +
           "\n" +
           "※ 행정심판에 대해 자세한 사항은 중앙행정심판위원회(www.simpan.go.kr) 홈페이지를 참고하시기 바랍니다.";


}
