package com.bixian365.dzc.entity.goodstype;

import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;

import java.util.List;

/**
 * 商品分类 查询详细商品信息
 */
public class TypeInfoEntity {
    private String total;
    private List<GoodsInfoEntity> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<GoodsInfoEntity> getRows() {
        return rows;
    }

    public void setRows(List<GoodsInfoEntity> rows) {
        this.rows = rows;
    }
}
