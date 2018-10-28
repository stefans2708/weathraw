package com.example.stefan.weathraw.viewmodel;

import com.example.stefan.weathraw.model.WeatherData;

public class ItemCurentWeatherViewModel extends BaseViewModel {

    private WeatherData weatherData;



    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}
