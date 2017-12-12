package com.bixian365.dzc.entity.car;

import android.os.Parcel;
import android.os.Parcelable;

import com.bixian365.dzc.entity.address.AddressInfoEntity;

import java.util.ArrayList;

/**
 * 购物车订单结算
 * Created by NN on 2017/9/25.
 */
public class FromOrderEntity implements Parcelable{
    private AddressInfoEntity defaultAddress;
    private String discountAmount;
    private String freightAmount;
    private String settlementAmount;
    private String transactionAmount;
    private String deliveryTime;
    private ArrayList<PayTypeEntity> settlementModes; //支付方式 在线支付和到付
    private ArrayList<OrderLinesEntity> orderLines; //支付商品列表
    private  ArrayList<OrderCouponsEntity> orderCoupons;//优惠券列表

    public AddressInfoEntity getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(AddressInfoEntity defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(String freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public ArrayList<PayTypeEntity> getSettlementModes() {
        return settlementModes;
    }

    public void setSettlementModes(ArrayList<PayTypeEntity> settlementModes) {
        this.settlementModes = settlementModes;
    }

    public ArrayList<OrderLinesEntity> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(ArrayList<OrderLinesEntity> orderLines) {
        this.orderLines = orderLines;
    }

    public ArrayList<OrderCouponsEntity> getOrderCoupons() {
        return orderCoupons;
    }

    public void setOrderCoupons(ArrayList<OrderCouponsEntity> orderCoupons) {
        this.orderCoupons = orderCoupons;
    }

    protected FromOrderEntity(Parcel in) {
        defaultAddress = in.readParcelable(AddressInfoEntity.class.getClassLoader());
        discountAmount = in.readString();
        freightAmount = in.readString();
        settlementAmount = in.readString();
        transactionAmount = in.readString();
        deliveryTime = in.readString();
        settlementModes = in.createTypedArrayList(PayTypeEntity.CREATOR);
        orderLines = in.createTypedArrayList(OrderLinesEntity.CREATOR);
        orderCoupons = in.createTypedArrayList(OrderCouponsEntity.CREATOR);
    }

    public static final Creator<FromOrderEntity> CREATOR = new Creator<FromOrderEntity>() {
        @Override
        public FromOrderEntity createFromParcel(Parcel in) {
            return new FromOrderEntity(in);
        }

        @Override
        public FromOrderEntity[] newArray(int size) {
            return new FromOrderEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(defaultAddress, flags);
        dest.writeString(discountAmount);
        dest.writeString(freightAmount);
        dest.writeString(settlementAmount);
        dest.writeString(transactionAmount);
        dest.writeString(deliveryTime);
        dest.writeTypedList(settlementModes);
        dest.writeTypedList(orderLines);
        dest.writeTypedList(orderCoupons);
    }
}
