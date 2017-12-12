package com.bixian365.dzc.entity.car;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 购买时优惠劵
 * Created by mfx-t224 on 2017/9/26.
 */

public class OrderCouponsEntity implements Parcelable {
    private String couponNo; //劵号
    private String couponTime;//到期时间
    private String couponTerm; //满多少可用
    private String couponMoney;//抵扣多少钱
    private String couponSlogan;//90抵100
    private String isUsable;  //是否可用
    private String isExclusive;//是否排它表示，用了它，其它的优惠券就不能用了

    protected OrderCouponsEntity(Parcel in) {
        couponNo = in.readString();
        couponTime = in.readString();
        couponTerm = in.readString();
        couponMoney = in.readString();
        couponSlogan = in.readString();
        isUsable = in.readString();
        isExclusive = in.readString();
    }

    public static final Creator<OrderCouponsEntity> CREATOR = new Creator<OrderCouponsEntity>() {
        @Override
        public OrderCouponsEntity createFromParcel(Parcel in) {
            return new OrderCouponsEntity(in);
        }

        @Override
        public OrderCouponsEntity[] newArray(int size) {
            return new OrderCouponsEntity[size];
        }
    };

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(String couponTime) {
        this.couponTime = couponTime;
    }

    public String getCouponTerm() {
        return couponTerm;
    }

    public void setCouponTerm(String couponTerm) {
        this.couponTerm = couponTerm;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getCouponSlogan() {
        return couponSlogan;
    }

    public void setCouponSlogan(String couponSlogan) {
        this.couponSlogan = couponSlogan;
    }

    public String getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(String isUsable) {
        this.isUsable = isUsable;
    }

    public String getIsExclusive() {
        return isExclusive;
    }

    public void setIsExclusive(String isExclusive) {
        this.isExclusive = isExclusive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(couponNo);
        dest.writeString(couponTime);
        dest.writeString(couponTerm);
        dest.writeString(couponMoney);
        dest.writeString(couponSlogan);
        dest.writeString(isUsable);
        dest.writeString(isExclusive);
    }
}
