package com.example.administrator.ylxq.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.activity.AboutActivity;
import com.example.administrator.ylxq.activity.ChouJiangActivity;
import com.example.administrator.ylxq.activity.LoginActivity;
import com.example.administrator.ylxq.activity.MoneyActivity;
import com.example.administrator.ylxq.activity.RegistActivity;
import com.example.administrator.ylxq.activity.SettingActivity;
import com.example.administrator.ylxq.activity.YingYongActivity;
import com.example.administrator.ylxq.application.MyApplication;
import com.example.administrator.ylxq.entity.User;
import com.example.administrator.ylxq.utils.PackageUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.example.administrator.ylxq.utils.StringUtils;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyUserFragment extends Fragment {
    @BindView(R.id.tv_top)
    TextView tv;
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_phone)
    TextView userPhone;
    @BindView(R.id.tv_noLogin)
    TextView noLogin;
    @BindView(R.id.ll_userRoom)
    LinearLayout userRoom;
    @BindView(R.id.tv_version)
    TextView version;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_user, container, false);
        ButterKnife.bind(this,view);
        version.setText("当前版本号："+PackageUtils.getVersionName(getActivity()));
        String userName = (String)SPUtils.get(getActivity(), "userName", "");
        if (!StringUtils.isEmpty(userName)){
            noLogin.setVisibility(View.GONE);
            userRoom.setVisibility(View.VISIBLE);
            userPhone.setText(userName);
        }
        tv.setText("我的");
        return view;
    }


    @OnClick({R.id.userRoom,R.id.moneyRoom,R.id.toFriendRoom,R.id.settingRoom,R.id.helpRoom,R.id.yingyongRoom,R.id.aboutRoom})
    public void click(View v){
        boolean isLogin = (boolean)SPUtils.get(getActivity(), "isLogin", false);
        if(!isLogin){
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity())
                    .setTitle("请选择")
                    .setMessage("您还没有登录，是否前往登录界面");
            ab.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            ab.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ab.show();
            return;
        }
        switch (v.getId()){
            case R.id.userRoom:
                ToastUtils.show("这是用户详情界面，请期待");
                break;
            case R.id.moneyRoom:
                intentActivity(MoneyActivity.class);
                break;
           case R.id.toFriendRoom:
               ToastUtils.show("这是分享好友界面，请期待");
                break;

            case R.id.settingRoom:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            case R.id.helpRoom:
                ToastUtils.show("这是帮助界面，请期待");
                break;

            case R.id.yingyongRoom:
                intentActivity(YingYongActivity.class);
                break;

            case R.id.aboutRoom:
                intentActivity(AboutActivity.class);
                break;

        }
    }

    public void intentActivity(Class<?> cla){
        Intent i = new Intent(getActivity() , cla);
        startActivity(i);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
