package com.example.stefan.weathraw.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    @Expose
    private Integer cloudPercent;

    public Integer getCloudPercent() {
        return cloudPercent;
    }

    public void setCloudPercent(Integer cloudPercent) {
        this.cloudPercent = cloudPercent;
    }

}