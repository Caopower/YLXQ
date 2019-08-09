package com.example.administrator.ylxq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.entity.WeigthEntity;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/8/8
 * Time: 11:45
 */
public class WeightAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WeigthEntity> mList;
    public WeightAdapter(Context context , ArrayList<WeigthEntity> mList){
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
        ViewHolder vh = null;
        if(view == null){
            view = View.inflate(context , R.layout.duihuan_item,null);
            vh = new ViewHolder();
            vh.title = view.findViewById(R.id.tv_title);
            vh.time = view.findViewById(R.id.tv_time);
            vh.moneyNumber = view.findViewById(R.id.tv_moneyNumber);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }
        WeigthEntity we = mList.get(i);
        vh.title.setText(we.getDealtype() == 1 ? "钱币兑换" : "转盘抽奖");
        String time = we.getDealtimeString();
        int index = time.lastIndexOf(":");
        String substring = time.substring(0, index);
        vh.time.setText(substring);
        vh.moneyNumber.setText("+"+we.getWeight()+"");
        return view;
    }


    public class ViewHolder{
        private TextView title;
        private TextView time;
        private TextView moneyNumber;

    }
}
