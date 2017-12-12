package com.bixian365.dzc.entity.goods;

import com.bixian365.dzc.entity.bill.BillChirdrenEntity;

import java.util.List;

/**
 * Created by mfx-t224 on 2017/9/13.
 */

public class GoodsDetailInfoEntity {
    private String goodsCode;
    private String goodsName;
    private String goodsUnit ;
    private String brandName;
    private String categoryId;
    private String categoryName;
    private String goodsDesc;
    private String goodsIntroduce;
    private String goodsNickname;
    private String goodsPlace;
    private String originalImg;
    private String albumImg;
    private String foodGrade;
    private String isCommonGoos;
    private String packing;
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getIsCommonGoos() {
        return isCommonGoos;
    }

    public void setIsCommonGoos(String isCommonGoos) {
        this.isCommonGoos = isCommonGoos;
    }

    public String getFoodGrade() {
        return foodGrade;
    }

    public void setFoodGrade(String foodGrade) {
        this.foodGrade = foodGrade;
    }

    public String getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(String albumImg) {
        this.albumImg = albumImg;
    }

    private List<BillChirdrenEntity> skuList;

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsIntroduce() {
        return goodsIntroduce;
    }

    public void setGoodsIntroduce(String goodsIntroduce) {
        this.goodsIntroduce = goodsIntroduce;
    }

    public String getGoodsNickname() {
        return goodsNickname;
    }

    public void setGoodsNickname(String goodsNickname) {
        this.goodsNickname = goodsNickname;
    }

    public String getGoodsPlace() {
        return goodsPlace;
    }

    public void setGoodsPlace(String goodsPlace) {
        this.goodsPlace = goodsPlace;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public List<BillChirdrenEntity> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<BillChirdrenEntity> skuList) {
        this.skuList = skuList;
    }
}
