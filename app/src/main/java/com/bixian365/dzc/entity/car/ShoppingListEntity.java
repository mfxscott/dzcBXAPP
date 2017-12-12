package com.bixian365.dzc.entity.car;

import java.util.List;

/**
 * 购物车商品列表
 * Created by NN on 2017/9/9.
 */
public class ShoppingListEntity {
    private String shopCode;
    private String shopName;
    private String isOwner;
    private List<ShoppingCartLinesEntity>  shoppingCartLines;



    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public List<ShoppingCartLinesEntity> getShoppingCartLines() {
        return shoppingCartLines;
    }

    public void setShoppingCartLines(List<ShoppingCartLinesEntity> shoppingCartLines) {
        this.shoppingCartLines = shoppingCartLines;
    }
}
