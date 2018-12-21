package com.example.stefan.weathraw.cashe.repository;

import android.arch.lifecycle.LiveData;

import com.example.stefan.weathraw.cashe.LocalDatabase;
import com.example.stefan.weathraw.cashe.dao.CityDao;
import com.example.stefan.weathraw.cashe.model.City;

import java.util.List;

public class CityRepository {

    private CityDao cityDao = LocalDatabase.getInstance().cityDao();

    public void insertAllCities(List<City> cities) {
        cityDao.insertAll(cities);
    }

    public LiveData<List<City>> getCities(int page, int pageSize) {
        return cityDao.getPage(pageSize, page * pageSize);
    }

    public LiveData<List<City>> getAllCities() {
        return cityDao.getAll();
    }

}
