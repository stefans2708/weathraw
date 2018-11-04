package com.example.stefan.weathraw.viewmodel;

import com.example.stefan.weathraw.model.FiveDayCityForecast;

public class ItemForecastViewModel extends BaseViewModel {

    private FiveDayCityForecast forecast;

    public ItemForecastViewModel(FiveDayCityForecast forecast) {
        this.forecast = forecast;
    }

    public FiveDayCityForecast getForecast() {
        return forecast;
    }

    public void setForecast(FiveDayCityForecast forecast) {
        this.forecast = forecast;
    }
}
