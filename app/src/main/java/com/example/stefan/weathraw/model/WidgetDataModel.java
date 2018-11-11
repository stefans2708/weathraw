package com.example.stefan.weathraw.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WidgetDataModel implements Parcelable {

    private String city;
    private Double temperature;
    private String description;
    private String iconId;

    private WidgetDataModel(Parcel in) {
        city = in.readString();
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readDouble();
        }
        description = in.readString();
        iconId = in.readString();
    }

    public static final Creator<WidgetDataModel> CREATOR = new Creator<WidgetDataModel>() {
        @Override
        public WidgetDataModel createFromParcel(Parcel in) {
            return new WidgetDataModel(in);
        }

        @Override
        public WidgetDataModel[] newArray(int size) {
            return new WidgetDataModel[size];
        }
    };

    public WidgetDataModel() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        if (temperature == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(temperature);
        }
        parcel.writeString(description);
        parcel.writeString(iconId);
    }
}
