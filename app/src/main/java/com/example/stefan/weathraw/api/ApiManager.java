package com.example.stefan.weathraw.api;

import android.support.annotation.NonNull;

import com.example.stefan.weathraw.utils.Constants;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static ApiInterface instance;

    public static ApiInterface getInstance() {
        if (instance == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request original = chain.request();
                            HttpUrl originalUrl = original.url();

                            HttpUrl newUrl = originalUrl.newBuilder().addQueryParameter("APPID", Constants.getApiKey()).build();
                            Request.Builder requestBuilder = original.newBuilder().url(newUrl);

                            Request newRequest = requestBuilder.build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(ApiInterface.class);
        }
        return instance;
    }


}
