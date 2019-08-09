package com.example.administrator.ylxq.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.adapter.WeightAdapter;
import com.example.administrator.ylxq.entity.WeigthEntity;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.BigDecimalUtils;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.example.administrator.ylxq.utils.StringUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DuiHuanActivity extends AppCompatActivity {

    @BindView(R.id.tv_shangxian)
    TextView shangxian;
    @BindView(R.id.tv_bili)
    TextView bili;
    @BindView(R.id.et_lysNum)
    EditText lysNum;
    @BindView(R.id.tv_quanzhong)
    TextView quanzhong;
    @BindView(R.id.lv_duihuan)
    ListView duihuanList;

    private WeightAdapter myAdapter = null;

    private ArrayList<WeigthEntity> mList = null;

    private  String userName;

    private  String ratio;

    private String surplusLimit;

    private String weightSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dui_huan);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        mList = new ArrayList<>();
        myAdapter = new WeightAdapter(this,mList);
        duihuanList.setAdapter(myAdapter);
        userName = (String) SPUtils.get(this, "userName", "");
        requestDHWeightInfo();
        requestWeigthInfo();

        lysNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")){
                    String result  = BigDecimalUtils.div(editable.toString() , ratio , 5);
                    quanzhong.setText("兑换的权重为："+result);
                    if(quanzhong.getVisibility() == View.INVISIBLE){
                        quanzhong.setVisibility(View.VISIBLE);
                    }
                }

                if(editable.toString().length() <= 0){
                    if(quanzhong.getVisibility() == View.VISIBLE){
                        quanzhong.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void requestDHWeightInfo() {
        OkHttpUtils
                .get()
                .url(HttpUtils.GET_WEIGTH_List)
                .addParams("userPhone" , userName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("网络异常，请检查您的网路!");
                    }

                    @Override
                    public void onResponse(String response) {
                        addData(response);
                    }
                });

    }

    private void addData(String response) {
        try {
            JSONArray ja = new JSONArray(response);
            if (mList.size() > 0 ) mList.clear();
            for (int i = 0 ; i < ja.length() ; i ++){
                JSONObject jsonObject = ja.getJSONObject(i);
                mList.add(new WeigthEntity(jsonObject.getInt("dealtype") , jsonObject.getDouble("weight") , jsonObject.getString("dealtimeString")));
            }
            myAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void requestWeigthInfo() {

        OkHttpUtils
                .get()
                .url(HttpUtils.GET_WEIGTH_INFO)
                .addParams("userPhone" , userName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("获取权重失败，请检查您的网路");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("tag" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //兑换比例
                            ratio = jsonObject.getString("ratio");
                            //个人兑换上限
                            surplusLimit = jsonObject.getString("surplusLimit");

                            weightSetId = jsonObject.getString("weightSetId");

                            shangxian.setText("个人兑换上限为\t" + surplusLimit);
                            bili.setText("链元素权重兑换比例\t" + ratio);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @OnClick(R.id.bt_duihuan)
    public void duihuan(View view){

        if(Double.parseDouble(surplusLimit) <= 0){
            ToastUtils.show("没有上限可兑换");
            return;
        }

        String money = lysNum.getText().toString();

        if(StringUtils.isEmpty(money)){
            ToastUtils.show("请输入链元素数量");
            return;
        }

        String result  = BigDecimalUtils.div(money , ratio , 5);
        Log.e("asd" , result);

        OkHttpUtils
                .get()
                .url(HttpUtils.DUIHUAN_QUANZHONG)
                .addParams("userPhone" , userName)
                .addParams("weightSetId", weightSetId)
                .addParams("money" , money)
                .addParams("weight", result)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("兑换权重失败，请检查您的网路");
                        Log.e("123" , e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("resultCode");
                            String resultMess = jsonObject.getString("resultMess");
                            if(code .equals("1001")){
                                ToastUtils.show("兑换成功");
                                lysNum.setText("");
                                if(mList.size() > 0) mList.clear();
                                requestDHWeightInfo();
                            }else if(code.equals("4001")){
                                ToastUtils.show("兑换失败");
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
