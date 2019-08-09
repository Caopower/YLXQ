package com.example.administrator.ylxq.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.administrator.ylxq.R;
import com.example.administrator.ylxq.utils.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻资讯界面。
 */
public class NewInfoActivity extends AppCompatActivity {
    @BindView(R.id.wv_new)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_info);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        wv.loadDataWithBaseURL(null,"<div class=\"js-article-detail\">\n" +
                "<p>我们从加密数字货币总市值周线走势能看到，从去年12月触及大底后截止目前加密总市值一直沿上升弧形趋势线震荡上行，币值也从刚启动的1000亿美元大幅拉升至最高点接近4000亿美元，整体区间涨幅高达4倍，而这几天随着BTC的大幅回撤，其余主流币包括大部分小币也联动跟跌，市值不断缩水，从盘面也看出目前走出一根超长上下插针走势，说明该区间多空分歧进一步加大，上方有近期持续拉升获利盘回吐与前期套牢筹码抛压，下方有做多资金承接筹码收回，目前上方强压为18年4月小牛波段高点4150亿美元附近，而目前加密总市值周线RSI再次拉涨至74，和前期三个礼拜前周线RSI一样均处于超买区域，上一次显示严重超买后总市值迎来了周线级别的回调，这一次如果BTC大盘再次带动整体市场掉头向下，那么这一周收阴线的可能性较大，若形成高位阴线十字星，那么后续加密总市值继续调整的概率继续加大，但目前走势仍处于上升弧形趋势线上方并未破位，一旦放量跌破的话，那么币值就会向下方2450亿美元寻求支撑。</p ><p><img src=\"https://img.jinse.com/2004512_image3.png\" title=\"XS9fR5aV2ozlWb2Ymt0x8MGxrKRlxvBZZ5UfXgvm.png\" alt=\"XS9fR5aV2ozlWb2Ymt0x8MGxrKRlxvBZZ5UfXgvm.png\"/></p ></div>","text/html" ,"utf-8",null);
    }
}
