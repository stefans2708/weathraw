package com.example.stefan.weathraw.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.cashe.repository.CityRepository;

import java.util.List;

public class AddCityViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> citiesRequest = new MutableLiveData<>();
    private CityRepository cityRepository = new CityRepository();
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private LiveData<List<City>> searchResultCities = Transformations.switchMap(searchQuery,
            new Function<String, LiveData<List<City>>>() {
                @Override
                public LiveData<List<City>> apply(String query) {
                    return cityRepository.searchCities(query);
                }
            });
    //dobro za paginaciju, umesto boolean moze da se stavi int koji ce da oznacava koja stranica je potrebna
    private LiveData<List<City>> cities = Transformations.switchMap(citiesRequest, new Function<Boolean, LiveData<List<City>>>() {
        @Override
        public LiveData<List<City>> apply(Boolean input) {
            return cityRepository.getAllCities();
        }
    });

    public AddCityViewModel() {
        citiesRequest.setValue(true);
    }

    public LiveData<List<City>> getCities() {
        return cities;
    }

    public void setSearchQuery(String query) {
        if (query.isEmpty()) {
            citiesRequest.setValue(true);
        } else {
            searchQuery.setValue(query);
        }
    }

    public LiveData<List<City>> getSearchResults() {
        return searchResultCities;
    }
}
