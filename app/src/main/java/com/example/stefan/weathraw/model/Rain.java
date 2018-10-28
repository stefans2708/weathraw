package com.example.stefan.weathraw.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    @Expose
    private Double rainInLast3Hours;

    public Double getRainInLast3Hours() {
        return rainInLast3Hours;
    }

    public void setRainInLast3Hours(Double rainInLast3Hours) {
        this.rainInLast3Hours = rainInLast3Hours;
    }
}