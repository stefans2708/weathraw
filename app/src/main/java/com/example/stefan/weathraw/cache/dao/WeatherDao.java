package com.example.stefan.weathraw.cache.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.stefan.weathraw.cache.model.WeatherDataLocal;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert
    void insertWeatherData(List<WeatherDataLocal> data);

    @Query("SELECT * FROM weatherdatalocal ORDER BY dt ASC")
    LiveData<List<WeatherDataLocal>> getCashedWeatherData();

    @Query("DELETE FROM weatherdatalocal")
    void deleteAllData();

}
