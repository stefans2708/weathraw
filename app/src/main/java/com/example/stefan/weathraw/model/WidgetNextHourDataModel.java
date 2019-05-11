package com.example.stefan.weathraw.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WidgetNextHourDataModel implements Parcelable {

    private Integer dateTime;
    private Double temperature;
    private String description;
    private String icon;

    public WidgetNextHourDataModel(Integer dateTime, Double temperature, String description, String icon) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
    }

    protected WidgetNextHourDataModel(Parcel in) {
        if (in.readByte() == 0) {
            dateTime = null;
        } else {
            dateTime = in.readInt();
        }
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readDouble();
        }
        description = in.readString();
        icon = in.readString();
    }

    public static final Creator<WidgetNextHourDataModel> CREATOR = new Creator<WidgetNextHourDataModel>() {
        @Override
        public WidgetNextHourDataModel createFromParcel(Parcel in) {
            return new WidgetNextHourDataModel(in);
        }

        @Override
        public WidgetNextHourDataModel[] newArray(int size) {
            return new WidgetNextHourDataModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dateTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(dateTime);
        }
        if (temperature == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(temperature);
        }
        dest.writeString(description);
        dest.writeString(icon);
    }

    public Integer getDateTime() {
        return dateTime;
    }

    public void setDateTime(Integer dateTime) {
        this.dateTime = dateTime;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
