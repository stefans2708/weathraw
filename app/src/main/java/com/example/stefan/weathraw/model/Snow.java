package com.example.stefan.weathraw.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("3h")
    @Expose
    private Double snowInLast3Hours;

    public Double getSnowInLast3Hours() {
        return snowInLast3Hours;
    }

    public void setSnowInLast3Hours(Double snowInLast3Hours) {
        this.snowInLast3Hours = snowInLast3Hours;
    }

}
