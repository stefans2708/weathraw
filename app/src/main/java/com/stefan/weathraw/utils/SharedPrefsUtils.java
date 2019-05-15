package com.stefan.weathraw.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.stefan.weathraw.WeatherApplication;
import com.google.gson.Gson;

import java.lang.reflect.Type;

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


    public static void putInteger(String key, Integer value) {
        getInstance().edit().putInt(key,value).apply();
    }

    public static Integer getInteger(String key) {
        return getInstance().getInt(key, -1);
    }

    public static void putObject(String key, Object object) {
        getInstance().edit().putString(key, new Gson().toJson(object)).apply();
    }

    public static <T> T getObject(String key, Type type) {
        String result = getString(key);
        if (result != null) {
            return new Gson().fromJson(result, type);
        }
        return null;
    }

}
