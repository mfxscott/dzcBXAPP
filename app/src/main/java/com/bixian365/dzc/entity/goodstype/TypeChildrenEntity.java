package com.bixian365.dzc.entity.goodstype;

/**
 * 商品二级分类
 */
public class TypeChildrenEntity {
    private String categoryCode;
    private String categoryName;
    private String id;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
