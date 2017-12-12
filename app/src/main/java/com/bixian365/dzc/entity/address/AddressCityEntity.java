package com.bixian365.dzc.entity.address;

import java.util.List;

/**
 * 城市
 * Created by NN on 2017/9/20.
 */

public class AddressCityEntity {
    private String value;
    private String label;
    private List<AddressDistrictEntity> children;

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

    public List<AddressDistrictEntity> getChildren() {
        return children;
    }

    public void setChildren(List<AddressDistrictEntity> children) {
        this.children = children;
    }
//    public class AddressDistrictEntity {
//        private String value;
//        private String label;
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//        public String getLabel() {
//            return label;
//        }
//
//        public void setLabel(String label) {
//            this.label = label;
//        }
//
//    }

}
