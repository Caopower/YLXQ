package com.example.administrator.ylxq.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/7/11
 * Time: 9:57
 */
public class ActivityCollector {
    //activity集合
    public static List<Activity> mActList = new ArrayList<Activity>();
    /**
     * 添加Activity
     *
     * @param activity
     */
    public static void addActivity(Activity activity){
        mActList.add(activity);
    }
    /**
     * 移除Activity
     */
    public static void removeActivity(Activity activity) {
        mActList.remove(activity);
    }
    /**
     * 退出
     */
    public static void finishAll() {
        for (Activity activity : mActList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
