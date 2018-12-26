package com.example.stefan.weathraw.cache.mapper;

import com.example.stefan.weathraw.cache.model.WeatherDataLocal;
import com.example.stefan.weathraw.model.Clouds;
import com.example.stefan.weathraw.model.Coordinates;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.GeneralWeatherData;
import com.example.stefan.weathraw.model.MainWeatherData;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.model.WeatherDescription;
import com.example.stefan.weathraw.model.Wind;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataMapper {

    public static WeatherData getRemoteFromLocal(WeatherDataLocal local) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCoordinates(new Coordinates(local.getLatitude(), local.getLongitude()));

        List<WeatherDescription> weatherDescriptions = new ArrayList<>();
        WeatherDescription weatherDescription = new WeatherDescription();
        weatherDescription.setId(local.getWeatherId());
        weatherDescription.setDescription(local.getDescription());
        weatherDescription.setIcon(local.getIcon());
        weatherDescription.setName(local.getName());
        weatherDescriptions.add(weatherDescription);
        weatherData.setWeatherDescriptions(weatherDescriptions);

        MainWeatherData mainWeatherData = new MainWeatherData();
        mainWeatherData.setTemperature(local.getTemperature());
        mainWeatherData.setPressure(local.getPressure());
        mainWeatherData.setHumidity(local.getHumidity());
        mainWeatherData.setTempMax(local.getTempMax());
        mainWeatherData.setTempMin(local.getTempMin());
        weatherData.setMain(mainWeatherData);

        weatherData.setVisibility(local.getVisibility());
        weatherData.setWind(new Wind(local.getWindSpeed(), local.getWindDeg()));
        weatherData.setClouds(new Clouds(local.getCloudPercent()));
        weatherData.setDt(local.getDt());

        GeneralWeatherData generalWeatherData = new GeneralWeatherData();
        generalWeatherData.setCountry(local.getCountry());
        generalWeatherData.setSunrise(local.getSunrise());
        generalWeatherData.setSunset(local.getSunset());
        weatherData.setSys(generalWeatherData);

        weatherData.setName(local.getCityName());
        weatherData.setId(local.getId());
        weatherData.setCod(local.getResultCode());
        weatherData.setDate(local.getDate());

        return weatherData;
    }

    public static WeatherDataLocal getLocalFromRemote(WeatherData weatherData) {
        WeatherDataLocal local = new WeatherDataLocal();
        local.setId(weatherData.getId());
        local.setLatitude(weatherData.getCoordinates() != null ? weatherData.getCoordinates().getLatitude() : 0);
        local.setLongitude(weatherData.getCoordinates() != null ? weatherData.getCoordinates().getLongitude() : 0);
        local.setWeatherId(weatherData.getWeatherDescription().getId());
        local.setName(weatherData.getWeatherDescription().getName());
        local.setDescription(weatherData.getWeatherDescription().getDescription());
        local.setIcon(weatherData.getWeatherDescription().getIcon());
        local.setTemperature(weatherData.getMain() != null ? weatherData.getMain().getTemperature() : 0);
        local.setPressure(weatherData.getMain() != null ? weatherData.getMain().getPressure() : 0);
        local.setHumidity(weatherData.getMain() != null ? weatherData.getMain().getHumidity() : 0);
        local.setTempMin(weatherData.getMain() != null ? weatherData.getMain().getTempMin() : 0);
        local.setTempMax(weatherData.getMain() != null ? weatherData.getMain().getTempMax() : 0);
        local.setVisibility(weatherData.getVisibility());
        local.setWindSpeed(weatherData.getWind() != null ? weatherData.getWind().getSpeed() : 0);
        local.setWindDeg(weatherData.getWind() != null ? weatherData.getWind().getDeg() : 0);
        local.setCloudPercent(weatherData.getClouds() != null ? weatherData.getClouds().getCloudPercent() : 0);
        local.setDt(weatherData.getDt());
        local.setCountry(weatherData.getSys() != null ? weatherData.getSys().getCountry() : "");
        local.setSunrise(weatherData.getSys() != null && weatherData.getSys().getSunrise() != null ? weatherData.getSys().getSunrise() : 0);
        local.setSunset(weatherData.getSys() != null && weatherData.getSys().getSunset() != null ?weatherData.getSys().getSunset() : 0);
        local.setCityName(weatherData.getName());
        local.setResultCode(weatherData.getCod());
        local.setDate(weatherData.getDate());
        return local;
    }

    public static List<WeatherDataLocal> toLocal(WeatherAndForecast weatherAndForecast) {
        List<WeatherDataLocal> list = new ArrayList<>();
        list.add(getLocalFromRemote(weatherAndForecast.getWeatherData()));
        if (weatherAndForecast != null && weatherAndForecast.getForecastData() != null && weatherAndForecast.getForecastData().getList() != null) {
            for (WeatherData weatherData : weatherAndForecast.getForecastData().getList()) {
                list.add(getLocalFromRemote(weatherData));
            }
        }
        return list;
    }

    public static WeatherAndForecast toRemote(List<WeatherDataLocal> locals) {
        WeatherAndForecast weatherAndForecast = new WeatherAndForecast();
        if (locals != null && locals.size() > 0) {
            weatherAndForecast.setWeatherData(getRemoteFromLocal(locals.get(0)));
            FiveDayCityForecast forecast = new FiveDayCityForecast();
            List<WeatherData> weatherData = new ArrayList<>();
            for (int i = 1; i < locals.size(); i++) {
                weatherData.add(getRemoteFromLocal(locals.get(i)));
            }
            forecast.setList(weatherData);
            weatherAndForecast.setForecastData(forecast);
        }
        return weatherAndForecast;
    }

}
