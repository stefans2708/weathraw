package com.example.stefan.weathraw.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.stefan.weathraw.NotificationBroadcastReceiver;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.GeneralUtils;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NotificationService extends JobIntentService {

    private static final int UNIQUE_JOB_ID = 1234;

    private int currentCityId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void enqueueJob(Context context, Intent intent) {
        enqueueWork(context, NotificationService.class, UNIQUE_JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.getAction() == null) return;

        if (intent.getAction().equals(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION)) {
            readWidgetSettings();
            if (GeneralUtils.isNetworkAvailable()) {
                getWidgetData(new Consumer<WeatherData>() {
                    @Override
                    public void accept(WeatherData weatherData) {
                        createNotification(weatherData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                });
            }
        }
    }

    private void createNotification(WeatherData weatherData) {
        Notification notification = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sunrise)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Test")
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(Constants.NOTIFICATION_ID, notification);
    }

    private void readWidgetSettings() {
        ApplicationSettings settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
        if (settings == null) {
            settings = ApplicationSettings.defaultValues();
        }
        currentCityId = settings.getWidgetCityId();
    }

    private void getWidgetData(Consumer<WeatherData> onSuccess, Consumer<Throwable> onError) {
        compositeDisposable.add(ApiManager.getInstance().getCurrentWeatherByCityId(currentCityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError));
    }
}
