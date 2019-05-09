package com.example.stefan.weathraw.utils;

import com.example.stefan.weathraw.model.WeatherData;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class HourXAxisFormatter implements IAxisValueFormatter {

    private List<Integer> weatherTimes;

    public HourXAxisFormatter(List<WeatherData> weatherDataList) {
        this.weatherTimes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            weatherTimes.add(weatherDataList.get(i).getDt());
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return WeatherDataUtils.getHours(WeatherDataUtils.getJavaDateFromUnixTime(weatherTimes.get((int) value)));
    }

}
