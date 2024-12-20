package com.example.nearme.data.datasources.remote;

import com.example.nearme.data.models.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGooglePlacesService {
    @GET("place/nearbysearch/json")
    Call<PlaceResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("types") String types,
            @Query("key") String apiKey
    );
}
