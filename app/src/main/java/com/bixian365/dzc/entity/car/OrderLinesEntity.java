package com.bixian365.dzc.entity.car;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NN on 2017/9/25.
 */

public class OrderLinesEntity implements Parcelable {
    private String discountAmount;
    private String goodsCode;
    private String goodsImage;
    private String goodsModel;
    private String goodsName;
    private String quantity;
    private String skuBarcode;
    private String skuPrice;
    private String subTotal;

    protected OrderLinesEntity(Parcel in) {
        discountAmount = in.readString();
        goodsCode = in.readString();
        goodsImage = in.readString();
        goodsModel = in.readString();
        goodsName = in.readString();
        quantity = in.readString();
        skuBarcode = in.readString();
        skuPrice = in.readString();
        subTotal = in.readString();
    }

    public static final Creator<OrderLinesEntity> CREATOR = new Creator<OrderLinesEntity>() {
        @Override
        public OrderLinesEntity createFromParcel(Parcel in) {
            return new OrderLinesEntity(in);
        }

        @Override
        public OrderLinesEntity[] newArray(int size) {
            return new OrderLinesEntity[size];
        }
    };

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(String skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(discountAmount);
        dest.writeString(goodsCode);
        dest.writeString(goodsImage);
        dest.writeString(goodsModel);
        dest.writeString(goodsName);
        dest.writeString(quantity);
        dest.writeString(skuBarcode);
        dest.writeString(skuPrice);
        dest.writeString(subTotal);
    }
}
