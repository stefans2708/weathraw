package com.stefan.weathraw.model;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DayAverageValues {

    private String icon;
    private Double maxTemperature;
    private Double minTemperature;
    private Double avgClouds;
    private Double avgPressure;
    private Double avgWind;
    private String weatherNote;
    private String date;

    public DayAverageValues(String icon, Double maxTemperature, Double minTemperature,
                            Double avgClouds, Double avgPressure, Double avgWind, String weatherNote, String date) {
        this.icon = icon;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.avgClouds = avgClouds;
        this.avgPressure = avgPressure;
        this.avgWind = avgWind;
        this.weatherNote = weatherNote;
        this.date = date;
    }

    public DayAverageValues() {
        this.icon = "";
        this.maxTemperature = 0.0;
        this.minTemperature = 0.0;
        this.avgClouds = 0.0;
        this.avgPressure = 0.0;
        this.avgWind = 0.0;
        this.weatherNote = "";
        this.date = "";
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getAvgClouds() {
        return avgClouds;
    }

    public void setAvgClouds(Double avgClouds) {
        this.avgClouds = avgClouds;
    }

    public Double getAvgPressure() {
        return avgPressure;
    }

    public void setAvgPressure(Double avgPressure) {
        this.avgPressure = avgPressure;
    }

    public Double getAvgWind() {
        return avgWind;
    }

    public void setAvgWind(Double avgWind) {
        this.avgWind = avgWind;
    }

    public String getWeatherNote() {
        return weatherNote;
    }

    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
