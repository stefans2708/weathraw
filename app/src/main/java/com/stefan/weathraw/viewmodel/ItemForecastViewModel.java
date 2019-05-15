package com.stefan.weathraw.viewmodel;

import com.stefan.weathraw.model.DayAverageValues;
import com.stefan.weathraw.model.FiveDayCityForecast;
import com.stefan.weathraw.model.WeatherData;
import com.stefan.weathraw.utils.WeatherDataUtils;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class ItemForecastViewModel extends BaseViewModel {

    private FiveDayCityForecast forecast;

    public ItemForecastViewModel(FiveDayCityForecast forecast) {
        this.forecast = forecast;
    }

    public FiveDayCityForecast getForecast() {
        return forecast;
    }

    public List<DayAverageValues> getDayAverageValuesList() {
        return WeatherDataUtils.extractDayAverageValuesInList(forecast);
    }

    public List<Entry> generate24HoursForecast() {
        List<Entry> entries = new ArrayList<>();
        List<WeatherData> list = forecast.getList();
        for (int i = 0; i < 8; i++) {
            WeatherData weatherData = list.get(i);
            entries.add(new Entry(i, WeatherDataUtils.getStandardTemperature(weatherData.getMain().getTemperature()).floatValue()));
        }
        return entries;
    }

}
