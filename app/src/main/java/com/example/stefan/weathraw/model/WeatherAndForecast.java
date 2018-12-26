package com.example.stefan.weathraw.model;

public class WeatherAndForecast {

    private WeatherData weatherData;
    private FiveDayCityForecast forecastData;

    public WeatherAndForecast(WeatherData weatherData, FiveDayCityForecast forecastData) {
        this.weatherData = weatherData;
        this.forecastData = forecastData;
    }

    public WeatherAndForecast() {

    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public FiveDayCityForecast getForecastData() {
        return forecastData;
    }

    public void setForecastData(FiveDayCityForecast forecastData) {
        this.forecastData = forecastData;
    }
}
