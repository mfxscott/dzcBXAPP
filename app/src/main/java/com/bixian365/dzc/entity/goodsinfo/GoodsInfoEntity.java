package com.bixian365.dzc.entity.goodsinfo;

import com.bixian365.dzc.entity.bill.BillChirdrenEntity;

import java.util.List;

/**
 * Created by mfx-t224 on 2017/7/10.
 */

public class GoodsInfoEntity {
    private String id;
    private String goodsModel;
    private String categoryId;
    private String goodsCode;
    private String goodsName;
    private String goodsNickname;
    private String goodsUnit;
    private String goodsDesc ;
    private String thumbImg;
    private String albumImg;
    private String originalImg;
    private String brandId;
    private String brandName;
    private String categoryCode;
    private String categoryName;
    private String goodsIntroduce;
    private String marketPrice;
    private String shopPrice;
    private String shopWholesalePrice;
    private String skuBarcode;
    private String status;
    private String stockQty;
    private String supplyInfo;
    private String vendorCode;
    private String vendorName;
    private String wholesalePrice;
    private String quantity;




    private List<BillChirdrenEntity> chirdren;

    public List<BillChirdrenEntity> getChirdren() {
        return chirdren;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChirdren(List<BillChirdrenEntity> chirdren) {
        this.chirdren = chirdren;
    }

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

    public String getGoodsNickname() {
        return goodsNickname;
    }

    public void setGoodsNickname(String goodsNickname) {
        this.goodsNickname = goodsNickname;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public String getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(String albumImg) {
        this.albumImg = albumImg;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
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

    public String getGoodsIntroduce() {
        return goodsIntroduce;
    }

    public void setGoodsIntroduce(String goodsIntroduce) {
        this.goodsIntroduce = goodsIntroduce;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopWholesalePrice() {
        return shopWholesalePrice;
    }

    public void setShopWholesalePrice(String shopWholesalePrice) {
        this.shopWholesalePrice = shopWholesalePrice;
    }

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getSupplyInfo() {
        return supplyInfo;
    }

    public void setSupplyInfo(String supplyInfo) {
        this.supplyInfo = supplyInfo;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }
}
