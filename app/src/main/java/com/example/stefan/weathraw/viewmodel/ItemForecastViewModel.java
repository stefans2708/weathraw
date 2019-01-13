package com.example.stefan.weathraw.viewmodel;

import com.example.stefan.weathraw.model.DayAverageValues;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.utils.WeatherDataUtils;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class ItemForecastViewModel extends BaseViewModel {

    private FiveDayCityForecast forecast;
    private List<DayAverageValues> dayAverageValuesList;

    public ItemForecastViewModel(FiveDayCityForecast forecast) {
        this.forecast = forecast;
        dayAverageValuesList = WeatherDataUtils.extractDayAverageValuesInList(forecast);
    }

    public FiveDayCityForecast getForecast() {
        return forecast;
    }

    public List<DayAverageValues> getDayAverageValuesList() {
        return dayAverageValuesList;
    }

    public List<Entry> generate24HoursForecast() {
        List<Entry> entries = new ArrayList<>();
        List<WeatherData> list = forecast.getList();
        for (int i = 0; i < 8; i++) {
            WeatherData weatherData = list.get(i);
            entries.add(new Entry(i,
                    WeatherDataUtils.getStandardTemperature(weatherData.getMain().getTemperature()).floatValue()));
        }
        return entries;
    }

    public DayAverageValues getDayData(int day) {
        if (dayAverageValuesList != null && dayAverageValuesList.get(day) != null) {
            return dayAverageValuesList.get(day);
        } else {
            return new DayAverageValues();
        }
    }

}
