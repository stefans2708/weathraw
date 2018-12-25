package com.example.stefan.weathraw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.model.CityList;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.ResponseMessage;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.repository.WeatherRepository;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class CityDataViewModel extends BaseViewModel {

    private WeatherRepository repository = new WeatherRepository();
    private MutableLiveData<WeatherAndForecast> weatherLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseMessage> errorResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> bottomMenuState = new MutableLiveData<>();
    private int cityId;

    public CityDataViewModel() {
        setBottomMenuState(false);
        cityId = SharedPrefsUtils.getInteger(Constants.CURRENT_CITY_ID);       //todo: zahtevati paljenje gps-a ili otvoriti acitivity za izbor grada
        if (cityId == -1) {
            SharedPrefsUtils.putInteger(Constants.CURRENT_CITY_ID, Constants.CITY_NIS);
            cityId = Constants.CITY_NIS;
        }
        getData(cityId);
    }

    public void getData(int cityId) {
        this.cityId = cityId;
        repository.getAllWeatherDataInZip(cityId,
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
                        weatherLiveData.setValue(completeWeatherData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMessage(e);
                    }
                });
    }

    public void refreshData() {
        getData(cityId);
    }

    public void onBottomMenuToggleClick() {
        Boolean state = bottomMenuState.getValue();
        bottomMenuState.setValue(state == null || !state);
    }

    //region getters and setters

    public LiveData<WeatherAndForecast> getWeatherLiveData() {
        return weatherLiveData;
    }

    public LiveData<ResponseMessage> getErrorData() {
        return errorResponse;
    }

    public LiveData<Boolean> getBottomMenuState() {
        return bottomMenuState;
    }

    public void setBottomMenuState(Boolean expanded) {
        this.bottomMenuState.setValue(expanded);
    }

    public void updateFavouritesList(City city) {
        CityList cityList = SharedPrefsUtils.getObject(Constants.SELECTED_CITIES, CityList.class);
        List<City> cities = cityList == null ? new ArrayList<City>() : cityList.getCities();
        cities.add(city);
        SharedPrefsUtils.putObject(Constants.SELECTED_CITIES, cityList);
    }

    //endregion
}
