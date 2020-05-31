package com.stefan.weathraw.cache.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.stefan.weathraw.cache.model.WeatherDataLocal;

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
