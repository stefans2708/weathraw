package com.example.stefan.weathraw.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.example.stefan.weathraw.NotificationBroadcastReceiver;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.model.DayAverageValues;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.GeneralUtils;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.utils.WeatherDataUtils;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
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
                getWidgetData(new BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast>() {
                    @Override
                    public WeatherAndForecast apply(WeatherData weatherData, FiveDayCityForecast fiveDayCityForecast) throws Exception {
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

    private void readWidgetSettings() {
        ApplicationSettings settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
        if (settings == null) {
            settings = ApplicationSettings.defaultValues();
        }
        currentCityId = settings.getWidgetCityId();
    }

    private void createNotification(WeatherAndForecast weatherData) {
        Notification notification = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sunrise)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Test")
                .setAutoCancel(true)
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
        remoteViews.setImageViewResource(R.id.img_weather, R.drawable.ic_wind);
        return remoteViews;
    }

    private RemoteViews getExpandedView(WeatherAndForecast weatherData) {
        //current day data
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        remoteViews.setTextViewText(R.id.txt_city, weatherData.getWeatherData().getName());
        remoteViews.setTextViewText(R.id.txt_weather_description, weatherData.getWeatherData().getWeatherDescription().getDescription());
        remoteViews.setTextViewText(R.id.txt_temperature, WeatherDataUtils.getTemperatureFormat(weatherData.getWeatherData().getMain().getTemperature()));
        remoteViews.setImageViewUri(R.id.img_weather, Uri.parse(WeatherDataUtils.getOfflineIconUrl(weatherData.getWeatherData().getWeatherDescription().getIcon())));
        //days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
        String[] weekdays = dfs.getWeekdays();
        remoteViews.setTextViewText(R.id.txt_day_in_week_1, weekdays[calendar.get(Calendar.DAY_OF_WEEK)]);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        remoteViews.setTextViewText(R.id.txt_day_in_week_2, weekdays[calendar.get(Calendar.DAY_OF_WEEK)]);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        remoteViews.setTextViewText(R.id.txt_day_in_week_3, weekdays[calendar.get(Calendar.DAY_OF_WEEK)]);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        remoteViews.setTextViewText(R.id.txt_day_in_week_4, weekdays[calendar.get(Calendar.DAY_OF_WEEK)]);
        //images
        List<DayAverageValues> dayAverageValues = WeatherDataUtils.extractDayAverageValuesInList(weatherData.getForecastData());
        DayAverageValues oneDayValues = dayAverageValues.get(0);
        remoteViews.setImageViewUri(R.id.img_next_day_1, Uri.parse(WeatherDataUtils.getOfflineIconUrl(oneDayValues.getIcon())));
        remoteViews.setTextViewText(R.id.txt_day_1_min, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMinTemperature()));
        remoteViews.setTextViewText(R.id.txt_day_1_max, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMaxTemperature()));
        oneDayValues = dayAverageValues.get(1);
        remoteViews.setImageViewUri(R.id.img_next_day_2, Uri.parse(WeatherDataUtils.getOfflineIconUrl(oneDayValues.getIcon())));
        remoteViews.setTextViewText(R.id.txt_day_2_min, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMinTemperature()));
        remoteViews.setTextViewText(R.id.txt_day_2_max, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMaxTemperature()));
        oneDayValues = dayAverageValues.get(2);
        remoteViews.setImageViewUri(R.id.img_next_day_3, Uri.parse(WeatherDataUtils.getOfflineIconUrl(oneDayValues.getIcon())));
        remoteViews.setTextViewText(R.id.txt_day_3_min, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMinTemperature()));
        remoteViews.setTextViewText(R.id.txt_day_3_max, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMaxTemperature()));
        oneDayValues = dayAverageValues.get(3);
        remoteViews.setImageViewUri(R.id.img_next_day_4, Uri.parse(WeatherDataUtils.getOfflineIconUrl(oneDayValues.getIcon())));
        remoteViews.setTextViewText(R.id.txt_day_4_min, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMinTemperature()));
        remoteViews.setTextViewText(R.id.txt_day_4_max, WeatherDataUtils.getTemperatureFormat(oneDayValues.getMaxTemperature()));

        return remoteViews;
    }

}
