package com.bixian365.dzc.entity.main;

/**
 * 首页轮播banner图片
 * Created by mfx-t224 on 2017/9/11.
 */

public class BannerSlidEntity {
    private String imgUrl;
    private String imgAction;
    private String imgParam;
    private String seconds;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

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

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
}
