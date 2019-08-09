package com.example.administrator.ylxq.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.application.MyApplication;
import com.example.administrator.ylxq.entity.User;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.ProgressUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.example.administrator.ylxq.utils.StringUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends AppCompatActivity {

    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.et_yzm)
    EditText yzm;
    @BindView(R.id.et_psw)
    EditText psw;
    @BindView(R.id.bt_yzm)
    Button btn_yzm;
    private String userPhone;
    private String yzm_count;
    private String passWord;
    private boolean isRegist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_layout);
        ButterKnife.bind(this);
        ActivityCollector.addActivity(this);
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
                .url(HttpUtils.GET_CODE)
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
                        try {
                            JSONObject jb = new JSONObject(response);
                            int resultCode = jb.getInt("resultCode");
                            String resultMess = jb.getString("resultMess");
                            if(resultCode == 1001){
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
                            }else {
                                ToastUtils.show(resultMess);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @OnClick(R.id.bt_regist)
    public void requestZC(View v){
        yzm_count = yzm.getText().toString();
        passWord = psw.getText().toString();

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
        String mac = HttpUtils.getLocalMacAddressFromIp();
        Log.e("Tag" , mac);
        if(StringUtils.isEmpty(mac)){
            ToastUtils.show("手机Mac地址异常");
            return;
        }
        ProgressUtils.showProgress(this,"等待中...");
        OkHttpUtils
                .get()
                .url(HttpUtils.ZHUCE)
                .addParams("userPhone",userPhone)
                .addParams("authCode" , yzm_count)
                .addParams("mobileId" , mac)
                .addParams("pwd",passWord)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ProgressUtils.closeProgress();
                        ToastUtils.show(e.getMessage());
                        Log.e("tag" , e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        ProgressUtils.closeProgress();
                        Log.e("Tag" , response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String code = json.getString("resultCode");
                            if(code.equals("1001")){
                                isRegist = true;
                                ToastUtils.show("注册成功");
                                SPUtils.put(getApplicationContext(),"isLogin" , true);
                                SPUtils.put(getApplicationContext(),"userName" , userPhone);
                                SPUtils.put(getApplicationContext(),"psw" , passWord);
                                login(userPhone , passWord);
                            }else{
                                ToastUtils.show(json.getString("resultMess"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.e("123","返回监听被触发了");
        Intent i = new Intent();
        i.putExtra("isRegistCode" , isRegist);
        i.putExtra("userPhone" , userPhone);
        i.putExtra("userPsw" , passWord);
        setResult(10,i);
        finish();
    }

    public void login(final String userName , final String userPsw){
        OkHttpUtils
                .get()
                .url(HttpUtils.LOGIN)
                .addParams("userPhone",userName)
                .addParams("userPwd",userPsw)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("登录异常，请检查您的网路！");
                        Log.e("login" , e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resultMess = jsonObject.getString("resultCode");
                            if (resultMess.equals("1001")){
                                ToastUtils.show("登录成功");
                                ActivityCollector.finishAll();
                                startActivity(new Intent(RegistActivity.this,HomeActivity.class));
                            }else{
                                ToastUtils.show(jsonObject.getString("resultMess"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("loginresult" , response);
                    }
                });
    }
}
