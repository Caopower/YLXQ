package com.example.administrator.ylxq.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.adapter.LianYSAdapter;
import com.example.administrator.ylxq.entity.MoneyEntity;
import com.example.administrator.ylxq.entity.WeigthEntity;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZhangDanInfoActivity extends AppCompatActivity {
    @BindView(R.id.sp_list)
    Spinner sp;
    @BindView(R.id.tv_top)
    TextView top;
    @BindView(R.id.iv_left)
    ImageView left;
    @BindView(R.id.lv_info)
    ListView info;

    private String[] items = {"链元素" ,"权重"};

    private String userPhone;
    private ArrayList<Object> mList = null;
    LianYSAdapter myAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhang_dan_info);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        userPhone = (String)SPUtils.get(this,"userName","");
        mList = new ArrayList<>();
        myAdapter = new LianYSAdapter(this,mList);
        info.setAdapter(myAdapter);
        top.setText("账单");
        left.setVisibility(View.VISIBLE);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_item,R.id.tv_content,items);
        sp.setAdapter(aa);
//        sp.set
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                ToastUtils.show(items[i]);
                OkHttpUtils
                        .get()
                        .url(i == 0? HttpUtils.GET_USER_MONEY_LIST : HttpUtils.GET_WEIGTH_List)
                        .addParams("userPhone" , userPhone)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                ToastUtils.show("网络异常，请检查您的网路!");
                            }

                            @Override
                            public void onResponse(String response) {
                                addData(response , i);
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addData(String response, int position) {
        try {
            JSONArray ja = new JSONArray(response);
            if (mList.size() > 0) mList.clear();
            for (int i = 0 ; i < ja.length() ; i ++){
                JSONObject jsonObject = ja.getJSONObject(i);
                if(position == 0){
                    mList.add(new MoneyEntity(jsonObject.getInt("dealtype") , jsonObject.getString("dealamount"),jsonObject.getString("showDealTime")));
                }else {
                    mList.add(new WeigthEntity(jsonObject.getInt("dealtype"), jsonObject.getDouble("weight"), jsonObject.getString("dealtimeString")));
                }
            }
            myAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @OnClick(R.id.iv_left)
    public void fanhui(View view){
        finish();
    }
}
