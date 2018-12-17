package com.example.stefan.weathraw.cashe;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.cashe.dao.CityDao;
import com.example.stefan.weathraw.cashe.model.City;

@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;

    public static LocalDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(WeatherApplication.getAppContext(), LocalDatabase.class, "weathraw-database")
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract CityDao cityDao();

}
