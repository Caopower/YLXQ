package com.example.administrator.ylxq.entity;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/8/8
 * Time: 9:38
 */
public class WeigthEntity {
    private int weiaccid;
    private String phone;
    private int dealtype;
    private double weight;
    private String dealtimeString;

    public WeigthEntity() {

    }

    public WeigthEntity(int dealtype, double weight, String dealtimeString) {
        this.dealtype = dealtype;
        this.weight = weight;
        this.dealtimeString = dealtimeString;
    }

    public WeigthEntity(int weiaccid, String phone, int dealtype, double weight, String dealtimeString) {
        this.weiaccid = weiaccid;
        this.phone = phone;
        this.dealtype = dealtype;
        this.weight = weight;
        this.dealtimeString = dealtimeString;
    }

    public int getWeiaccid() {
        return weiaccid;
    }

    public void setWeiaccid(int weiaccid) {
        this.weiaccid = weiaccid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDealtype() {
        return dealtype;
    }

    public void setDealtype(int dealtype) {
        this.dealtype = dealtype;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDealtimeString() {
        return dealtimeString;
    }

    public void setDealtimeString(String dealtimeString) {
        this.dealtimeString = dealtimeString;
    }

    @Override
    public String toString() {
        return "WeigthEntity{" +
                "weiaccid=" + weiaccid +
                ", phone='" + phone + '\'' +
                ", dealtype=" + dealtype +
                ", weight=" + weight +
                ", dealtimeString='" + dealtimeString + '\'' +
                '}';
    }
}
