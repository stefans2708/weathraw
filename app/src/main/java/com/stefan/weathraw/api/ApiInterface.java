package com.stefan.weathraw.api;

import com.stefan.weathraw.model.FiveDayCityForecast;
import com.stefan.weathraw.model.WeatherData;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather")
    Single<WeatherData> getCurrentWeatherByCityId(@Query("id") long id);

    @GET("weather")
    Single<WeatherData> getCurrentWeatherByCityName(@Query("q") String name);

    @GET("forecast")
    Single<FiveDayCityForecast> getFiveDayForecastByCityId(@Query("id") long id);

    @GET("forecast")
    Single<FiveDayCityForecast> getFiveDayForecastByCityName(@Query("q") String name);

}
