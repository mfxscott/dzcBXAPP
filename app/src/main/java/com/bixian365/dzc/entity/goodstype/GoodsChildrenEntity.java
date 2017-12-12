package com.bixian365.dzc.entity.goodstype;

/**
 * Created by NN on 2017/9/9.
 */

public class GoodsChildrenEntity {
    private String shopPrice;
    private String goodsModel;
    private String skuBarcode;

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public void setSkuBarcode(String skuBarcode) {
        this.skuBarcode = skuBarcode;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }
}
