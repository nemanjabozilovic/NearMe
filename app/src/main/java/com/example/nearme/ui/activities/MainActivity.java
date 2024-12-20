package com.example.nearme.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.nearme.R;
import com.example.nearme.data.datasources.remote.ApiService;
import com.example.nearme.data.models.PlaceResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Spinner placesSpinner;
    private Button searchButton;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApiKey();
        initializeUIElements();
        setupListeners();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getApiKey() {
        apiKey = getString(R.string.google_api_key);
    }

    private void initializeUIElements() {
        placesSpinner = findViewById(R.id.places_spinner);
        searchButton = findViewById(R.id.search_button);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList("Hospital", "Restaurant", "Shopping Mall", "Gym"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placesSpinner.setAdapter(adapter);
    }

    private void setupListeners() {
        searchButton.setOnClickListener(v -> {
            String selectedPlaceType = placesSpinner.getSelectedItem().toString();
            searchNearbyPlaces(selectedPlaceType);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        try {
            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(this, location -> {
                if (location != null && mMap != null) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                } else {
                    Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                Toast.makeText(this, "Permission denied. Location features will be unavailable.", Toast.LENGTH_SHORT).show();
                searchButton.setEnabled(false);
            }
        }
    }

    private void searchNearbyPlaces(String placeType) {
        String placeTypeCode = getPlaceTypeCode(placeType);
        if (placeTypeCode != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String locationString = latitude + "," + longitude;

                        getNearbyPlaces(locationString, placeTypeCode);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private String getPlaceTypeCode(String placeType) {
        switch (placeType) {
            case "Hospital":
                return "hospital";
            case "Restaurant":
                return "restaurant";
            case "Shopping Mall":
                return "shopping_mall";
            case "Gym":
                return "gym";
            default:
                return null;
        }
    }

    public void getNearbyPlaces(String userLocation, String placeType) {
        int radius = 1000;

        Call<PlaceResponse> call = ApiService.getGooglePlacesApi().getNearbyPlaces(userLocation, radius, placeType, apiKey);
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlaceResponse placeResponse = response.body();
                    handleNearbyPlaces(placeResponse);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch places", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Error fetching places", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleNearbyPlaces(PlaceResponse placeResponse) {
        if (placeResponse.getResults() != null && !placeResponse.getResults().isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (PlaceResponse.Place place : placeResponse.getResults()) {
                LatLng placeLocation = new LatLng(place.getGeometry().getLocation().getLat(),
                        place.getGeometry().getLocation().getLng());
                builder.include(placeLocation);
                mMap.addMarker(new MarkerOptions().position(placeLocation).title(place.getName()));
            }

            LatLngBounds bounds = builder.build();
            int padding = 100;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cameraUpdate);
        } else {
            Toast.makeText(MainActivity.this, "No nearby places found.", Toast.LENGTH_SHORT).show();
        }
    }
}