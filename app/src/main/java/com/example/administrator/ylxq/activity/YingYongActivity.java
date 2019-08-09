package com.example.administrator.ylxq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
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

public class YingYongActivity extends AppCompatActivity {
    @BindView(R.id.tv_allQZ)
    TextView allQZ;
    @BindView(R.id.tv_allLYS)
    TextView allLYS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ying_yong);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        //请求获得全网权重和总链元素
        requestInfo();


    }

    private void requestInfo() {
        OkHttpUtils
                .get()
                .url(HttpUtils.GET_ALL_INFO)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("网络异常，请检查网络后再试");
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String lys = jsonObject.getString("totalMoney");
                            String zqz = jsonObject.getString("totalWeight");

                            allLYS.setText(StringUtils.addStr(lys));
                            allQZ.setText(StringUtils.addStr(zqz));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @OnClick(R.id.ll_choujiangRoom)
    public void chouJiang(View v){
        startActivity(new Intent(this,ChouJiangActivity.class));
    }

    @OnClick(R.id.ll_duihuanRoom)
    public void duiHuan(View v){
        startActivity(new Intent(this,DuiHuanActivity.class));
    }
}
