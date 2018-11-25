package com.example.stefan.weathraw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.repository.WeatherRepository;
import com.example.stefan.weathraw.utils.Constants;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class CityDataViewModel extends BaseViewModel {

    private ObservableField<WeatherAndForecast> weatherAndForecast = new ObservableField<>();
    private WeatherRepository repository = new WeatherRepository();
    private MutableLiveData<WeatherAndForecast> weatherLiveData = new MutableLiveData<>();

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
                        weatherAndForecast.set(completeWeatherData);
                        weatherLiveData.setValue(completeWeatherData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CityDataViewModel.super.onError("Problems with loading weather data.");
                    }
                });
    }

    //region getters and setters


    public ObservableField<WeatherAndForecast> getWeatherAndForecast() {
        return weatherAndForecast;
    }

    public void setWeatherAndForecast(ObservableField<WeatherAndForecast> weatherAndForecast) {
        this.weatherAndForecast = weatherAndForecast;
    }

    public LiveData<WeatherAndForecast> getWeatherLiveData() {
        return weatherLiveData;
    }

    //endregion
}
