package com.example.stefan.weathraw.cashe.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.stefan.weathraw.cashe.model.City;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insertAll(List<City> cities);

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAll();

    @Query("SELECT COUNT(*) FROM city WHERE id=787657")
    Integer getStartCity();

    @Query("SELECT * FROM city LIMIT :pageSize OFFSET :offset")
    LiveData<List<City>> getPage(int pageSize, int offset);

}
