package com.example.administrator.ylxq.entity;

import java.util.List;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/7/10
 * Time: 16:13
 */
public class User {
    private String userPhone;
    private String userPsw;
    private double balance;
    private double weight;
    private List<Object> activeCodeList;

    public User(){

    }

    public User(String userPhone, double balance, double weight, List<Object> activeCodeList) {
        this.userPhone = userPhone;
        this.balance = balance;
        this.weight = weight;
        this.activeCodeList = activeCodeList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userPhone='" + userPhone + '\'' +
                ", userPsw='" + userPsw + '\'' +
                ", balance=" + balance +
                ", weight=" + weight +
                ", activeCodeList=" + activeCodeList +
                '}';
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPsw() {
        return userPsw;
    }

    public void setUserPsw(String userPsw) {
        this.userPsw = userPsw;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Object> getActiveCodeList() {
        return activeCodeList;
    }

    public void setActiveCodeList(List<Object> activeCodeList) {
        this.activeCodeList = activeCodeList;
    }
}
