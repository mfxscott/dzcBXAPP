package com.bixian365.dzc.entity.main;

/**
 * Created by mfx-t224 on 2017/10/12.
 */

public class ButtonsEntity {
    private String name;
    private String imgUrl;
    private String imgAction;
    private String imgParam;

    public String getImgAction() {
        return imgAction;
    }

    public void setImgAction(String imgAction) {
        this.imgAction = imgAction;
    }

    public String getImgParam() {
        return imgParam;
    }

    public void setImgParam(String imgParam) {
        this.imgParam = imgParam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
