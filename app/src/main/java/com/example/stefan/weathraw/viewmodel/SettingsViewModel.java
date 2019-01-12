package com.example.stefan.weathraw.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.cache.repository.CityRepository;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.model.CityList;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewModel extends BaseViewModel {

    private SingleLiveEvent<Boolean> changeCityClick = new SingleLiveEvent<>();
    private LiveData<String> currentWidgetCityName;
    private MutableLiveData<Integer> currentWidgetCityId = new MutableLiveData<>();
    private MutableLiveData<Boolean> autoUpdateStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> enableNotifications = new MutableLiveData<>();
    private ApplicationSettings settings;
    private CityRepository cityRepository = new CityRepository();

    public SettingsViewModel() {
        settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
        if (settings == null) {
            settings = ApplicationSettings.defaultValues();
        }

        currentWidgetCityName = Transformations.switchMap(currentWidgetCityId, new Function<Integer, LiveData<String>>() {
            @Override
            public LiveData<String> apply(Integer cityId) {
                return cityRepository.getCityNameById(cityId);
            }
        });

        currentWidgetCityId.setValue(settings.getWidgetCityId());
    }

    public void onNewCitySelected(int newCityId) {
        settings.setWidgetCityId(newCityId);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        currentWidgetCityId.setValue(newCityId);
    }

    public void updateFavouritesList(City city) {
        CityList cityList = SharedPrefsUtils.getObject(Constants.SELECTED_CITIES, CityList.class);
        List<City> cities = cityList == null ? new ArrayList<City>() : cityList.getCities();
        cities.add(city);
        cityList = new CityList(cities);
        SharedPrefsUtils.putObject(Constants.SELECTED_CITIES, cityList);
    }

    public void onCityClick() {
        changeCityClick.setValue(true);
    }

    public void onAutoRefreshClick(boolean isChecked) {
        settings.setWidgetAutoRefreshEnabled(isChecked);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        autoUpdateStatus.setValue(isChecked);
    }

    public void onEnableNotificationClick(boolean isChecked) {
        settings.setNotificationEnabled(isChecked);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        enableNotifications.setValue(isChecked);
    }

    public LiveData<Boolean> getChangeCityClick() {
        return changeCityClick;
    }

    public ApplicationSettings getSettings() {
        return settings;
    }

    public LiveData<String> getCurrentWidgetCityName() {
        return currentWidgetCityName;
    }

    public LiveData<Boolean> getAutoUpdateStatus() {
        return autoUpdateStatus;
    }

    public LiveData<Boolean> getNotificationsStatus() {
        return enableNotifications;
    }
}
