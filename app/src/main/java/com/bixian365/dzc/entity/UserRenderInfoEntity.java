package com.bixian365.dzc.entity;

/**
 * 用户我的查询相关数量
 * Created by mfx-t224 on 2017/9/29.
 */

public class UserRenderInfoEntity {
//   private String  completePurchases;
//    private String toCommitOrders;
//    private String  todayComm;
//    private String  todayOrder;
//    private String todaySellAmount;
//    private String  toDelivery;
    private String  toPayOrder;
//    private String  toReceive;
    private String  userableAmount;
    private String userableCoupon;


    private String toDelivery;
    private String toReceive;
    private String todayOrder;
    private String todaySellAmount;
    private String todayComm;
    private String toCommitOrders;
    private String completePurchases;


    public String getCompletePurchases() {
        return completePurchases;
    }

    public void setCompletePurchases(String completePurchases) {
        this.completePurchases = completePurchases;
    }

    public String getToCommitOrders() {
        return toCommitOrders;
    }

    public void setToCommitOrders(String toCommitOrders) {
        this.toCommitOrders = toCommitOrders;
    }

    public String getTodayComm() {
        return todayComm;
    }

    public void setTodayComm(String todayComm) {
        this.todayComm = todayComm;
    }

    public String getTodayOrder() {
        return todayOrder;
    }

    public void setTodayOrder(String todayOrder) {
        this.todayOrder = todayOrder;
    }

    public String getTodaySellAmount() {
        return todaySellAmount;
    }

    public void setTodaySellAmount(String todaySellAmount) {
        this.todaySellAmount = todaySellAmount;
    }

    public String getToDelivery() {
        return toDelivery;
    }

    public void setToDelivery(String toDelivery) {
        this.toDelivery = toDelivery;
    }

    public String getToPayOrder() {
        return toPayOrder;
    }

    public void setToPayOrder(String toPayOrder) {
        this.toPayOrder = toPayOrder;
    }

    public String getToReceive() {
        return toReceive;
    }

    public void setToReceive(String toReceive) {
        this.toReceive = toReceive;
    }

    public String getUserableAmount() {
        return userableAmount;
    }

    public void setUserableAmount(String userableAmount) {
        this.userableAmount = userableAmount;
    }

    public String getUserableCoupon() {
        return userableCoupon;
    }

    public void setUserableCoupon(String userableCoupon) {
        this.userableCoupon = userableCoupon;
    }
}
