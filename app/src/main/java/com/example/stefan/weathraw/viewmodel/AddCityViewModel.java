package com.example.stefan.weathraw.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.cache.repository.CityRepository;

import java.util.List;

public class AddCityViewModel extends BaseViewModel {

    private CityRepository cityRepository = new CityRepository();
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private LiveData<List<City>> resultCities = Transformations.switchMap(searchQuery,
            new Function<String, LiveData<List<City>>>() {
                @Override
                public LiveData<List<City>> apply(String query) {
                    if (query.isEmpty()) {
                        return cityRepository.getAllCities();
                    } else {
                        return cityRepository.searchCities(query);
                    }
                }
            });

    public AddCityViewModel() {
        searchQuery.setValue("");
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<List<City>> getCities() {
        return resultCities;
    }
}


//    //dobro za paginaciju, umesto boolean moze da se stavi int koji ce da oznacava koja stranica je potrebna
//    private MutableLiveData<Boolean> citiesRequest = new MutableLiveData<>();
//    private LiveData<List<City>> cities = Transformations.switchMap(citiesRequest, new Function<Boolean, LiveData<List<City>>>() {
//        @Override
//        public LiveData<List<City>> apply(Boolean input) {
//            return cityRepository.getAllCities();
//        }
//    });