package com.example.administrator.ylxq.activity;

import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneyActivity extends AppCompatActivity {

    @BindView(R.id.tv_top)
    TextView tv_top;
    @BindView(R.id.iv_left)
    ImageView left;
    @BindView(R.id.tv_rightText)
    TextView rightText;
    @BindView(R.id.tv_userMoney)
    TextView userMoney;
    @BindView(R.id.lv_moneys)
    ListView lv_moneys;
    private String userPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        ButterKnife.bind(this);
        ActivityCollector.addActivity(this);
        userPhone = (String) SPUtils.get(this,"userName" , "");
        left.setVisibility(View.VISIBLE);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("账单详情");
        tv_top.setText("钱包");
        getUserInfo();

    }

    @OnClick(R.id.iv_left)
    public void back(View view){
        finish();
    }

    public void getUserInfo() {
        OkHttpUtils
                .get()
                .url(HttpUtils.GET_USER_INFO)
                .addParams("userPhone", userPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("网络异常，请检查您的网路！");
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jb = new JSONObject(response);
                            String balance = jb.getString("balance");
                            if(!StringUtils.isEmpty(balance)){
                                userMoney.setText(StringUtils.addStr(balance));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick(R.id.tv_rightText)
    public void info(View v){
        startActivity(new Intent(this,ZhangDanInfoActivity.class));
    }
}
