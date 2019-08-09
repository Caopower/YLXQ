package com.example.administrator.ylxq.application;

import android.app.Application;

import com.example.administrator.ylxq.entity.JsonEntity;
import com.example.administrator.ylxq.entity.User;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/6/19.
 */

public class MyApplication extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    private List<JsonEntity> mList = new ArrayList<>();

    public static MyApplication getInstance(){
        MyApplication ml = null;
        if (ml == null)
            return new MyApplication();
        return null;
    }

    public List<JsonEntity> getmList() {
        return mList;
    }

    public void setmList(List<JsonEntity> mList) {
        this.mList = mList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtils.init(this);
    }
}
