
package com.example.administrator.ylxq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.adapter.TabsFragmentPagerAdapter;
import com.example.administrator.ylxq.application.MyApplication;
import com.example.administrator.ylxq.entity.User;
import com.example.administrator.ylxq.fragment.ChatFragment;
import com.example.administrator.ylxq.fragment.FindFragment;
import com.example.administrator.ylxq.fragment.FriendListFragment;
import com.example.administrator.ylxq.fragment.LianZhuFragment;
import com.example.administrator.ylxq.fragment.MyUserFragment;
import com.example.administrator.ylxq.utils.ActivityCollector;
import com.example.administrator.ylxq.utils.HttpUtils;
import com.example.administrator.ylxq.utils.ProgressUtils;
import com.example.administrator.ylxq.utils.SPUtils;
import com.hjq.toast.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeActivity extends FragmentActivity {

    private ViewPager mPager;
    private RadioGroup mGroup;
    private RadioButton rbChat,rbContacts,rbLianzhu,rbDiscovery,rbMe;
    private ArrayList<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityCollector.addActivity(this);
        boolean islogin = (boolean)SPUtils.get(this,"isLogin",false);
        if(islogin){
           String userName = (String)SPUtils.get(this ,"userName" , "");
           String userPsw = (String) SPUtils.get(this ,"psw" , "");
            login(userName,userPsw);
        }

        //初始化界面组件
        initView();
        //初始化ViewPager
        initViewPager();
    }

    private void initView(){
        mPager=findViewById(R.id.viewPager);
        mGroup=findViewById(R.id.radiogroup);
        rbChat=findViewById(R.id.rb_chat);
        rbContacts=findViewById(R.id.rb_contacts);
        rbDiscovery=findViewById(R.id.rb_discovery);
        rbLianzhu = findViewById(R.id.rb_lianzhu);
        rbMe=findViewById(R.id.rb_me);
        //RadioGroup选中状态改变监听
        mGroup.setOnCheckedChangeListener(new myCheckChangeListener());
    }

    private void initViewPager(){
        ChatFragment weChatFragment=new ChatFragment();
        FriendListFragment friendFragment=new FriendListFragment();
        FindFragment findFragment=new FindFragment();
        MyUserFragment meFragment=new MyUserFragment();
        LianZhuFragment lz = new LianZhuFragment();
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(weChatFragment);
        fragmentList.add(friendFragment);
        fragmentList.add(lz);
        fragmentList.add(findFragment);
        fragmentList.add(meFragment);
        //ViewPager设置适配器
        mPager.setAdapter(new TabsFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        //ViewPager显示第一个Fragment
        mPager.setCurrentItem(0);
        //ViewPager页面切换监听
        mPager.setOnPageChangeListener(new myOnPageChangeListener());
    }

    /**
     *RadioButton切换Fragment
     */
    private class myCheckChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_chat:
                    //ViewPager显示第一个Fragment且关闭页面切换动画效果
                    mPager.setCurrentItem(0,false);
                    break;
                case R.id.rb_contacts:
                    mPager.setCurrentItem(1,false);
                    break;
                case R.id.rb_lianzhu:
                    mPager.setCurrentItem(2,false);
                    break;
                case R.id.rb_discovery:
                    mPager.setCurrentItem(3,false);
                    break;
                case R.id.rb_me:
                    mPager.setCurrentItem(4,false);
                    break;
            }
        }
    }

    /**
     *ViewPager切换Fragment,RadioGroup做相应变化
     */
    private class myOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mGroup.check(R.id.rb_chat);
                    break;
                case 1:
                    mGroup.check(R.id.rb_contacts);
                    break;
                case 2:
                    mGroup.check(R.id.rb_lianzhu);
                    break;
                case 3:
                    mGroup.check(R.id.rb_discovery);
                    break;
                case 4:
                    mGroup.check(R.id.rb_me);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public void login(final String userName , final String userPsw){
        ProgressUtils.showProgress(this,"登录中...");
        OkHttpUtils
                .get()
                .url(HttpUtils.LOGIN)
                .addParams("userPhone",userName)
                .addParams("userPwd",userPsw)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ProgressUtils.closeProgress();
                        ToastUtils.show("登录异常，请检查您的网路！");
                        Log.e("login" , e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        ProgressUtils.closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resultMess = jsonObject.getString("resultCode");
                            if (resultMess.equals("1001")){
                                String balance = jsonObject.getString("balance");
                                String userPhone =  jsonObject.getString("userPhone");
                                String weight = jsonObject.getString("weight");
                                User user = new User(userPhone , Double.parseDouble(balance) , Double.parseDouble(weight) , null);
                                ((MyApplication)getApplication()).setUser(user);
                            }else{
                                ToastUtils.show(jsonObject.getString("resultMess"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("loginresult" , response);
                    }
                });
    }
}
