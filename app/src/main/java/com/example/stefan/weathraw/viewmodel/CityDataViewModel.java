package com.example.stefan.weathraw.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.model.CityList;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.repository.WeatherRepository;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class CityDataViewModel extends BaseViewModel implements WeatherRepository.OnResultListener {

    private WeatherRepository repository = new WeatherRepository(this);
    private LiveData<WeatherAndForecast> weatherLiveData;
    private SingleLiveEvent<Boolean> bottomMenuState = new SingleLiveEvent<>();
    private int cityId;

    public CityDataViewModel() {
        setBottomMenuState(false);
        getCurrentCityId();
        getData(cityId);
    }

    private void getCurrentCityId() {
        cityId = SharedPrefsUtils.getInteger(Constants.CURRENT_CITY_ID);
        if (cityId == -1) {                                                 //todo: zahtevati paljenje gps-a ili otvoriti acitivity za izbor grada
            SharedPrefsUtils.putInteger(Constants.CURRENT_CITY_ID, Constants.CITY_NIS);
            cityId = Constants.CITY_NIS;
        }
    }

    public void getData(int cityId) {
        this.cityId = cityId;
        weatherLiveData = repository.getData(cityId);
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
        cityList = new CityList(cities);
        SharedPrefsUtils.putObject(Constants.SELECTED_CITIES, cityList);
    }

    //endregion

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.dispose();
    }

    @Override
    public void onError(Throwable message) {
        setErrorMessage(message);
    }

}
