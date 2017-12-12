package com.bixian365.dzc.entity.bill;

import java.util.List;

/**
 * Created by NN on 2017/9/19.
 */

public class CategoryListEntity{
    private String  goodsUnit;
    private String id;
    private String goodsNickname;
    private String goodsCode;
    private String goodsName;
    private String originalImg;
    private List<BillChirdrenEntity> chirdren;


    public List<BillChirdrenEntity> getChirdren() {
        return chirdren;
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

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }


    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsNickname() {
        return goodsNickname;
    }

    public void setGoodsNickname(String goodsNickname) {
        this.goodsNickname = goodsNickname;
    }

}
