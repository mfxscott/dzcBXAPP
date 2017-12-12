package com.bixian365.dzc.entity.wallet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mfx-t224 on 2017/9/19.
 */

public class WalletInfoEntity implements Parcelable {
    private String accNo;
    private String addiable;
    private String bankAccountName;
    private String bankBranchName;
    private String bankName;
    private String created;
    private String creator;
    private String id;
    private String idNo;
    private String phoneNo;
    private String removable;
    private String totalAmt;
    private String totalWithdraw;
    private String unbilledAmt;
    private String usableAmt;
    private String userNo;
    private String version;

    protected WalletInfoEntity(Parcel in) {
        accNo = in.readString();
        addiable = in.readString();
        bankAccountName = in.readString();
        bankBranchName = in.readString();
        bankName = in.readString();
        created = in.readString();
        creator = in.readString();
        id = in.readString();
        idNo = in.readString();
        phoneNo = in.readString();
        removable = in.readString();
        totalAmt = in.readString();
        totalWithdraw = in.readString();
        unbilledAmt = in.readString();
        usableAmt = in.readString();
        userNo = in.readString();
        version = in.readString();
    }

    public static final Creator<WalletInfoEntity> CREATOR = new Creator<WalletInfoEntity>() {
        @Override
        public WalletInfoEntity createFromParcel(Parcel in) {
            return new WalletInfoEntity(in);
        }

        @Override
        public WalletInfoEntity[] newArray(int size) {
            return new WalletInfoEntity[size];
        }
    };

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAddiable() {
        return addiable;
    }

    public void setAddiable(String addiable) {
        this.addiable = addiable;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRemovable() {
        return removable;
    }

    public void setRemovable(String removable) {
        this.removable = removable;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(String totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }

    public String getUnbilledAmt() {
        return unbilledAmt;
    }

    public void setUnbilledAmt(String unbilledAmt) {
        this.unbilledAmt = unbilledAmt;
    }

    public String getUsableAmt() {
        return usableAmt;
    }

    public void setUsableAmt(String usableAmt) {
        this.usableAmt = usableAmt;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accNo);
        dest.writeString(addiable);
        dest.writeString(bankAccountName);
        dest.writeString(bankBranchName);
        dest.writeString(bankName);
        dest.writeString(created);
        dest.writeString(creator);
        dest.writeString(id);
        dest.writeString(idNo);
        dest.writeString(phoneNo);
        dest.writeString(removable);
        dest.writeString(totalAmt);
        dest.writeString(totalWithdraw);
        dest.writeString(unbilledAmt);
        dest.writeString(usableAmt);
        dest.writeString(userNo);
        dest.writeString(version);
    }
}
