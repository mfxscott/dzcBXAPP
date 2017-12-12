package com.bixian365.dzc.entity.orderlist;

import java.util.List;

/**
 * 摊主订单详情
 * Created by mfx-t224 on 2017/10/13.
 */

public class OrderDetailEntity {
    private String address;
    private String created;
    private List<OrderGoodsInfoEntity> myOrderLineVos;
    private String orderNo;
    private String settlementAmount;
    private String settlementMode;
    private String status;
    private String consigneeName;
    private String phoneNumber;
    private String freightAmount;
    private String discountAmount;
    private String transactionAmount;
    private String goodsId;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(String freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<OrderGoodsInfoEntity> getMyOrderLineVos() {
        return myOrderLineVos;
    }

    public void setMyOrderLineVos(List<OrderGoodsInfoEntity> myOrderLineVos) {
        this.myOrderLineVos = myOrderLineVos;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }
}
