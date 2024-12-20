package com.example.nearme.data.datasources.remote;

public class ApiService {
    private static IGooglePlacesService googlePlacesApi;
    public static IGooglePlacesService getGooglePlacesApi() {
        if (googlePlacesApi == null) {
            googlePlacesApi = RetrofitClient.getGooglePlacesApiRetrofitInstance().create(IGooglePlacesService.class);
        }

        return googlePlacesApi;
    }
}