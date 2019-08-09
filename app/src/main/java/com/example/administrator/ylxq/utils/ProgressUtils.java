package com.example.administrator.ylxq.utils;

import android.content.Context;

import com.android.tu.loadingdialog.LoadingDailog;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/7/9
 * Time: 11:09
 */
public class ProgressUtils {

    private static LoadingDailog.Builder loadingDailog = null;
    private static LoadingDailog ld = null;


    public static void showProgress(Context context,String message){
        loadingDailog = new LoadingDailog.Builder(context)
                .setCancelable(false)
                .setCancelOutside(false)
                .setMessage(message);

        ld = ProgressUtils.loadingDailog.create();
        ld.show();
    }


    public static void closeProgress(){
        if(ld != null) {
            ld.dismiss();
        }
    }
}
