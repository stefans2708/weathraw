package com.example.stefan.weathraw.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stefan.weathraw.WeatherApplication;

public class SharedPrefsUtils {

    private static final String PREFS_NAME = "weathraw_prefs";

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = WeatherApplication.getAppContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void putString(String key, String value) {
        getInstance().edit().putString(key,value).apply();
    }

    public static String getString(String key) {
        return getInstance().getString(key, null);
    }

    public static void putBoolean(String key, Boolean value) {
        getInstance().edit().putBoolean(key, value).apply();
    }

    public static Boolean getBoolean(String key) {
        return getInstance().getBoolean(key, false);
    }


}
