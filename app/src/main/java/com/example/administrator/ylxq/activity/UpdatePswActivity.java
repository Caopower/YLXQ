package com.example.administrator.ylxq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.example.administrator.ylxq.utils.StringUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePswActivity extends AppCompatActivity {
    @BindView(R.id.tv_top)
    TextView toptxt;

    @BindView(R.id.iv_left)
    ImageView left;

    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.et_yzm)
    EditText yzm;
    @BindView(R.id.et_psw)
    EditText psw;

    @BindView(R.id.bt_yzm)
    Button btn_yzm;
    @BindView(R.id.et_psw1)
    EditText psw1;

    private String userPhone;
    private String yzm_count;
    private String passWord;
    private String passWord1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_psw);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        toptxt.setText("修改密码");
        left.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.iv_left)
    public void beark(View v){
        finish();
    }

    @OnClick(R.id.bt_yzm)
    public void getYzm(View v){
        btn_yzm.setEnabled(false);
        userPhone = phone.getText().toString();
        if (StringUtils.isEmpty(userPhone)){
            ToastUtils.show("手机号不能为空");
            return;
        }


        OkHttpUtils
                .get()
                .url(HttpUtils.GET_UPDATE_PSW_YZM)
                .addParams("userPhone",userPhone)
                .addParams("authid","I0Vt194Iq5PB")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("ABC" , response);
                        CountDownTimer cdt = new CountDownTimer(60 * 1000 , 1000) {
                            @Override
                            public void onTick(final long l) {
                                btn_yzm.setBackgroundResource(R.mipmap.regist_check1);
                                btn_yzm.setTextColor(getResources().getColor(R.color.home_botton_bgcolor));
                                btn_yzm.setText("已发送:" + (l/1000) + "s");
                            }

                            @Override
                            public void onFinish() {
                                btn_yzm.setText("重新发送");
                                btn_yzm.setBackgroundResource(R.mipmap.regist_cheke);
                                btn_yzm.setTextColor(Color.parseColor("#439dfe"));
                                btn_yzm.setEnabled(true);
                            }
                        }.start();

                    }
                });
    }


    @OnClick(R.id.bt_update)
    public void update(View v){
        yzm_count = yzm.getText().toString();
        passWord = psw.getText().toString();
        passWord1 = psw1.getText().toString();

        if (StringUtils.isEmpty(yzm_count)) {
            ToastUtils.show("请输入验证码");
            return;
        }

        if(StringUtils.isEmpty(passWord)){
            ToastUtils.show("请输入密码");
            return;
        }

        if(passWord.length() < 6){
            ToastUtils.show("键入的密码不能少于6位");
            return;
        }


        if(StringUtils.isEmpty(passWord1)){
            ToastUtils.show("再次输入的密码不能为空");
            return;
        }

        if(!passWord.equals(passWord1)){
            ToastUtils.show("两次密码输入不一致");
            return;
        }
        OkHttpUtils
                .get()
                .url(HttpUtils.UPDATE_PASSWORD)
                .addParams("userPhone",userPhone)
                .addParams("authCode" , yzm_count)
                .addParams("pwd",passWord)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("网络异常，请检查网路");
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject js = new JSONObject(response);
                            String resultCode = js.getString("resultCode");
                            String resultMess = js.getString("resultMess");
                            if(resultCode.equals("1001")){
                                ToastUtils.show("修改成功，请重新登录!");
                                SPUtils.clear(UpdatePswActivity.this);
                                startActivity(new Intent(UpdatePswActivity.this,LoginActivity.class));
                                ActivityCollector.finishAll();
                                finish();
                            }else{
                                ToastUtils.show(resultMess);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
