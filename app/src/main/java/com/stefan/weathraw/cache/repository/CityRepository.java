package com.stefan.weathraw.cache.repository;

import androidx.lifecycle.LiveData;

import com.stefan.weathraw.cache.LocalDatabase;
import com.stefan.weathraw.cache.dao.CityDao;
import com.stefan.weathraw.cache.model.City;

import java.util.List;

public class CityRepository {

    private CityDao cityDao = LocalDatabase.getInstance().cityDao();

    public void insertAllCities(List<City> cities) {
        cityDao.insertAll(cities);
    }

//    public LiveData<List<City>> getCities(int page, int pageSize) {
//        return cityDao.getPage(pageSize, page * pageSize);
//    }

    public LiveData<List<City>> getAllCities() {
        return cityDao.getAll();
    }

    public LiveData<List<City>> searchCities(String query) {
        return cityDao.searchCities("%"+query+"%");
    }

    public LiveData<City> getCityById(int cityId) {
        return cityDao.getCityById(cityId);
    }

    public LiveData<String> getCityNameById(int cityId) {
        return cityDao.getCityNameById(cityId);
    }
}
