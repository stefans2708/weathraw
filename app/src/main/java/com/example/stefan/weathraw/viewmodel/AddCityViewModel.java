package com.example.stefan.weathraw.viewmodel;

import android.arch.lifecycle.LiveData;

import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.cashe.repository.CityRepository;

import java.util.List;

public class AddCityViewModel extends BaseViewModel {

    private LiveData<List<City>> cities;
    private CityRepository cityRepository = new CityRepository();

    public AddCityViewModel() {
        cities = cityRepository.getAllCities();
    }

    public LiveData<List<City>> getCities() {
        return cities;
    }

}
