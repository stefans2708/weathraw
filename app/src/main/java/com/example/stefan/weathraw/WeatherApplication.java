package com.example.stefan.weathraw;

import android.app.Application;
import android.content.Context;

public class WeatherApplication extends Application {

    private static WeatherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

}
