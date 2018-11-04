package com.example.stefan.weathraw.viewmodel;

import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.repository.WeatherRepository;
import com.example.stefan.weathraw.utils.Constants;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class CityDataViewModel extends BaseViewModel {

    private WeatherAndForecast weatherAndForecast;
    private WeatherRepository repository = new WeatherRepository();

    public CityDataViewModel() {
        getData();
    }

    private void getData() {
        repository.getAllWeatherDataInZip(Constants.CITY_NIS,
                new BiFunction<WeatherData, FiveDayCityForecast, WeatherAndForecast>() {
                    @Override
                    public WeatherAndForecast apply(WeatherData weatherData, FiveDayCityForecast forecast) throws Exception {
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
                        weatherAndForecast = completeWeatherData;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }



    //region getters and setters

    public WeatherAndForecast getWeatherAndForecast() {
        return weatherAndForecast;
    }

    public void setWeatherAndForecast(WeatherAndForecast weatherAndForecast) {
        this.weatherAndForecast = weatherAndForecast;
    }

    //endregion
}
