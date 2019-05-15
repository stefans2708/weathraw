package com.stefan.weathraw.model;

import com.stefan.weathraw.cache.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityList {

    private List<City> cities;

    public CityList() {
        cities = new ArrayList<>();
    }

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
