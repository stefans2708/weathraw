package com.stefan.weathraw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataModel implements Parcelable {

    private String city;
    private Double temperature;
    private String description;
    private String iconId;
    private List<WidgetNextHourDataModel> nextHoursData;

    public WidgetDataModel() {

    }

    protected WidgetDataModel(Parcel in) {
        city = in.readString();
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readDouble();
        }
        description = in.readString();
        iconId = in.readString();
        nextHoursData = in.createTypedArrayList(WidgetNextHourDataModel.CREATOR);
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

    public void setNextHoursWeatherData(List<WeatherData> nextFiveValues) {
        nextHoursData = new ArrayList<>();
        for (WeatherData weatherData : nextFiveValues) {
            nextHoursData.add(new WidgetNextHourDataModel(weatherData.getDt(),
                    weatherData.getMain().getTemperature(),
                    weatherData.getWeatherDescription().getDescription(),
                    weatherData.getWeatherDescription().getIcon()));
        }
    }

    public List<WidgetNextHourDataModel> getNextHoursData() {
        return nextHoursData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        if (temperature == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(temperature);
        }
        dest.writeString(description);
        dest.writeString(iconId);
        dest.writeTypedList(nextHoursData);
    }
}
