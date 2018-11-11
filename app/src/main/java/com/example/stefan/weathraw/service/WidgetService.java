package com.example.stefan.weathraw.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.model.WidgetDataModel;
import com.example.stefan.weathraw.ui.widget.WeatherWidgetProvider;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.WeatherDataUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WidgetService extends IntentService {

    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_UPDATE_RESPONSE = "ACTION_UPDATE_RESPONSE";
    public static final String EXTRA_WEATHER_DATA = "EXTRA_WEATHER_DATA";
    public static final String ACTION_REFRESH = "ACTION_REFRESH";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public WidgetService() {
        super("WidgetService");
    }

    public WidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        if (intent.getAction().equals(ACTION_UPDATE)) {
            getWidgetData(new Consumer<WeatherData>() {
                @Override
                public void accept(WeatherData weatherData) throws Exception {
                    sendDataToWidgetProvider(weatherData);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

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

    private void getWidgetData(Consumer<WeatherData> onSuccess, Consumer<Throwable> onError) {
        compositeDisposable.add(ApiManager.getInstance().getCurrentWeatherByCityId(Constants.CITY_NIS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError));
    }

}
