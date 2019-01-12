package com.example.stefan.weathraw.service;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.model.WidgetDataModel;
import com.example.stefan.weathraw.ui.widget.WeatherWidgetProvider;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class WidgetService extends JobIntentService {

    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_UPDATE_RESPONSE = "ACTION_UPDATE_RESPONSE";
    public static final String ACTION_ERROR = "ACTION_ERROR";
    public static final String EXTRA_WEATHER_DATA = "EXTRA_WEATHER_DATA";
    public static final String EXTRA_ERROR_TYPE = "EXTRA_ERROR_TYPE";
    public static final String ACTION_REFRESH = "ACTION_REFRESH";
    public static final int ERROR_TYPE_NO_INTERNET = 1;
    public static final int ERROR_TYPE_REQUEST_TIMEOUT = 2;

    private static final int UNIQUE_JOB_ID = 1337;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int currentCityId;
    private boolean autoRefreshingEnabled;

    public static void enqueueJob(Context context, Intent work) {
        enqueueWork(context, WidgetService.class, UNIQUE_JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.getAction() == null) return;

        if (intent.getAction().equals(ACTION_UPDATE)) {
            readWidgetSettings();

            getWidgetData(new Consumer<WeatherData>() {
                @Override
                public void accept(WeatherData weatherData) {
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

    private void readWidgetSettings() {
        ApplicationSettings settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
        if (settings == null) {
            settings = ApplicationSettings.defaultValues();
        }
        currentCityId = settings.getWidgetCityId();
        autoRefreshingEnabled = settings.isWidgetAutoRefreshEnabled();
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
        compositeDisposable.add(ApiManager.getInstance().getCurrentWeatherByCityId(currentCityId)
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
                }
            }
        }
    }

}
