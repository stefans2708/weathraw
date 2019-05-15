package com.stefan.weathraw.model;


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Coordinates {

    @SerializedName("lon")
    @Expose
    private Double longitude;
    @SerializedName("lat")
    @Expose
    private Double latitude;

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
