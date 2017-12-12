package com.bixian365.dzc.entity.car;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mfx-t224 on 2017/9/26.
 */

public class UserCouponEntity implements Parcelable {
    private String couponTime;
    private String couponMoney;
    private String couponTerm;

    protected UserCouponEntity(Parcel in) {
        couponTime = in.readString();
        couponMoney = in.readString();
        couponTerm = in.readString();
    }

    public static final Creator<UserCouponEntity> CREATOR = new Creator<UserCouponEntity>() {
        @Override
        public UserCouponEntity createFromParcel(Parcel in) {
            return new UserCouponEntity(in);
        }

        @Override
        public UserCouponEntity[] newArray(int size) {
            return new UserCouponEntity[size];
        }
    };

    public String getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(String couponTime) {
        this.couponTime = couponTime;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getCouponTerm() {
        return couponTerm;
    }

    public void setCouponTerm(String couponTerm) {
        this.couponTerm = couponTerm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(couponTime);
        dest.writeString(couponMoney);
        dest.writeString(couponTerm);
    }
}
