package com.bixian365.dzc.entity.car;

import android.os.Parcel;
import android.os.Parcelable;

/** 支付类型  货到付款或者在线支付
 * Created by mfx-t224 on 2017/9/26.
 */

public class PayTypeEntity implements Parcelable {
    private String value;
    private String label;

    protected PayTypeEntity(Parcel in) {
        value = in.readString();
        label = in.readString();
    }

    public static final Creator<PayTypeEntity> CREATOR = new Creator<PayTypeEntity>() {
        @Override
        public PayTypeEntity createFromParcel(Parcel in) {
            return new PayTypeEntity(in);
        }

        @Override
        public PayTypeEntity[] newArray(int size) {
            return new PayTypeEntity[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(label);
    }
}
