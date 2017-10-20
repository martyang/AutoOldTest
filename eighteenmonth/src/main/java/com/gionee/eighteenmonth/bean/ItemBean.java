package com.gionee.eighteenmonth.bean;

/*
 *  @项目名：  AutoAging18Month 
 *  @包名：    com.gionee.eighteenmonth.bean
 *  @文件名:   ItemBean
 *  @创建者:   gionee
 *  @创建时间:  2017/7/31 14:49
 *  @描述：
 */


import android.os.Parcel;
import android.os.Parcelable;

public class ItemBean implements Parcelable {
    private int      icon;
    private String   describe;
    private String   details;

    public ItemBean() {
    }

    protected ItemBean(Parcel in) {
        icon = in.readInt();
        describe = in.readString();
        details = in.readString();
    }

    public static final Creator<ItemBean> CREATOR = new Creator<ItemBean>() {
        @Override
        public ItemBean createFromParcel(Parcel in) {
            return new ItemBean(in);
        }

        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }
    };

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(icon);
        parcel.writeString(describe);
        parcel.writeString(details);
    }
}
