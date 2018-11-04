package com.example.stefan.weathraw.utils;

public class WeatherDataUtils {

    public static Double getStandardTemperature(Double apiTemp) {
        return apiTemp - 273.15;
    }

}
