package com.example.administrator.ylxq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.entity.JsonEntity;
import com.example.administrator.ylxq.interfaces.OnRemoveListener;
import com.example.administrator.ylxq.utils.BigDecimalUtils;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.RandomFrameLayout;
import com.example.administrator.ylxq.utils.RandomView;
import com.example.administrator.ylxq.utils.SPUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LianZhuFragment extends Fragment{


    @BindView(R.id.fl_random)
    RandomFrameLayout randomFrameLayout;
    @BindView(R.id.tv_wait)
    TextView tvWait;
    @BindView(R.id.tv_money)
    TextView money;
    @BindView(R.id.tv_quanzhong)
    TextView quanzhong;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view == null){
            Log.e("123",HttpUtils.mList.size()+"");
            view = inflater.inflate(R.layout.fragment_lian_zhu, container, false);
            ButterKnife.bind(this,view);
            EventBus.getDefault().register(this);
            boolean islogin = (boolean)SPUtils.get(getActivity(),"isLogin",false);
            if(!islogin){
                ToastUtils.show("请先登录");
            }else {
                requestLYSNumber();
            }
            randomFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    randomFrameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
            randomFrameLayout.setOnRemoveListener(new OnRemoveListener() {
                @Override
                public void remove(RandomView randomView) {
                    if (randomFrameLayout.getChildCount() == 1) {
                        //等待图片的动画启动 暂时屏蔽
//                        tvWait.startAnimation(animation());
//                        tvWait.setVisibility(View.VISIBLE);
                        randomFrameLayout.removeAllViews();
                        if(randomFrameLayout.getTagViews().size() > 0) {
                            //每次产生瓶子的时候会产生有效的坐标储存在 tagViews集合中 那么每点完一轮的瓶子的时候
                            //需要清空这些有效坐标 重新生成有效坐标
                            randomFrameLayout.getTagViews().clear();
                        }
                        updateViewValue();
                    }
                }
            });
        }
        else{
            //当Fragment切换时会出现动画消失，这里重新设置一下动画 没有考虑性能问题
            List<RandomView> viewList = randomFrameLayout.getViewList();
            Log.e("瓶子的数量",viewList.size() + "");
            for (RandomView rv: viewList) {
                Log.e("切换后的瓶子id",rv.getHint().toString());
                rv.startAnimation(animation());
        }

        }


        Log.e("ABC" ,"================");

        return view;
    }

    private void requestLYSNumber() {
        String userPhone = (String)SPUtils.get(getActivity(),"userName" , "");
        OkHttpUtils
                .get()
                .url(HttpUtils.GET_GAME_NUMBER)
                .addParams("userPhone" , userPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.show("链元素获取异常，请检查您的网路！！！"+e.getMessage());
                        updateViewValue();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("tag" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resultCode = jsonObject.getString("resultCode");
                            String resultMess = jsonObject.getString("resultMess");

                            if(resultCode.equals("1001")){
                                JSONArray jsonArray = jsonObject.getJSONArray("moneys");
                                String userMoney =  jsonObject.getString("userMoney");
                                String userWeight = jsonObject.getString("userWeight");
                                money.setText(userMoney);
                                quanzhong.setText(userWeight);
                                getData(jsonArray);
                                updateViewValue();
                            }else if(resultCode.equals("4001")){
                                ToastUtils.show("链元素访问失败");
                            }else {
                                ToastUtils.show(resultMess);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }

    private void getData(JSONArray jsonArray) {
        if(HttpUtils.mList.size() > 0){
            HttpUtils.mList.clear();
        }
        for(int i = 0 ; i < jsonArray.length() ; i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HttpUtils.mList.add(new JsonEntity(jsonObject.getString("moneyId"),jsonObject.getString("money")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateViewValue() {

        if(HttpUtils.mList.size() <= 0){
            if (randomFrameLayout.getChildCount() == 1) {
                tvWait.setVisibility(View.VISIBLE);
            }
            return;
        }
        for (int i = 0; i < HttpUtils.mList.size(); i++) {
            JsonEntity je = HttpUtils.mList.get(i);
            String moneyId = je.getMoneyId();
            String money = je.getMoneyNnmber();
            randomFrameLayout.updateView(money + "_" + moneyId);
            Log.e("瓶子id",je.getMoneyId());
            if(i == 9){
                break;
            }
        }

    }

    private TranslateAnimation animation() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -15, 15);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        List<RandomView> viewList = randomFrameLayout.getViewList();
        for (RandomView rv: viewList) {
            rv.clearAnimation();
        }

    }

    //漂流瓶点击监听传递过来的 每个瓶子底部的数值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void postInfo(String info) {
        String add = BigDecimalUtils.add(money.getText().toString(), info, 5);
        money.setText(add);
    }
}
