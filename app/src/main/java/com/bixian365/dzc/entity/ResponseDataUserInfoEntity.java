package com.bixian365.dzc.entity;

/**
 * Created by mfx-t224 on 2017/8/23.
 */
public class ResponseDataUserInfoEntity {
    private String responseCode;
    private String businessCode;
    private String responseText;
    private UserInfoEntity responseData; //供应商用户信息
//    private List<UserInfoEntity>

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

    public UserInfoEntity getResponseData() {
        return responseData;
    }

    public void setResponseData(UserInfoEntity responseData) {
        this.responseData = responseData;
    }
}
