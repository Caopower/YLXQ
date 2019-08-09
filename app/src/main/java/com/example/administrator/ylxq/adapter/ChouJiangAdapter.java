package com.example.administrator.ylxq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.entity.ChouJiangEntity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/8/7
 * Time: 9:53
 */
public class ChouJiangAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChouJiangEntity> mList;
    public ChouJiangAdapter(Context context , ArrayList<ChouJiangEntity> mList){
        this.context = context;
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view == null){
            vh = new ViewHolder();
            view = View.inflate(context, R.layout.choujiang_item,null);
            vh.title = view.findViewById(R.id.tv_title);
            vh.time = view.findViewById(R.id.tv_time);
            view.setTag(vh);

        }else{
            vh = (ViewHolder) view.getTag();
        }
        String dealTime = mList.get(i).getDealTime();
        int index = dealTime.indexOf(".");
        String substring = dealTime.substring(0, index - 3);

        vh.title.setText("恭喜你抽中了" + mList.get(i).getDealAmount() + (mList.get(i).getDealType() == 3 ?  "链元素" : "权重"));
        vh.time.setText(substring);
        return view;
    }


    public class ViewHolder{
        private TextView title;
        private TextView time;

    }

}
