package com.bixian365.dzc.entity;

/**
 * Created by mfx-t224 on 2017/8/24.
 */

public class testGoodsinfo {
    private String sex;
    private String acount;
    private String tag;
    private String mobile;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "testGoodsinfo{" +
                "sex='" + sex + '\'' +
                ", acount='" + acount + '\'' +
                ", tag='" + tag + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
