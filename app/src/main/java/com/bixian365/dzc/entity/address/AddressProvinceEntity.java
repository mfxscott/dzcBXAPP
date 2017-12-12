package com.bixian365.dzc.entity.address;

import java.util.List;

/**
 * 省份
 * Created by NN on 2017/9/20.
 */

public class AddressProvinceEntity {
    private String value;
    private String label;
//    private ArrayList<AddressProvinceEntity> children;
//
//    public ArrayList<AddressProvinceEntity> getChildren() {
//        return children;
//    }
//
//    public void setChildren(ArrayList<AddressProvinceEntity> children) {
//        this.children = children;
//    }

    private List<AddressCityEntity> children;
    public List<AddressCityEntity> getChildren() {
        return children;
    }

    public void setChildren(List<AddressCityEntity> children) {
        this.children = children;
    }




    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
