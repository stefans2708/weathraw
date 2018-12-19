package com.example.stefan.weathraw.model;

import com.example.stefan.weathraw.cashe.model.City;

import java.util.List;

public class CityList {

    private List<City> cities;

    public CityList(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
