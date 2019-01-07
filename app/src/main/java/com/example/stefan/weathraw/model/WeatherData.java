package com.example.stefan.weathraw.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("coord")
    @Expose
    private Coordinates coord;
    @SerializedName("weather")
    @Expose
    private List<WeatherDescription> weatherDescriptions = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private MainWeatherData main;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private GeneralWeatherData sys;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;
    @SerializedName("dt_txt")
    @Expose
    private String date;
    @SerializedName("apiCallTime")
    @Expose
    private String lastResponseTime;


    public Coordinates getCoordinates() {
        return coord;
    }

    public void setCoordinates(Coordinates coord) {
        this.coord = coord;
    }

    public List<WeatherDescription> getWeatherDescriptionList() {
        return weatherDescriptions;
    }

    public WeatherDescription getWeatherDescription() {
        return weatherDescriptions != null && weatherDescriptions.size()>0 ? weatherDescriptions.get(0) : new WeatherDescription();
    }

    public void setWeatherDescriptions(List<WeatherDescription> weatherDescriptions) {
        this.weatherDescriptions = weatherDescriptions;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public MainWeatherData getMain() {
        return main;
    }

    public void setMain(MainWeatherData main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public GeneralWeatherData getSys() {
        return sys;
    }

    public void setSys(GeneralWeatherData sys) {
        this.sys = sys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastResponseTime() {
        return lastResponseTime;
    }

    public void setLastResponseTime(String lastResponseTime) {
        this.lastResponseTime = lastResponseTime;
    }
}