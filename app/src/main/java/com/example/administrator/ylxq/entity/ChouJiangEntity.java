package com.example.administrator.ylxq.entity;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/8/7
 * Time: 9:58
 */
public class ChouJiangEntity {
    private int dealType;
    private double dealAmount;
    private String dealTime;

    public ChouJiangEntity(){

    }

    public ChouJiangEntity(int dealType, double dealAmount, String dealTime) {
        this.dealType = dealType;
        this.dealAmount = dealAmount;
        this.dealTime = dealTime;
    }

    public int getDealType() {
        return dealType;
    }

    public void setDealType(int dealType) {
        this.dealType = dealType;
    }

    public double getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(double dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    @Override
    public String toString() {
        return "ChouJiangEntity{" +
                "dealType=" + dealType +
                ", dealAmount=" + dealAmount +
                ", dealTime='" + dealTime + '\'' +
                '}';
    }
}
