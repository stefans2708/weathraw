package com.example.stefan.weathraw.viewmodel;

import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.cashe.repository.CityRepository;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel {

    private CityRepository cityRepository = new CityRepository();

    public MainViewModel() {
        if (!SharedPrefsUtils.getBoolean(Constants.FLAG_CITIES_LOADED)) {
            loadDatabaseWithCityData();
        }
    }

    private void loadDatabaseWithCityData() {
        String jsonArray = getJsonFromFile();

        Type listType = new TypeToken<ArrayList<City>>() {
        }.getType();
        final List<City> cities = new Gson().fromJson(jsonArray, listType);

        new Thread(new Runnable() {
            @Override
            public void run() {
                cityRepository.insertAllCities(cities);
                SharedPrefsUtils.putBoolean(Constants.FLAG_CITIES_LOADED, true);
            }
        }).start();
    }

    private String getJsonFromFile() {
        String json = null;
        try {
            InputStream inputStream = WeatherApplication.getAppContext().getAssets().open(Constants.FILE_CITIES);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


}
