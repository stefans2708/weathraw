package com.example.stefan.weathraw.repository;

import com.example.stefan.weathraw.api.ApiManager;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepository {

    public void getCurrentWeatherByCityId(long id, SingleObserver<WeatherData> observer) {
        ApiManager.getInstance().getCurrentWeatherByCityId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getFiveDayForecastByCityId(long id, SingleObserver<FiveDayCityForecast> observer) {
        ApiManager.getInstance().getFiveDayForecastByCityId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getAllWeatherDataInZip(long id, BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast> zipFun, SingleObserver<WeatherAndForecast> observer) {
        Single.zip(ApiManager.getInstance().getCurrentWeatherByCityId(id), ApiManager.getInstance().getFiveDayForecastByCityId(id),zipFun)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
