package com.example.administrator.ylxq.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tv_top)
    TextView toptxt;
    @BindView(R.id.iv_left)
    ImageView left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        toptxt.setText("设置");
        left.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.updatePsw)
    public void updatePsw(View view){
        startActivity(new Intent(this,UpdatePswActivity.class));
    }


    @OnClick(R.id.iv_left)
    public void fanhui(View view){
        finish();
    }

    @OnClick(R.id.bt_tuichu)
    public void tuichu(){

        AlertDialog.Builder ab = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("请选择")
                .setMessage("确定退出当前账号重新登录?")
                .setPositiveButton("当然", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SPUtils.clear(SettingActivity.this);
                        startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                        ActivityCollector.finishAll();
                    }
                })
                .setNegativeButton("不，我考虑下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        ab.show();

    }
}
