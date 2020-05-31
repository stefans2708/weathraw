package com.stefan.weathraw.cache.model;


import androidx.room.ColumnInfo;

public class Coordinates {

    @ColumnInfo(name = "lon")
    private Double longitude;
    @ColumnInfo(name = "lat")
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}
