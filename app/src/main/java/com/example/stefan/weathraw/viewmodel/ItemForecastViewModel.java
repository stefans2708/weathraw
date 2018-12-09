package com.example.stefan.weathraw.viewmodel;

import android.app.DatePickerDialog;
import android.util.ArrayMap;

import com.example.stefan.weathraw.model.DayAverageValues;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.utils.WeatherDataUtils;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class ItemForecastViewModel extends BaseViewModel {

    private static final int DATA_PER_DAY = 8;

    private FiveDayCityForecast forecast;
    private List<DayAverageValues> dayAverageValuesList = new ArrayList<>();

    public ItemForecastViewModel(FiveDayCityForecast forecast) {
        this.forecast = forecast;
        extractDayValuesInList();
    }

    public FiveDayCityForecast getForecast() {
        return forecast;
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

//    public List<BarEntry> getMaxTemperaturesForNext5Days() {
//        List<BarEntry> maxTempsResult = new ArrayList<>();
//        List<WeatherData> allData = forecast.getList();
//
//        ArrayMap<String, Double> maxValues = new ArrayMap<>();
//        for (WeatherData weatherData : allData) {
//            String time[] = weatherData.getDate().split(" ");
//            String dateOnly = time[0];
//            Double tempTemp = maxValues.get(dateOnly);
//            if (tempTemp == null || tempTemp < weatherData.getMain().getTemperature()) {
//                maxValues.put(dateOnly, weatherData.getMain().getTemperature());
//            }
//        }
//
//        int i = 0;
//        for (String date : maxValues.keySet()) {
//            maxTempsResult.add(new BarEntry(i++,
//                    WeatherDataUtils.getStandardTemperature(maxValues.get(date)).floatValue()));
//        }
//        if (maxTempsResult.size() == 6) {
//            maxTempsResult.remove(5);
//        }
//        maxTempsResult.remove(0);   //today
//
//        return maxTempsResult;
//    }

    private void extractDayValuesInList() {
        List<WeatherData> allData = forecast.getList();

        ArrayMap<String, ArrayList<WeatherData>> averageValuesArrayMap = new ArrayMap<>();
        for (WeatherData weatherData : allData) {
            String time[] = weatherData.getDate().split(" ");
            String dateOnly = time[0];
            if (averageValuesArrayMap.get(dateOnly) == null) {
                ArrayList<WeatherData> newList = new ArrayList<>();
                newList.add(weatherData);
                averageValuesArrayMap.put(dateOnly, newList);
            } else {
                averageValuesArrayMap.get(dateOnly).add(weatherData);
            }
        }

        for (String key : averageValuesArrayMap.keySet()) {
            dayAverageValuesList.add(calculateDayAverages(averageValuesArrayMap.get(key)));
        }

        dayAverageValuesList.remove(0);//today
    }

    private DayAverageValues calculateDayAverages(ArrayList<WeatherData> dailyData) {
        Double minTemp = Double.MAX_VALUE;
        Double maxTemp = Double.MIN_VALUE;
        int icon = 0;
        Double cloudsSum = 0.0;
        Double windSum = 0.0;
        Double pressureSum = 0.0;
        String note = "";

        for (WeatherData singleData : dailyData) {
            minTemp = singleData.getMain().getTemperature() < minTemp ? singleData.getMain().getTemperature() : minTemp;
            maxTemp = singleData.getMain().getTemperature() > maxTemp ? singleData.getMain().getTemperature() : maxTemp;
            cloudsSum += singleData.getClouds() != null ? singleData.getClouds().getCloudPercent() : 0;
            windSum += singleData.getWind() != null ? singleData.getWind().getSpeed() : 0;
            pressureSum += singleData.getMain().getPressure() != null ? singleData.getMain().getPressure() : 0;

            String iconCode = singleData.getWeatherDescription().getIcon().substring(0, 1);
            Integer iconNum = Integer.valueOf(iconCode);
            if (iconNum > icon) {
                icon = iconNum;
                note = singleData.getWeatherDescription().getDescription();
            }
        }
        String iconCode = icon + "d";

        return new DayAverageValues(iconCode, maxTemp, minTemp, cloudsSum / DATA_PER_DAY,
                pressureSum / DATA_PER_DAY, windSum / DATA_PER_DAY, note);
    }

    public DayAverageValues getDayData(int day) {
        if (dayAverageValuesList != null && dayAverageValuesList.get(day) != null) {
            return dayAverageValuesList.get(day);
        } else {
            return new DayAverageValues();
        }
    }

}
