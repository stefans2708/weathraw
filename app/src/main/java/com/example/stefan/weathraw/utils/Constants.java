package com.example.stefan.weathraw.utils;

public class Constants {

    private static final String API_VERSION = "2.5";
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    public static final int CITY_NIS = 787657;
    public static final String FILE_CITIES = "RScities.json";
    public static final String FLAG_CITIES_LOADED = "FLAG_CITIES_LOADED";
    public static final String SELECTED_CITIES = "SELECTED_CITIES";
    public static final String CURRENT_CITY_ID = "CURRENT_CITY_ID";

    public static String getApiKey() {
        return "867b58713226291e35ea0471eee3dcf2";
    }
}
