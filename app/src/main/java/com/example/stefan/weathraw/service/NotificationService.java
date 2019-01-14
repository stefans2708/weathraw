package com.example.stefan.weathraw.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.example.stefan.weathraw.NotificationBroadcastReceiver;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.ui.activity.MainActivity;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.GeneralUtils;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.utils.WeatherDataUtils;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class NotificationService extends JobIntentService {

    public static final int UNIQUE_JOB_ID = 1234;

    private int currentCityId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void enqueueJob(Context context, Intent intent) {
        enqueueWork(context, NotificationService.class, UNIQUE_JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.getAction() == null) return;

        if (intent.getAction().equals(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION)) {
            currentCityId = SharedPrefsUtils.getInteger(Constants.CURRENT_CITY_ID);

            if (GeneralUtils.isNetworkAvailable()) {
                getWidgetData(new BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast>() {
                    @Override
                    public WeatherAndForecast apply(WeatherData weatherData, FiveDayCityForecast fiveDayCityForecast) {
                        return new WeatherAndForecast(weatherData, fiveDayCityForecast);
                    }
                }, new Consumer<WeatherAndForecast>() {
                    @Override
                    public void accept(WeatherAndForecast weatherAndForecast) {
                        if (weatherAndForecast == null || weatherAndForecast.getWeatherData() == null
                                || weatherAndForecast.getForecastData() == null)
                            return;
                        createNotification(weatherAndForecast);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                });
            }
        }
    }

    private void getWidgetData(BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast> zipFun, Consumer<WeatherAndForecast> onSuccess, Consumer<Throwable> onError) {
        compositeDisposable.add(
                Single.zip(ApiManager.getInstance().getCurrentWeatherByCityId(currentCityId), ApiManager.getInstance().getFiveDayForecastByCityId(currentCityId), zipFun)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onSuccess, onError));
    }

    private void createNotification(WeatherAndForecast weatherData) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 32,new Intent(this, MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon_sun)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(getCollapsedView(weatherData))
                .setCustomBigContentView(getExpandedView(weatherData))
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(Constants.NOTIFICATION_ID, notification);
    }


    private RemoteViews getCollapsedView(WeatherAndForecast weatherData) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_colapsed);
        remoteViews.setTextViewText(R.id.txt_city, weatherData.getWeatherData().getName());
        remoteViews.setTextViewText(R.id.txt_weather_description, weatherData.getWeatherData().getWeatherDescription().getDescription());
        remoteViews.setTextViewText(R.id.txt_temperature, WeatherDataUtils.getTemperatureFormat(weatherData.getWeatherData().getMain().getTemperature()));
        loadNotificationImage(remoteViews, R.id.img_weather, weatherData.getWeatherData().getWeatherDescription().getIcon());

        return remoteViews;
    }

    private RemoteViews getExpandedView(WeatherAndForecast weatherData) {
        //current day data
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        remoteViews.setTextViewText(R.id.txt_city, weatherData.getWeatherData().getName());
        remoteViews.setTextViewText(R.id.txt_weather_description, weatherData.getWeatherData().getWeatherDescription().getDescription());
        remoteViews.setTextViewText(R.id.txt_temperature, WeatherDataUtils.getTemperatureFormat(weatherData.getWeatherData().getMain().getTemperature()));
        loadNotificationImage(remoteViews, R.id.img_weather, weatherData.getWeatherData().getWeatherDescription().getIcon());

        List<WeatherData> nextFourValues = WeatherDataUtils.extractNextFourValues(weatherData.getForecastData());
        WeatherData hourValue = nextFourValues.get(0);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_1, WeatherDataUtils.getHourMinuteFromUnixTime(hourValue.getDt()));
        remoteViews.setTextViewText(R.id.txt_data_1_temperature, WeatherDataUtils.getTemperatureFormat(hourValue.getMain().getTemperature()));
        remoteViews.setTextViewText(R.id.txt_data_1_description, hourValue.getWeatherDescription().getDescription());
        loadNotificationImage(remoteViews, R.id.img_next_data_1, hourValue.getWeatherDescription().getIcon());
        hourValue = nextFourValues.get(1);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_2, WeatherDataUtils.getHourMinuteFromUnixTime(hourValue.getDt()));
        remoteViews.setTextViewText(R.id.txt_data_2_temperature, WeatherDataUtils.getTemperatureFormat(hourValue.getMain().getTemperature()));
        remoteViews.setTextViewText(R.id.txt_data_2_description, hourValue.getWeatherDescription().getDescription());
        loadNotificationImage(remoteViews, R.id.img_next_data_2, hourValue.getWeatherDescription().getIcon());
        hourValue = nextFourValues.get(2);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_3, WeatherDataUtils.getHourMinuteFromUnixTime(hourValue.getDt()));
        remoteViews.setTextViewText(R.id.txt_data_3_temperature, WeatherDataUtils.getTemperatureFormat(hourValue.getMain().getTemperature()));
        remoteViews.setTextViewText(R.id.txt_data_3_description, hourValue.getWeatherDescription().getDescription());
        loadNotificationImage(remoteViews, R.id.img_next_data_3, hourValue.getWeatherDescription().getIcon());
        hourValue = nextFourValues.get(3);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_4, WeatherDataUtils.getHourMinuteFromUnixTime(hourValue.getDt()));
        remoteViews.setTextViewText(R.id.txt_data_4_temperature, WeatherDataUtils.getTemperatureFormat(hourValue.getMain().getTemperature()));
        remoteViews.setTextViewText(R.id.txt_data_4_description, hourValue.getWeatherDescription().getDescription());
        loadNotificationImage(remoteViews, R.id.img_next_data_4, hourValue.getWeatherDescription().getIcon());

        return remoteViews;
    }

    private void loadNotificationImage(RemoteViews remoteViews, int imageId, String imageUrl) {
        AssetManager assets = WeatherApplication.getAppContext().getAssets();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(assets.open(imageUrl + ".png"));
            remoteViews.setImageViewBitmap(imageId, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
