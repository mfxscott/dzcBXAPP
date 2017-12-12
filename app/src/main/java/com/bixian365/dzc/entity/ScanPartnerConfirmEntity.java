package com.bixian365.dzc.entity;

/**
 * 合伙人扫码收货订单列表
 * Created by mfx-t224 on 2017/10/13.
 */

public class ScanPartnerConfirmEntity {

    private String shopUserNo;
    private String shopUserName;
    private  String orderNo;

    public String getShopUserNo() {
        return shopUserNo;
    }

    public void setShopUserNo(String shopUserNo) {
        this.shopUserNo = shopUserNo;
    }

    public String getShopUserName() {
        return shopUserName;
    }

    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
