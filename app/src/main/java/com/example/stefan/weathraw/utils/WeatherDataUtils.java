package com.example.stefan.weathraw.utils;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.WeatherApplication;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

    public static Double getStandardTemperature(Double apiTemp) {
        return apiTemp - 273.15;
    }

    public static String getFormattedTemperature(Double apiTemp) {
        return getOneDecimalFormatter().format(getStandardTemperature(apiTemp)) + WeatherApplication.getAppContext().getString(R.string.degree_celsius);
    }

    public static String getIconUrl(String iconId) {
        return "http://openweathermap.org/img/w/"+iconId+".png";
    }

    public static String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy \n HH:mm:ss", Locale.US).format(date);
    }

}
