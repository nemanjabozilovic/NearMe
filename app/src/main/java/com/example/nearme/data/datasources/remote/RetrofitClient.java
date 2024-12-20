package com.example.nearme.data.datasources.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private static final String GooglePlacesApiBaseUrl = "https://maps.googleapis.com/maps/api/";

    private RetrofitClient() {
    }

    public static Retrofit getInstance(String baseUrl) {
        if (instance != null) {
            return instance;
        }

        synchronized (RetrofitClient.class) {
            if (instance == null) {
                instance = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }

        return instance;
    }

    public static Retrofit getGooglePlacesApiRetrofitInstance() {
        return  getInstance(GooglePlacesApiBaseUrl);
    }
}