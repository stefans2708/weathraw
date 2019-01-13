package com.example.stefan.weathraw.utils;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.WeatherApplication;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherDataUtils {

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
        return new SimpleDateFormat("HH:mm", Locale.US).format(new Date((long)seconds * 1000));
    }

}
