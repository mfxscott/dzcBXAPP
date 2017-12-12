package com.bixian365.dzc.entity;

import com.bixian365.dzc.entity.goods.GoodsDetailEntity;

import java.util.List;

/**
 * Created by mfx-t224 on 2017/8/23.
 */
public class GsonResponseDataEntity {
    private String responseCode;
    private String businessCode;
    private String responseText;
    private List<GoodsDetailEntity> responseData; //商品列表

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public List<GoodsDetailEntity> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<GoodsDetailEntity> responseData) {
        this.responseData = responseData;
    }
    @Override
    public String toString() {
        return "GsonResponseDataEntity{" +
                "responseCode='" + responseCode + '\'' +
                ", businessCode='" + businessCode + '\'' +
                ", responseText='" + responseText + '\'' +
                ", responseData=" + responseData +
                '}';
    }
}
