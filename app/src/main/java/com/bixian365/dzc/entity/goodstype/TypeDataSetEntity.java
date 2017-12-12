package com.bixian365.dzc.entity.goodstype;

import java.util.List;

/**
 * 商品一级分类
 */
public class TypeDataSetEntity {
    private String categoryName;
    private String fatherNode;
    private String categoryCode;
    private String icon;
    private String  level;
    private List<TypeChildrenEntity> children;

    public List<TypeChildrenEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TypeChildrenEntity> children) {
        this.children = children;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFatherNode() {
        return fatherNode;
    }

    public void setFatherNode(String fatherNode) {
        this.fatherNode = fatherNode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
