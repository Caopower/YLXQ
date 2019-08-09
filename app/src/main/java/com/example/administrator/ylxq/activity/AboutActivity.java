package com.example.administrator.ylxq.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.https.HttpsUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActivityCollector.addActivity(this);
        OkHttpUtils
                .get()
                .url("http://www.weather.com.cn/data/sk/101010100.html")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG",e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG" , response);
                    }
                });
    }
}
