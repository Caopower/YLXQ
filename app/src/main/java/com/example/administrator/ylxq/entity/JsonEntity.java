package com.example.administrator.ylxq.entity;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/7/29
 * Time: 15:32
 */
public class JsonEntity {
    private String moneyId;
    private String moneyNnmber;

    public JsonEntity(String moneyId, String moneyNnmber) {
        this.moneyId = moneyId;
        this.moneyNnmber = moneyNnmber;
    }

    public String getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(String moneyId) {
        this.moneyId = moneyId;
    }

    public String getMoneyNnmber() {
        return moneyNnmber;
    }

    public void setMoneyNnmber(String moneyNnmber) {
        this.moneyNnmber = moneyNnmber;
    }
}
