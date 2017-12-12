package com.bixian365.dzc.entity.orderlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 摊主普通订单列表
 * Created by NN on 2017/9/27.
 */

public class OrderInfoEntity implements Parcelable {
    private String orderNo;
    private String states;
    private String vehicleNo;
    private String images;
    private String omsStatus;
    private String driverName;
    private String driverPhone;
    private String goodsTotalAmount;
    private String transactionAmount;
    private String partnerUserNo;
    private String partnerUserName;
    private String shopUserNo;
    private String shopUserName;
    private String remarks;
    private String printState;
    private String printExpressState;
    private String orderTime;
    private String orderAddress;
    private String  tradeNo;
    private String  deliveryTime;
    private String settlementAmount;
    private List<OrderGoodsInfoEntity> orderLines;

    public List<OrderGoodsInfoEntity> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderGoodsInfoEntity> orderLines) {
        this.orderLines = orderLines;
    }

    protected OrderInfoEntity(Parcel in) {
        orderNo = in.readString();
        states = in.readString();
        vehicleNo = in.readString();
        images = in.readString();
        omsStatus = in.readString();
        driverName = in.readString();
        driverPhone = in.readString();
        goodsTotalAmount = in.readString();
        transactionAmount = in.readString();
        partnerUserNo = in.readString();
        partnerUserName = in.readString();
        shopUserNo = in.readString();
        shopUserName = in.readString();
        remarks = in.readString();
        printState = in.readString();
        printExpressState = in.readString();
        orderTime = in.readString();
        orderAddress = in.readString();
        tradeNo = in.readString();
        deliveryTime = in.readString();
        settlementAmount = in.readString();
        orderLines = in.createTypedArrayList(OrderGoodsInfoEntity.CREATOR);
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel in) {
            return new OrderInfoEntity(in);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOmsStatus() {
        return omsStatus;
    }

    public void setOmsStatus(String omsStatus) {
        this.omsStatus = omsStatus;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getGoodsTotalAmount() {
        return goodsTotalAmount;
    }

    public void setGoodsTotalAmount(String goodsTotalAmount) {
        this.goodsTotalAmount = goodsTotalAmount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPartnerUserNo() {
        return partnerUserNo;
    }

    public void setPartnerUserNo(String partnerUserNo) {
        this.partnerUserNo = partnerUserNo;
    }

    public String getPartnerUserName() {
        return partnerUserName;
    }

    public void setPartnerUserName(String partnerUserName) {
        this.partnerUserName = partnerUserName;
    }

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPrintState() {
        return printState;
    }

    public void setPrintState(String printState) {
        this.printState = printState;
    }

    public String getPrintExpressState() {
        return printExpressState;
    }

    public void setPrintExpressState(String printExpressState) {
        this.printExpressState = printExpressState;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderNo);
        dest.writeString(states);
        dest.writeString(vehicleNo);
        dest.writeString(images);
        dest.writeString(omsStatus);
        dest.writeString(driverName);
        dest.writeString(driverPhone);
        dest.writeString(goodsTotalAmount);
        dest.writeString(transactionAmount);
        dest.writeString(partnerUserNo);
        dest.writeString(partnerUserName);
        dest.writeString(shopUserNo);
        dest.writeString(shopUserName);
        dest.writeString(remarks);
        dest.writeString(printState);
        dest.writeString(printExpressState);
        dest.writeString(orderTime);
        dest.writeString(orderAddress);
        dest.writeString(tradeNo);
        dest.writeString(deliveryTime);
        dest.writeString(settlementAmount);
        dest.writeTypedList(orderLines);
    }
}
