package com.bixian365.dzc.entity.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 获取服务器地址列表详细地址
 * Created by mfx-t224 on 2017/7/19.
 */

public class AddressInfoEntity implements Parcelable {
    private String  address;
    private String  cityCode;
    private String  cityName;
    private String  consigneeId;
    private String  consigneeMobile;
    private String  consigneeName;
    private String  districtCode;
    private String  districtName;
    private String  isDefault;
    private String  provinceCode;
    private String  provinceName;

    protected AddressInfoEntity(Parcel in) {
        address = in.readString();
        cityCode = in.readString();
        cityName = in.readString();
        consigneeId = in.readString();
        consigneeMobile = in.readString();
        consigneeName = in.readString();
        districtCode = in.readString();
        districtName = in.readString();
        isDefault = in.readString();
        provinceCode = in.readString();
        provinceName = in.readString();
    }

    public static final Creator<AddressInfoEntity> CREATOR = new Creator<AddressInfoEntity>() {
        @Override
        public AddressInfoEntity createFromParcel(Parcel in) {
            return new AddressInfoEntity(in);
        }

        @Override
        public AddressInfoEntity[] newArray(int size) {
            return new AddressInfoEntity[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getConsigneeId() {
        return consigneeId;
    }

    public void setConsigneeId(String consigneeId) {
        this.consigneeId = consigneeId;
    }

    public String getConsigneeMobile() {
        return consigneeMobile;
    }

    public void setConsigneeMobile(String consigneeMobile) {
        this.consigneeMobile = consigneeMobile;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(cityCode);
        dest.writeString(cityName);
        dest.writeString(consigneeId);
        dest.writeString(consigneeMobile);
        dest.writeString(consigneeName);
        dest.writeString(districtCode);
        dest.writeString(districtName);
        dest.writeString(isDefault);
        dest.writeString(provinceCode);
        dest.writeString(provinceName);
    }
}
