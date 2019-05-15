package com.stefan.weathraw.cache.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.stefan.weathraw.cache.model.City;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insertAll(List<City> cities);

    @Query("SELECT * FROM city ORDER BY city.name")
    LiveData<List<City>> getAll();

    @Query("SELECT * FROM city WHERE city.name LIKE :query")
    LiveData<List<City>> searchCities(String query);

    @Query("SELECT * FROM city WHERE city.id = :id")
    LiveData<City> getCityById(int id);

    @Query("SELECT city.name FROM city WHERE city.id = :cityId")
    LiveData<String> getCityNameById(int cityId);
}
