package com.example.stefan.weathraw.api;

import com.example.stefan.weathraw.utils.Constants;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static ApiInterface instance;

    private static ApiInterface getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build()
                    .create(ApiInterface.class);
        }
        return instance;
    }


}
