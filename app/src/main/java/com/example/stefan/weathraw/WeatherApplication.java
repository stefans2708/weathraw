package com.example.stefan.weathraw;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class WeatherApplication extends Application {

    public static final String CHANNEL_ID = "kanal";
    private static WeatherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createNotificationChannels();
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "imeKanala", NotificationManager.IMPORTANCE_MIN);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
