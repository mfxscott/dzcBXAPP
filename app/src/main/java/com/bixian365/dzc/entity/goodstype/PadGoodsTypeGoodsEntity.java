package com.bixian365.dzc.entity.goodstype;

import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;

import java.util.List;

/**
 * Created by mfx-t224 on 2017/12/18.
 */

public class PadGoodsTypeGoodsEntity {
    //平板销售商品查询
    private List<GoodsInfoEntity> categoryList;
    private String categoryId;
    private String categoryName;
    private String categoryCode;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<GoodsInfoEntity> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<GoodsInfoEntity> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
