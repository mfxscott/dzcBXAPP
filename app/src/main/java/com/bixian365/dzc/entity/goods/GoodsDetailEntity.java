package com.bixian365.dzc.entity.goods;

import java.util.List;

/**
 * 商品详情详细信息
 * @author mfx
 * @time  2017/8/23 12:07
 */
public class GoodsDetailEntity {
    private String id;
    private String creator;
    private String created;
    private String modifier;
    private String modified;
    private String version;
    private String cid;
    private String cno;
    private String bid;
    private String bname;
    private String goodsCode;
    private String goodsName;
    private String shopCode;
    private String goodsNickname;
    private String goodsLabel;
    private String goodsUnit;
    private String foodGrade;
    private String goodsPlace;
    private String goodsType;
    private String originalImg;
    private String thumbImg;
    private String albumImg;
    private String goodsDesc;
    private String goodsIntroduce;
    private String status;
    private String removed;
    private String supplyName;
    private String purchaseName;
    private String ordered;
    private String addiable;
    private String removable;
    private String editable;
    private GoodsDetailCatEntity cat;
    private GoodsDetailBrandEntity brand;
    private List<GoodsDetailSkuListEntity> skuList;
    public static class GoodsDetailCatEntity{
        private String id;
        private String creator;
        private String created;
        private String modifier;
        private String version;
        private String modified;
        private String catNo;
        private String catName;
        private String catImg;
        private String catDesc;
        private String isParent;
        private String parentId;
        private String enabled;
        private String ordered;
        private String level;
        private String addiable;
        private String removable;
        private String editable;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public String getCatNo() {
            return catNo;
        }

        public void setCatNo(String catNo) {
            this.catNo = catNo;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public String getCatImg() {
            return catImg;
        }

        public void setCatImg(String catImg) {
            this.catImg = catImg;
        }

        public String getCatDesc() {
            return catDesc;
        }

        public void setCatDesc(String catDesc) {
            this.catDesc = catDesc;
        }

        public String getIsParent() {
            return isParent;
        }

        public void setIsParent(String isParent) {
            this.isParent = isParent;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

        public String getOrdered() {
            return ordered;
        }

        public void setOrdered(String ordered) {
            this.ordered = ordered;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getAddiable() {
            return addiable;
        }

        public void setAddiable(String addiable) {
            this.addiable = addiable;
        }

        public String getRemovable() {
            return removable;
        }

        public void setRemovable(String removable) {
            this.removable = removable;
        }

        public String getEditable() {
            return editable;
        }

        public void setEditable(String editable) {
            this.editable = editable;
        }
    }
    public static class GoodsDetailBrandEntity{
        private String id;
        private String creator;
        private String created;
        private String modifier;
        private String version;
        private String modified;
        private String name;
        private String bdesc;
        private String brandPlace;
        private String brandLogo;
        private String status;
        private String ordered;
        private String addiable;
        private String removable;
        private String editable;
        public String getModified() {
            return modified;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBdesc() {
            return bdesc;
        }

        public void setBdesc(String bdesc) {
            this.bdesc = bdesc;
        }

        public String getBrandPlace() {
            return brandPlace;
        }

        public void setBrandPlace(String brandPlace) {
            this.brandPlace = brandPlace;
        }

        public String getBrandLogo() {
            return brandLogo;
        }

        public void setBrandLogo(String brandLogo) {
            this.brandLogo = brandLogo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrdered() {
            return ordered;
        }

        public void setOrdered(String ordered) {
            this.ordered = ordered;
        }

        public String getAddiable() {
            return addiable;
        }

        public void setAddiable(String addiable) {
            this.addiable = addiable;
        }

        public String getRemovable() {
            return removable;
        }

        public void setRemovable(String removable) {
            this.removable = removable;
        }

        public String getEditable() {
            return editable;
        }

        public void setEditable(String editable) {
            this.editable = editable;
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getGoodsNickname() {
        return goodsNickname;
    }

    public void setGoodsNickname(String goodsNickname) {
        this.goodsNickname = goodsNickname;
    }

    public String getGoodsLabel() {
        return goodsLabel;
    }

    public void setGoodsLabel(String goodsLabel) {
        this.goodsLabel = goodsLabel;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getFoodGrade() {
        return foodGrade;
    }

    public void setFoodGrade(String foodGrade) {
        this.foodGrade = foodGrade;
    }

    public String getGoodsPlace() {
        return goodsPlace;
    }

    public void setGoodsPlace(String goodsPlace) {
        this.goodsPlace = goodsPlace;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }

    public String getAddiable() {
        return addiable;
    }

    public void setAddiable(String addiable) {
        this.addiable = addiable;
    }

    public String getRemovable() {
        return removable;
    }

    public void setRemovable(String removable) {
        this.removable = removable;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public GoodsDetailCatEntity getCat() {
        return cat;
    }

    public void setCat(GoodsDetailCatEntity cat) {
        this.cat = cat;
    }

    public GoodsDetailBrandEntity getBrand() {
        return brand;
    }

    public void setBrand(GoodsDetailBrandEntity brand) {
        this.brand = brand;
    }

    public List<GoodsDetailSkuListEntity> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<GoodsDetailSkuListEntity> skuList) {
        this.skuList = skuList;
    }
}
