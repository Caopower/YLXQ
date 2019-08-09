package com.example.administrator.ylxq.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.interfaces.OnRemoveListener;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


public class RandomView extends AppCompatTextView implements View.OnClickListener{
    private OnRemoveListener removeListener;
    public RandomView(Context context) {
        super(context);
        init();
    }

    public RandomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RandomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init() {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_piaoliup1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        setCompoundDrawables(null, drawable, null, null);
        setCompoundDrawablePadding(-ScreenUtils.dip2px(getContext(), 5));
        setTextColor(Color.WHITE);
        setTextSize(11);
        setGravity(Gravity.CENTER);
        startAnimation(animation());
        setOnClickListener(this);
    }

    private TranslateAnimation animation() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -10, 10);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }


    @Override
    public void onClick(View view) {
        this.clearAnimation();
        String userPhone = (String)SPUtils.get(getContext(),"userName" , "");
//        ToastUtils.show(getHint().toString());
        ((RandomFrameLayout)getParent()).removeView(RandomView.this);
        if (removeListener!=null) removeListener.remove(RandomView.this);
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext(),R.style.dialog);
        View v =  View.inflate(getContext(),R.layout.lys_tanchuan,null);
        ab.setView(v);
        ab.setCancelable(true);
        final TextView tx1 = v.findViewById(R.id.tv_tx1);
        tx1.setText("获得了"+getText()+"链元素");

        ab.show();

        //漂流瓶移出动画，有问题，暂时注释
//        this.startAnimation(translateAnimation());
        final String s = getHint().toString();
        Log.e("漂流瓶的id" , s.trim());
        OkHttpUtils.get().url(HttpUtils.UPDATE_LYS_NUMBER)
                .addParams("userPhone" , userPhone)
                .addParams("moneyId",s.trim())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtils.show("链元素上传失败，请检查您的网路" + e.getMessage());
                Log.e("tag" , e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String resultCode = jsonObject.getString("resultCode");
                    String resultMess = jsonObject.getString("resultMess");
                    if(resultCode.equals("1001")){
                        EventBus.getDefault().post(getText().toString());
                        for(int i = 0 ; i < HttpUtils.mList.size(); i++){
                            if(HttpUtils.mList.get(i).getMoneyId().equals(s.trim())){
                                HttpUtils.mList.remove(i);
                            }
                        }
                    }else{
                        ToastUtils.show(resultMess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private TranslateAnimation translateAnimation() {
        setEnabled(false);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -3000);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //这行代码因为fragment切换过快 导致在显示之后移除  所以暂时把当前代码移到图标点击时移除
                ((RandomFrameLayout)getParent()).removeView(RandomView.this);
                if (removeListener!=null) removeListener.remove(RandomView.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    public void setOnRemoveListener(OnRemoveListener removeListener){
        this.removeListener=removeListener;
    }

}
