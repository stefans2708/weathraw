package com.example.stefan.weathraw.service;

import android.app.IntentService;
import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.model.WidgetDataModel;
import com.example.stefan.weathraw.ui.widget.WeatherWidgetProvider;
import com.example.stefan.weathraw.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class WidgetService extends IntentService {

    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_UPDATE_RESPONSE = "ACTION_UPDATE_RESPONSE";
    public static final String ACTION_ERROR = "ACTION_ERROR";
    public static final String EXTRA_WEATHER_DATA = "EXTRA_WEATHER_DATA";
    public static final String EXTRA_ERROR_TYPE = "EXTRA_ERROR_TYPE";
    public static final String ACTION_REFRESH = "ACTION_REFRESH";
    public static final int ERROR_TYPE_NO_INTERNET = 1;
    public static final int ERROR_TYPE_REQUEST_TIMEOUT = 2;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public WidgetService() {
        super("WidgetService");
    }

    public WidgetService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(23, createNotification());
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, WeatherApplication.CHANNEL_ID)
                .setContentTitle("Notifikacija")
                .setContentText("Tekst")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setSmallIcon(R.drawable.ic_location_on_black_24dp)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        if (intent.getAction().equals(ACTION_UPDATE)) {
//            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_weather);
//            remoteViews.setViewVisibility(R.id.progress_bar, View.VISIBLE);
//            remoteViews.setViewVisibility(R.id.image_refresh, View.GONE);
//            ComponentName componentName = new ComponentName(this, WeatherWidgetProvider.class);
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
//            for (int appWidgetId : appWidgetIds) {
//                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
//            }

            getWidgetData(new Consumer<WeatherData>() {
                @Override
                public void accept(WeatherData weatherData) throws Exception {
                    sendDataToWidgetProvider(weatherData);
                }
            }, new OnError() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    super.accept(throwable);
                }
            });
        }
    }

    private void sendDataToWidgetProvider(WeatherData weatherData) {
        WidgetDataModel data = new WidgetDataModel();
        data.setCity(weatherData.getName());
        data.setDescription(weatherData.getWeatherDescription().getDescription());
        data.setIconId(weatherData.getWeatherDescription().getIcon());
        data.setTemperature(weatherData.getMain().getTemperature());

        Intent dataIntent = new Intent(this, WeatherWidgetProvider.class);
        dataIntent.setAction(ACTION_UPDATE_RESPONSE);
        dataIntent.putExtra(EXTRA_WEATHER_DATA, data);
        sendBroadcast(dataIntent);
    }

    private void getWidgetData(Consumer<WeatherData> onSuccess, OnError onError) {
        compositeDisposable.add(ApiManager.getInstance().getCurrentWeatherByCityId(Constants.CITY_NIS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError));
    }

    private void sendErrorToWidgetProvider(int errorType) {
        Intent errorIntent = new Intent(this, WeatherWidgetProvider.class);
        errorIntent.setAction(ACTION_ERROR);
        errorIntent.putExtra(EXTRA_ERROR_TYPE, errorType);
        sendBroadcast(errorIntent);
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) WeatherApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    abstract class OnError implements Consumer<Throwable> {

        @Override
        public void accept(Throwable throwable) throws Exception {
            if (!isNetworkAvailable()) {
                sendErrorToWidgetProvider(ERROR_TYPE_NO_INTERNET);
                return;
            }

            if (throwable instanceof HttpException) {
                if (((HttpException) throwable).code() == 408) { //request time out
                    sendErrorToWidgetProvider(ERROR_TYPE_REQUEST_TIMEOUT);
//                    return;
                }
            }
        }
    }
}
