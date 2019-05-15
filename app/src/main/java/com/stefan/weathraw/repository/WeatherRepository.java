package com.stefan.weathraw.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.stefan.weathraw.api.ApiManager;
import com.stefan.weathraw.cache.LocalDatabase;
import com.stefan.weathraw.cache.dao.WeatherDao;
import com.stefan.weathraw.cache.mapper.WeatherDataMapper;
import com.stefan.weathraw.cache.model.WeatherDataLocal;
import com.stefan.weathraw.model.FiveDayCityForecast;
import com.stefan.weathraw.model.WeatherAndForecast;
import com.stefan.weathraw.model.WeatherData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepository {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private WeatherDao weatherDao = LocalDatabase.getInstance().weatherDao();
    private OnResultListener listener;

    public WeatherRepository (OnResultListener listener) {
        this.listener = listener;
    }

    public void getAllWeatherDataInZip(long id, BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast> zipFun, SingleObserver<WeatherAndForecast> observer) {
        Single.zip(ApiManager.getInstance().getCurrentWeatherByCityId(id), ApiManager.getInstance().getFiveDayForecastByCityId(id), zipFun)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void getWeatherDataFromServer(int cityId) {
        getAllWeatherDataInZip(cityId,
                new BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast>() {
                    @Override
                    public WeatherAndForecast apply(WeatherData weatherData, FiveDayCityForecast forecast) throws Exception {
                        weatherData.setLastResponseTime(new SimpleDateFormat("dd.MM. - HH:mm", Locale.US).format(new Date()));
                        return new WeatherAndForecast(weatherData, forecast);
                    }
                },
                new SingleObserver<WeatherAndForecast>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(WeatherAndForecast completeWeatherData) {
                        saveToCache(completeWeatherData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                });
    }

    public LiveData<WeatherAndForecast> getData(int cityId) {
        getWeatherDataFromServer(cityId);

        return Transformations.map(weatherDao.getCashedWeatherData(), new Function<List<WeatherDataLocal>, WeatherAndForecast>() {
            @Override
            public WeatherAndForecast apply(List<WeatherDataLocal> input) {
                return WeatherDataMapper.toRemote(input);
            }
        });
    }

    private void saveToCache(final WeatherAndForecast completeWeatherData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                weatherDao.deleteAllData();
                weatherDao.insertWeatherData(WeatherDataMapper.toLocal(completeWeatherData));
            }
        }).start();
    }

    private void addDisposable(Disposable d) {
        this.compositeDisposable.add(d);
    }

    public void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public interface OnResultListener {
        void onError(Throwable throwable);
    }

}

/**
 * Napraviti BaseRepository u koji ce da se nalazi logika za dispose-ovanje i za error handling(interfejs)
 */
