package com.bixian365.dzc.entity;

import java.util.ArrayList;

/**
 * 合伙人扫码收货
 * Created by mfx-t224 on 2017/10/13.
 */

public class ScanPartnerOrderLinesEntity {
    private String outAuditor;
    private String states;
    private String outNo;
    private String vehicleNo;
    private String driverPhone;
    private String outTime;
    private String partnerUserName;
    private String driverName;
    private ArrayList<ScanPartnerConfirmEntity> orderList;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getOutAuditor() {
        return outAuditor;
    }

    public void setOutAuditor(String outAuditor) {
        this.outAuditor = outAuditor;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }


    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getPartnerUserName() {
        return partnerUserName;
    }

    public void setPartnerUserName(String partnerUserName) {
        this.partnerUserName = partnerUserName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public ArrayList<ScanPartnerConfirmEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<ScanPartnerConfirmEntity> orderList) {
        this.orderList = orderList;
    }
}
