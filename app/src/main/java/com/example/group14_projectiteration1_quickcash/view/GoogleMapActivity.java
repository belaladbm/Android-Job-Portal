package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.example.group14_projectiteration1_quickcash.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * GoogleMapActivity displays a map with the job location marked.
 * This activity is opened when a user clicks on a job to see it on the map.
 */
public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private double latitude;
    private double longitude;

    /**
     * Called when the activity is first created.
     * Sets up the map fragment and back button to return to the dashboard.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        // Get latitude and longitude from intent
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        // Set up "Back to Dashboard" button
        Button backToDashboard = findViewById(R.id.backButton);
        backToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(GoogleMapActivity.this, DashboardActivity.class);
            String email = getIntent().getStringExtra("userEmail");
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });

        // Load the Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Call onMapReady() when map is ready
        }
    }

    /**
     * Called when the Google Map is ready.
     * Adds a marker for the job location and moves the camera to it.
     *
     * @param googleMap The GoogleMap object provided by the callback.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng jobLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jobLocation, 15)); // Zoom to job location
    }
}