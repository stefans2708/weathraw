package com.stefan.weathraw.viewmodel;

import com.stefan.weathraw.model.WeatherData;

public class ItemCurrentWeatherViewModel extends BaseViewModel {

    private WeatherData weatherData;

    public ItemCurrentWeatherViewModel(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}
