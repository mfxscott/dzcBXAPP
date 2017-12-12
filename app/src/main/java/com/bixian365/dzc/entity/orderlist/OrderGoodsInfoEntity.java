package com.bixian365.dzc.entity.orderlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单中的商品信息
 * Created by NN on 2017/9/27.
 */

public class OrderGoodsInfoEntity implements Parcelable {
    private String id;
    private String goodsCode;
    private String skuCode;
    private String skuName;
    private String skuImage;
    private String skuPrice;
    private String  skuNumber;
    private String totalAmount;
    private String goodsUnit;
    private String removable;
    private String editable;
    private String addiable;
    private String goodsId;

    private String imageSrc;
    private String skuWeight;
    private String unit;
    private String shopWholesalePrice;
    private String shopPrice;
    private String originSkuPrice;
    private String  goodsModel;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuImage() {
        return skuImage;
    }

    public void setSkuImage(String skuImage) {
        this.skuImage = skuImage;
    }

    public String getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(String skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getRemovable() {
        return removable;
    }

    public void setRemovable(String removable) {
        this.removable = removable;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getAddiable() {
        return addiable;
    }

    public void setAddiable(String addiable) {
        this.addiable = addiable;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getSkuWeight() {
        return skuWeight;
    }

    public void setSkuWeight(String skuWeight) {
        this.skuWeight = skuWeight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getShopWholesalePrice() {
        return shopWholesalePrice;
    }

    public void setShopWholesalePrice(String shopWholesalePrice) {
        this.shopWholesalePrice = shopWholesalePrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getOriginSkuPrice() {
        return originSkuPrice;
    }

    public void setOriginSkuPrice(String originSkuPrice) {
        this.originSkuPrice = originSkuPrice;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    protected OrderGoodsInfoEntity(Parcel in) {
        id = in.readString();
        goodsCode = in.readString();
        skuCode = in.readString();
        skuName = in.readString();
        skuImage = in.readString();
        skuPrice = in.readString();
        skuNumber = in.readString();
        totalAmount = in.readString();
        goodsUnit = in.readString();
        removable = in.readString();
        editable = in.readString();
        addiable = in.readString();
        imageSrc = in.readString();
        skuWeight = in.readString();
        unit = in.readString();
        shopWholesalePrice = in.readString();
        shopPrice = in.readString();
        originSkuPrice = in.readString();
        goodsModel = in.readString();
    }

    public static final Creator<OrderGoodsInfoEntity> CREATOR = new Creator<OrderGoodsInfoEntity>() {
        @Override
        public OrderGoodsInfoEntity createFromParcel(Parcel in) {
            return new OrderGoodsInfoEntity(in);
        }

        @Override
        public OrderGoodsInfoEntity[] newArray(int size) {
            return new OrderGoodsInfoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(goodsCode);
        parcel.writeString(skuCode);
        parcel.writeString(skuName);
        parcel.writeString(skuImage);
        parcel.writeString(skuPrice);
        parcel.writeString(skuNumber);
        parcel.writeString(totalAmount);
        parcel.writeString(goodsUnit);
        parcel.writeString(removable);
        parcel.writeString(editable);
        parcel.writeString(addiable);
        parcel.writeString(imageSrc);
        parcel.writeString(skuWeight);
        parcel.writeString(unit);
        parcel.writeString(shopWholesalePrice);
        parcel.writeString(shopPrice);
        parcel.writeString(originSkuPrice);
        parcel.writeString(goodsModel);
    }
}
