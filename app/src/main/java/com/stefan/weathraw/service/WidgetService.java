package com.stefan.weathraw.service;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.stefan.weathraw.api.ApiManager;
import com.stefan.weathraw.model.ApplicationSettings;
import com.stefan.weathraw.model.FiveDayCityForecast;
import com.stefan.weathraw.model.WeatherAndForecast;
import com.stefan.weathraw.model.WeatherData;
import com.stefan.weathraw.model.WidgetDataModel;
import com.stefan.weathraw.ui.widget.WeatherWidgetProvider;
import com.stefan.weathraw.ui.widget.WeatherWidgetProviderDark;
import com.stefan.weathraw.utils.Constants;
import com.stefan.weathraw.utils.GeneralUtils;
import com.stefan.weathraw.utils.SharedPrefsUtils;
import com.stefan.weathraw.utils.WeatherDataUtils;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.stefan.weathraw.ui.widget.BaseAppWidgetProvider.EXTRA_PROVIDER_TYPE;
import static com.stefan.weathraw.ui.widget.BaseAppWidgetProvider.EXTRA_PROVIDER_TYPE_DARK;
import static com.stefan.weathraw.ui.widget.BaseAppWidgetProvider.EXTRA_PROVIDER_TYPE_REGULAR;

public class WidgetService extends JobIntentService {

    public static final String ACTION_UPDATE_RESPONSE = "ACTION_UPDATE_RESPONSE";
    public static final String ACTION_ERROR = "ACTION_ERROR";
    public static final String ACTION_REFRESH = "ACTION_REFRESH";
    public static final String ACTION_UPDATED_SETTINGS = "ACTION_UPDATED_SETTINGS";
    public static final String EXTRA_WEATHER_DATA = "EXTRA_WEATHER_DATA";
    public static final String EXTRA_ERROR_TYPE = "EXTRA_ERROR_TYPE";
    public static final int ERROR_TYPE_NO_INTERNET = 1;
    public static final int ERROR_TYPE_REQUEST_TIMEOUT = 2;

    private static final int UNIQUE_JOB_ID = 1337;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int currentCityId;

    public static void enqueueJob(Context context, Intent work) {
        enqueueWork(context, WidgetService.class, UNIQUE_JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull final Intent intent) {
        if (intent.getAction() == null) return;

        if (intent.getAction().equals(ACTION_REFRESH)) {
            readWidgetSettings();

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
                        sendDataToWidgetProvider(intent.getIntExtra(EXTRA_PROVIDER_TYPE, -1), weatherAndForecast);
                    }
                }, new OnError(intent) {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        super.accept(throwable);
                    }
                });
            }

        }
    }

    private void readWidgetSettings() {
        ApplicationSettings settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
        if (settings == null) {
            settings = ApplicationSettings.defaultValues();
        }
        currentCityId = settings.getWidgetCityId();
    }

    private void sendDataToWidgetProvider(int providerType, WeatherAndForecast weatherData) {
        WidgetDataModel data = new WidgetDataModel();
        data.setCity(weatherData.getWeatherData().getName());
        data.setDescription(weatherData.getWeatherData().getWeatherDescription().getDescription());
        data.setIconId(weatherData.getWeatherData().getWeatherDescription().getIcon());
        data.setTemperature(weatherData.getWeatherData().getMain().getTemperature());

        List<WeatherData> nextFiveValues = WeatherDataUtils.extractNextNValues(weatherData.getForecastData(), 5);
        data.setNextHoursWeatherData(nextFiveValues);


        Intent dataIntent;
        switch (providerType) {
            case EXTRA_PROVIDER_TYPE_DARK: {
                dataIntent = new Intent(this, WeatherWidgetProviderDark.class);
                break;
            }
            default:
            case EXTRA_PROVIDER_TYPE_REGULAR: {
                dataIntent = new Intent(this, WeatherWidgetProvider.class);
                break;
            }
        }
        dataIntent.setAction(ACTION_UPDATE_RESPONSE);
        dataIntent.putExtra(EXTRA_WEATHER_DATA, data);
        sendBroadcast(dataIntent);
    }

    private void sendErrorToWidgetProvider(int providerType, int errorType) {
        Intent errorIntent;
        switch (providerType) {
            case EXTRA_PROVIDER_TYPE_DARK: {
                errorIntent = new Intent(this, WeatherWidgetProviderDark.class);
                break;
            }
            default:
            case EXTRA_PROVIDER_TYPE_REGULAR: {
                errorIntent = new Intent(this, WeatherWidgetProvider.class);
                break;
            }
        }
        errorIntent.setAction(ACTION_ERROR);
        errorIntent.putExtra(EXTRA_ERROR_TYPE, errorType);
        sendBroadcast(errorIntent);
    }

    private void getWidgetData(BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast> zipFun, Consumer<WeatherAndForecast> onSuccess, Consumer<Throwable> onError) {
        compositeDisposable.add(
                Single.zip(ApiManager.getInstance().getCurrentWeatherByCityId(currentCityId), ApiManager.getInstance().getFiveDayForecastByCityId(currentCityId), zipFun)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onSuccess, onError));
    }

    abstract class OnError implements Consumer<Throwable> {

        private Intent intent;

        OnError(Intent intent) {
            this.intent = intent;
        }

        @Override
        public void accept(Throwable throwable) throws Exception {
            if (!GeneralUtils.isNetworkAvailable()) {
                sendErrorToWidgetProvider(intent.getIntExtra(EXTRA_PROVIDER_TYPE, -1), ERROR_TYPE_NO_INTERNET);
                return;
            }

            if (throwable instanceof HttpException) {
                if (((HttpException) throwable).code() == 408) { //request time out
                    sendErrorToWidgetProvider(intent.getIntExtra(EXTRA_PROVIDER_TYPE, -1), ERROR_TYPE_REQUEST_TIMEOUT);
                }
            }
        }
    }

}
