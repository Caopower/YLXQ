package com.example.administrator.ylxq.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.adapter.ChouJiangAdapter;
import com.example.administrator.ylxq.entity.ChouJiangEntity;
import com.example.administrator.ylxq.interfaces.RotateListener;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.ProgressUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.example.administrator.ylxq.utils.StringUtils;
import com.example.administrator.ylxq.utils.WheelSurfView;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChouJiangActivity extends AppCompatActivity {

    @BindView(R.id.wl_view)
    WheelSurfView wsv;
    @BindView(R.id.lv_cj)
    ListView cj;

    private int[] jilvs = new int[6];
    private ChouJiangAdapter cjAdapter = null;
    private ArrayList<ChouJiangEntity> mList = new ArrayList<>();

    //颜色
    Integer[] colors = new Integer[]{Color.parseColor("#fdcf91")
            , Color.parseColor("#fff7db")
            , Color.parseColor("#fdcf91")
            , Color.parseColor("#fff7db")
            , Color.parseColor("#fdcf91")
            , Color.parseColor("#fff7db")};

    String[] des = new String[6];

    //图标
    List<Bitmap> mListBitmap = new ArrayList<>();

    private String userPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chou_jiang);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        userPhone = (String)SPUtils.get(this,"userName" , "");
        requestCJInfo();
        cjAdapter = new ChouJiangAdapter(this,mList);
        cj.setAdapter(cjAdapter);
        requestCJJL();

        //添加滚动监听
        wsv.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                String[] split = des.split("：");
                Toast.makeText(ChouJiangActivity.this, "恭喜获得了" + des, Toast.LENGTH_SHORT).show();
                OkHttpUtils
                        .get()
                        .url(split[0].equals("链元素") ? HttpUtils.UPDATE_CJ_LYS : HttpUtils.UPDATE_CJ_QZ)
                        .addParams("userPhone" , userPhone)
                        .addParams(split[0].equals("链元素") ? "money" : "weight" , split[1])
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                ToastUtils.show("网络异常，请检查您的网路！");
                            }

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code =  jsonObject.getInt("resultCode");
                                    if(code != 1001){
                                        ToastUtils.show("抽奖数据同步失败，请检查您的网络");
                                    }else{
                                        requestCJJL();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {

            }

            @Override
            public void rotateBefore(ImageView goImg) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ChouJiangActivity.this);
                View v = View.inflate(ChouJiangActivity.this,R.layout.dialog_layout,null);
                builder.setView(v);
                builder.setCancelable(false);
                final AlertDialog dialog = builder.show();
                final EditText yzm = v.findViewById(R.id.et_inputyzm);
                Button ok = v.findViewById(R.id.bt_ok);
                Button no = v.findViewById(R.id.bt_no);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String choujm = yzm.getText().toString();
                        if(StringUtils.isEmpty(choujm)){
                            ToastUtils.show("请输入抽奖码！");
                            return;
                        }
                        requestYZMIsOk(dialog,choujm);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private void requestCJJL() {
        String userPhone = (String)SPUtils.get(this,"userName" , "");
        OkHttpUtils
                .get()
                .url(HttpUtils.CHOUJIANG_JILU)
                .addParams("userPhone" , userPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("网络异常，请检查您的网路");
                    }

                    @Override
                    public void onResponse(String response) {
                        shuaxinData(response);
                    }
                });
    }

    private void shuaxinData(String response) {
        try {
            if (mList != null) mList.clear();
            JSONArray ja = new JSONArray(response);
            for (int i = 0 ; i < ja.length() ; i++){
                JSONObject jsonObject = ja.getJSONObject(i);
                ChouJiangEntity cje = new ChouJiangEntity(jsonObject.getInt("dealType"),jsonObject.getDouble("dealAmount"),jsonObject.getString("dealTime"));
                mList.add(cje);
            }
            cjAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void requestYZMIsOk(final AlertDialog dialog, String choujm) {
        String userName = (String)SPUtils.get(this, "userName", "");
        if (StringUtils.isEmpty(userName)){
            ToastUtils.show("登录异常，请重新登录!");
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return;
        }
        OkHttpUtils
                .get()
                .url(HttpUtils.CHOUJIANGMA)
                .addParams("userPhone", userName)
                .addParams("activeCode",choujm.trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("验证异常，请检查你的网路");
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resultCode = jsonObject.getString("resultCode");
                            String message = jsonObject.getString("resultMess");
                            if(resultCode.equals("1001")){
                                ToastUtils.show("激活码可用，开始抽奖!");
                                int position =  getRandomIndex();
                                int number = getNumber(position);
                                Log.e("tag",position + "");
                                wsv.startRotate(number);
                            }else if(resultCode.equals("4001")){
                                ToastUtils.show("验证失败，请重试");
                            }else{
                                ToastUtils.show(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }

    private int getRandomIndex() {
        int number = 0;
        int index = new Random().nextInt(10000) + 1;
        if(index > 0 && index <= (number = jilvs[0])){
            return 1;
        }else if(index > number && index < (number += jilvs[1])){
            return 2;
        }else if(index > number && index < (number += jilvs[2])){
            return 3;
        }else if(index > number && index < (number += jilvs[3])){
            return 4;
        }else if(index > number && index < (number += jilvs[4])){
            return 5;
        }else if(index > number && index < (number += jilvs[5])){
            return 6;
        }
        return 0;
    }

    private void requestCJInfo() {
        OkHttpUtils
                .get()
                .url(HttpUtils.CHOUJIANG)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("请求失败，请检查你的网路!");
                        Log.e("ad" , e.getMessage());
                        ProgressUtils.closeProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("ad", response);
                        ProgressUtils.closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resultCode = jsonObject.getString("resultCode");
                            if(resultCode.equals("1001")){
                                //type的结果为1的话表示链元素 2为权重数量
                                String type = jsonObject.getString("content1");
                                //num为数量
                                String num = jsonObject.getString("count1");
                                des[0] = (type.equals("1")?"链元素" : "权重") + "：" + num;
                                mListBitmap.add(BitmapFactory.decodeResource(getResources(), type.equals("1")?R.mipmap.lianzhu_yuansu : R.mipmap.lianzhu_quanzhong));
                                //中奖几率
                                String jilv = jsonObject.getString("odds1");
                                Double j = Double.parseDouble(jilv);
                                jilvs[0] = (int)(j*10000);


                                String type1 = jsonObject.getString("content2");
                                String num1 = jsonObject.getString("count2");
                                des[1] = (type1.equals("1")?"链元素" : "权重") + "：" + num1;
                                mListBitmap.add(BitmapFactory.decodeResource(getResources(), type1.equals("1")?R.mipmap.lianzhu_yuansu : R.mipmap.lianzhu_quanzhong));
                                String jilv1 = jsonObject.getString("odds2");
                                Double j1 = Double.parseDouble(jilv1);
                                jilvs[1] = (int)(j1*10000);

                                String type2 = jsonObject.getString("content3");
                                String num2 = jsonObject.getString("count3");
                                des[2] = (type2.equals("1")?"链元素" : "权重") + "：" + num2;
                                mListBitmap.add(BitmapFactory.decodeResource(getResources(), type2.equals("1")?R.mipmap.lianzhu_yuansu : R.mipmap.lianzhu_quanzhong));
                                String jilv2 = jsonObject.getString("odds3");
                                Double j2 = Double.parseDouble(jilv2);
                                jilvs[2] = (int)(j2*10000);;

                                String type3 = jsonObject.getString("content4");
                                String num3 = jsonObject.getString("count4");
                                des[3] = (type3.equals("1")?"链元素" : "权重") + "：" + num3;
                                mListBitmap.add(BitmapFactory.decodeResource(getResources(), type3.equals("1")?R.mipmap.lianzhu_yuansu : R.mipmap.lianzhu_quanzhong));
                                String jilv3 = jsonObject.getString("odds4");
                                Double j3 = Double.parseDouble(jilv3);
                                jilvs[3] = (int)(j3*10000);

                                String type4 = jsonObject.getString("content5");
                                String num4 = jsonObject.getString("count5");
                                des[4] = (type4.equals("1")?"链元素" : "权重") + "：" + num4;
                                mListBitmap.add(BitmapFactory.decodeResource(getResources(), type4.equals("1")?R.mipmap.lianzhu_yuansu : R.mipmap.lianzhu_quanzhong));
                                String jilv4 = jsonObject.getString("odds5");
                                Double j4 = Double.parseDouble(jilv4);
                                jilvs[4] = (int)(j4*10000);


                                String type5 = jsonObject.getString("content6");
                                String num5 = jsonObject.getString("count6");
                                des[5] = (type5.equals("1")?"链元素" : "权重") + "：" + num5;
                                mListBitmap.add(BitmapFactory.decodeResource(getResources(), type5.equals("1")?R.mipmap.lianzhu_yuansu : R.mipmap.lianzhu_quanzhong));
                                String jilv5 = jsonObject.getString("odds6");
                                Double j5 = Double.parseDouble(jilv5);
                                jilvs[5] = (int)(j5*10000);

                                //主动旋转一下图片
                                mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap);
                                WheelSurfView.Builder build = new WheelSurfView.Builder()
                                        .setmColors(colors)
                                        .setmDeses(des)
                                        .setmIcons(mListBitmap)
                                        .setmType(1)
                                        .setmTypeNum(6)
                                        .setmMinTimes(6)
                                        .build();

                                wsv.setConfig(build);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    public int getNumber(int randomNumber){
        if(randomNumber == 1 || randomNumber == 4) {
            return randomNumber;

        }else if(randomNumber == 6){
            return 2;
        }else if(randomNumber == 5){
            return 3;
        }else if(randomNumber == 3){
            return 5;
        }else if(randomNumber == 2){
            return 6;
        }
        return 0;

    }
}
