package com.bixian365.dzc.entity.cgListInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mfx-t224 on 2017/9/1.
 * 采购列表
 */

public class CGListInfoEntity implements Parcelable {
    private String purchaseAmount;
    private String completeTime;
    private String purchaseCode;//采购单号purchaseCode
    private String actualAmount;//实际支付金额
    private String payState;//付款状态，payed：已付款；not_pay：未付款；
    private String receiveState;//收货状态(10:新建 20:供应商确认30:已发货 40:完成)
    private String receiveResult;//0:未收货；10:部分收货20:全部收货
    private String receiveTime;//收货时间
    private String requestTime;//要求到货时间
    private String receiver;//收货人名称
    private String receiverPhone;//收货人电话
    private String receiverAddr;//收货地址
    private String vehicleNo;//车牌号
    private String driverName;//司机名称
    private String driverPhone;//司机电话
    private String sendTime;//发货时间
    private String sendAddr;//发货人地址
    private String senderPhone;//发货人电话
    private String senderName;//发货人名称
    private String remark;//备注
    private String created;//订单创建时间
    private String paymentMode;
    private List<CGPurchaseLinesEntity> purchaseLineVos;

    protected CGListInfoEntity(Parcel in) {
        purchaseAmount = in.readString();
        completeTime = in.readString();
        purchaseCode = in.readString();
        actualAmount = in.readString();
        payState = in.readString();
        receiveState = in.readString();
        receiveResult = in.readString();
        receiveTime = in.readString();
        requestTime = in.readString();
        receiver = in.readString();
        receiverPhone = in.readString();
        receiverAddr = in.readString();
        vehicleNo = in.readString();
        driverName = in.readString();
        driverPhone = in.readString();
        sendTime = in.readString();
        sendAddr = in.readString();
        senderPhone = in.readString();
        senderName = in.readString();
        remark = in.readString();
        created = in.readString();
        paymentMode = in.readString();
        purchaseLineVos = in.createTypedArrayList(CGPurchaseLinesEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(purchaseAmount);
        dest.writeString(completeTime);
        dest.writeString(purchaseCode);
        dest.writeString(actualAmount);
        dest.writeString(payState);
        dest.writeString(receiveState);
        dest.writeString(receiveResult);
        dest.writeString(receiveTime);
        dest.writeString(requestTime);
        dest.writeString(receiver);
        dest.writeString(receiverPhone);
        dest.writeString(receiverAddr);
        dest.writeString(vehicleNo);
        dest.writeString(driverName);
        dest.writeString(driverPhone);
        dest.writeString(sendTime);
        dest.writeString(sendAddr);
        dest.writeString(senderPhone);
        dest.writeString(senderName);
        dest.writeString(remark);
        dest.writeString(created);
        dest.writeString(paymentMode);
        dest.writeTypedList(purchaseLineVos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CGListInfoEntity> CREATOR = new Creator<CGListInfoEntity>() {
        @Override
        public CGListInfoEntity createFromParcel(Parcel in) {
            return new CGListInfoEntity(in);
        }

        @Override
        public CGListInfoEntity[] newArray(int size) {
            return new CGListInfoEntity[size];
        }
    };

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(String receiveState) {
        this.receiveState = receiveState;
    }

    public String getReceiveResult() {
        return receiveResult;
    }

    public void setReceiveResult(String receiveResult) {
        this.receiveResult = receiveResult;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendAddr() {
        return sendAddr;
    }

    public void setSendAddr(String sendAddr) {
        this.sendAddr = sendAddr;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<CGPurchaseLinesEntity> getPurchaseLineVos() {
        return purchaseLineVos;
    }

    public void setPurchaseLineVos(List<CGPurchaseLinesEntity> purchaseLineVos) {
        this.purchaseLineVos = purchaseLineVos;
    }
}