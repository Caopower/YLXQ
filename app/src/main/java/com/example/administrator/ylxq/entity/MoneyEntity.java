package com.example.administrator.ylxq.entity;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/8/9
 * Time: 14:10
 */
public class MoneyEntity {
    //1.充值 2.游戏充值  3.摇奖  4转账  5.提现  6.发送红包 7.收取红包 8.钱币兑换权重
    private int dealtype;
    private String dealamount;
    private String showDealTime;
    private String dealphone;

    public MoneyEntity(int dealtype, String dealamount, String showDealTime) {
        this.dealtype = dealtype;
        this.dealamount = dealamount;
        this.showDealTime = showDealTime;
    }

    public MoneyEntity(int dealtype, String dealamount, String showDealTime, String dealphone) {
        this.dealtype = dealtype;
        this.dealamount = dealamount;
        this.showDealTime = showDealTime;
        this.dealphone = dealphone;
    }

    public int getDealtype() {
        return dealtype;
    }

    public void setDealtype(int dealtype) {
        this.dealtype = dealtype;
    }

    public String getDealamount() {
        return dealamount;
    }

    public void setDealamount(String dealamount) {
        this.dealamount = dealamount;
    }

    public String getShowDealTime() {
        return showDealTime;
    }

    public void setShowDealTime(String showDealTime) {
        this.showDealTime = showDealTime;
    }

    public String getDealphone() {
        return dealphone;
    }

    public void setDealphone(String dealphone) {
        this.dealphone = dealphone;
    }

    @Override
    public String toString() {
        return "MoneyEntity{" +
                "dealtype=" + dealtype +
                ", dealamount='" + dealamount + '\'' +
                ", showDealTime='" + showDealTime + '\'' +
                ", dealphone='" + dealphone + '\'' +
                '}';
    }
}
