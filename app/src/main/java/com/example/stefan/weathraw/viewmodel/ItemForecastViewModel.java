package com.example.stefan.weathraw.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class ItemForecastViewModel extends BaseViewModel {

    private FiveDayCityForecast forecast;

    public ItemForecastViewModel(FiveDayCityForecast forecast) {
        this.forecast = forecast;
    }

    public List<Entry> generateTemperatureGraphEntries() {
        List<Entry> entries = new ArrayList<>();
        List<WeatherData> list = forecast.getList();
        for (int i = 0; i < list.size(); i++) {
            WeatherData weatherData = list.get(i);
            entries.add(new Entry(i, weatherData.getMain().getTemperature().floatValue()));
        }
        return entries;
    }

    public FiveDayCityForecast getForecast() {
        return forecast;
    }

    public void setForecast(FiveDayCityForecast forecast) {
        this.forecast = forecast;
    }
}
