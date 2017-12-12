package com.bixian365.dzc.entity;

import java.util.List;

/**
 * 商品分类 一，二级实体类
 * @author mfx
 * @time  2017/8/3 16:50
 */
public class GoodsTypeEntity {
    private String id;
    private String name;
    private String catNo;
    private String parentId;
    private List<GoodsTypeEntity> goodsTypeList;

    public List<GoodsTypeEntity> getGoodsTypeList() {
        return goodsTypeList;
    }

    public void setGoodsTypeList(List<GoodsTypeEntity> goodsTypeList) {
        this.goodsTypeList = goodsTypeList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatNo() {
        return catNo;
    }

    public void setCatNo(String catNo) {
        this.catNo = catNo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
