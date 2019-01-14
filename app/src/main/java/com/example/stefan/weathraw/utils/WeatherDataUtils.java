package com.example.stefan.weathraw.utils;

import android.net.Uri;
import android.util.ArrayMap;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.model.DayAverageValues;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherData;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDataUtils {

    private static final int DATA_PER_DAY = 8;
    private static DecimalFormat oneDecimalFormatter;

    private static DecimalFormat getOneDecimalFormatter() {
        if (oneDecimalFormatter == null) {
            oneDecimalFormatter = new DecimalFormat("#.#");
            oneDecimalFormatter.setRoundingMode(RoundingMode.CEILING);
        }
        return oneDecimalFormatter;
    }

    public static Integer getStandardTemperature(Double apiTemp) {
        long value = Math.round(apiTemp - 273.15);
        return (int) value;
    }

    public static String getFormattedTemperature(Double apiTemp) {
        return getOneDecimalFormatter().format(getStandardTemperature(apiTemp)) + WeatherApplication.getAppContext().getString(R.string.degree_celsius);
    }

    public static String getIconUrl(String iconId) {
        return "http://openweathermap.org/img/w/" + iconId + ".png";
    }

    public static String getOfflineIconUrl(String iconId) {
        return "file:///android_asset/" + iconId + ".png";
    }

    public static String getDayHourFormat(Date date) {
        return new SimpleDateFormat("dd/MM, HH:mm", Locale.US).format(date);
    }

    public static String getDayMonthFormat(Date date) {
        return new SimpleDateFormat("EEE, dd.MMMM", Locale.US).format(date);
    }

    public static Date getJavaDateFromUnixTime(int unixTime) {
        return new Date((long) unixTime * 1000);
    }

    public static String getHours(Date date) {
        return new SimpleDateFormat("HH:mm", Locale.US).format(date);
    }

    public static String getTemperatureFormat(double value) {
        return getOneDecimalFormatter().format(getStandardTemperature(value))
                + WeatherApplication.getAppContext().getString(R.string.degree_celsius);
    }

    public static String getWindFormat(double value) {
        return getOneDecimalFormatter().format(value) + " m/s";
    }

    public static String getCloudFormat(double value) {
        return getOneDecimalFormatter().format(value) + " %";
    }

    public static String getPressureFormat(double value) {
        return getOneDecimalFormatter().format(value) + " mbar";
    }

    public static String getHumidityFormat(double value) {
        return getOneDecimalFormatter().format(value) + " %";
    }

    public static String getVisibilityFormat(double value) {
        return getOneDecimalFormatter().format(value) + " m";
    }

    public static String getHourMinuteFormat(int seconds) {
        return new SimpleDateFormat("HH:mm", Locale.US).format(new Date((long) seconds * 1000));
    }

    public static String getHourMinuteFromUnixTime(int unixTime) {
        return new SimpleDateFormat("HH:mm", Locale.US).format(getJavaDateFromUnixTime(unixTime));
    }

    public static List<WeatherData> extractNextFourValues(FiveDayCityForecast forecast) {
        List<WeatherData> allData = forecast.getList();
        List<WeatherData> nextFourData = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            nextFourData.add(allData.get(i));
        }

        return nextFourData;
    }

    public static List<DayAverageValues> extractDayAverageValuesInList(FiveDayCityForecast forecast) {
        List<WeatherData> allData = forecast.getList();
        List<DayAverageValues> dayAverageValuesList = new ArrayList<>();

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
        return dayAverageValuesList;
    }

    private static DayAverageValues calculateDayAverages(ArrayList<WeatherData> dailyData) {
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

            String iconCode = singleData.getWeatherDescription().getIcon().substring(0, 2);
            Integer iconNum = Integer.valueOf(iconCode);
            if (iconNum > icon) {
                icon = iconNum;
                note = singleData.getWeatherDescription().getDescription();
            }
        }
        String iconCode = icon + "d";
        iconCode = iconCode.length() < 3 ? "0".concat(iconCode) : iconCode;
        Date date = WeatherDataUtils.getJavaDateFromUnixTime(dailyData.get(0).getDt());
        String formatedDate = new SimpleDateFormat("EEE dd MMM", Locale.US).format(date);

        return new DayAverageValues(iconCode, maxTemp, minTemp, cloudsSum / DATA_PER_DAY,
                pressureSum / DATA_PER_DAY, windSum / DATA_PER_DAY, note, formatedDate);
    }

    public static Uri getCurrentCityUrl() {
        Integer cityId = SharedPrefsUtils.getInteger(Constants.CURRENT_CITY_ID);
        return Uri.parse("https://openweathermap.org/city/" + cityId);
    }
}
