package com.example.administrator.ylxq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.entity.MoneyEntity;
import com.example.administrator.ylxq.entity.WeigthEntity;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/8/9
 * Time: 11:58
 */
public class LianYSAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Object> mList;
    public LianYSAdapter(Context context , ArrayList<Object> mList){
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
        Object object = mList.get(i);
        if(object instanceof MoneyEntity){
            MoneyEntity me = (MoneyEntity)object;
            String content = "";
            String str = "";
            switch (me.getDealtype()){
                case 1:
                    content = "充值";
                    str = "+";
                    break;
                case 2:
                    content = "游戏充值";
                    str = "+";
                    break;
                case 3:
                    content = "摇奖";
                    str = "+";
                    break;
                case 4:
                    content = "转账";
                    str = "-";
                    break;
                case 5:
                    content = "提现";
                    str = "-";
                    break;
                case 6:
                    content = "发送红包";
                    str = "-";
                    break;
                case 7:
                    content = "收取红包";
                    str = "+";
                    break;
                case 8:
                    content = "权重兑换";
                    str = "-";
                    break;
            }
            vh.title.setText(content);
            String time = me.getShowDealTime();
            int index = time.lastIndexOf(":");
            String substring = time.substring(0, index);
            vh.time.setText(substring);
            vh.moneyNumber.setText(str + me.getDealamount());
        }else{
            WeigthEntity we = (WeigthEntity)object;
            vh.title.setText(we.getDealtype() == 1 ? "钱币兑换" : "转盘抽奖");
            String time = we.getDealtimeString();
            int index = time.lastIndexOf(":");
            String substring = time.substring(0, index);
            vh.time.setText(substring);
            vh.moneyNumber.setText("+"+we.getWeight()+"");
        }
        return view;
    }





    public class ViewHolder{
        private TextView title;
        private TextView time;
        private TextView moneyNumber;

    }


}
