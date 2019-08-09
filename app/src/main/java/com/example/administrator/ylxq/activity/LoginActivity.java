package com.example.administrator.ylxq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.entity.User;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.example.administrator.ylxq.utils.StringUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_user)
    EditText user;
    @BindView(R.id.et_userPsw)
    EditText psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ActivityCollector.addActivity(this);
    }

    @OnClick(R.id.tv_regist)
    public void gotoRegist(View v){
        startActivity(new Intent(this,RegistActivity.class));
    }

    @OnClick(R.id.tv_wangjiPsw)
    public void wjMiMa(View v){
        startActivity(new Intent(this,UpdatePswActivity.class));
        finish();
    }

    @OnClick(R.id.bt_login)
    public void login(View v){
        final String userName = user.getText().toString();
        final String userPsw = psw.getText().toString();
        if(StringUtils.isEmpty(userName)){
            ToastUtils.show("请输入手机号码");
            return;
        }
        if(StringUtils.isEmpty(userPsw)){
            ToastUtils.show("请输入密码");
            return;
        }

        login1(userName , userPsw);


    }


    public void login1(final String userName , final String userPsw){
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
                                SPUtils.put(getApplicationContext(),"isLogin" , true);
                                SPUtils.put(getApplicationContext(),"userName" , userName);
                                SPUtils.put(getApplicationContext(),"psw" , userPsw);
                                String balance = jsonObject.getString("balance");
                                ActivityCollector.finishAll();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(User user){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
