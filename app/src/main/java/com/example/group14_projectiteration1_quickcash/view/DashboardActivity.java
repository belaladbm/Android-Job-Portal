package com.example.group14_projectiteration1_quickcash.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * After successfully login user gets redirected to dashboard.
 * */

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button settingsButton;
    private TextView locationText;

    private TextView nearbyJobsText;

    private RecyclerView jobsDisplay;

    private String userLocationCity;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //initializing dashboard page UI elements
        welcomeText = findViewById(R.id.dashboardText);
        settingsButton = findViewById(R.id.settingsButton);
        locationText = findViewById(R.id.locationTextView);
        nearbyJobsText = findViewById(R.id.nearbyJobsTextView);
        jobsDisplay = findViewById(R.id.jobsDisplayView);


        //extracting email passed through intent
        String email = getIntent().getStringExtra("userEmail");

        // Initialize location manager
        //Referred from Tutorial 6 - GoogleMaps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initializeLocationListener();
        checkLocationPermissionAndStartUpdates();

        moveToSettingsPage(settingsButton, email);

        //if there is not email
        if (email == null || email.isEmpty()) {
            welcomeText.setText(getDefaultMessage());
        }

        //getting database reference
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User details");

        // making query
        Query userQuery = dbRef.orderByChild("email").equalTo(email);

        //executing query
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if user found
                if (dataSnapshot.exists()) {
                    //looping through database
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //getting data
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        //setting the welcome message
                        welcomeText.setText(getMessage(firstName, role));

                        //this would show different display for both roles
                        //View Visible referred from android studio docs
                        //URL: https://developer.android.com/reference/android/view/View
                        if("Employee".equals(role)){
                            locationText.setVisibility(View.VISIBLE);
                        }
                        else if("Employer".equals(role)){
                            locationText.setVisibility(View.GONE);
                        }
                    }
                }
                //if user is not found
                else {
                    //default welcome message
                    welcomeText.setText(getDefaultMessage());
                }
            }

            //if any error are there while extracting data from firebase database
            @Override
            public void onCancelled(DatabaseError error) {
                welcomeText.setText(getDefaultMessage());
                Toast.makeText(DashboardActivity.this, "Error while extracting data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize Location Listener
     * Referred from Tutorial 6 - GoogleMaps
     * */
    public void initializeLocationListener(){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLocation(location);
            }
        };
    }

    /**
     * Generated customized welcome message
     * @param firstName User's first name
     * @param role User's current role
     * @return Customized welcome message String.
     * */
    public static String getMessage(String firstName, String role) {
        return "Welcome, " + firstName + "! Your role is " + role + "!";
    }

    /**
     * @return Default Welcome message
     * */
    public static String getDefaultMessage() {
        return "Welcome to QuickCash :)";
    }

    /**
     * Moves the user to settings page
     * @param settingsPageButton Button to take user to settings page
     * @param email User's email
     * */
    public void moveToSettingsPage(Button settingsPageButton, String email) {
        settingsPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.putExtra("userEmail", email); //  Send user email to settings
            startActivity(intent);
        });
    }


    /**
     * Check permissions and start location updates
     * Referred from Tutorial 6 - GoogleMaps
     * */
    private void checkLocationPermissionAndStartUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            startLocationUpdates();
        }
    }

    /**
     * Start Receiving location updates
     * Referred from Tutorial 6 - GoogleMaps
     * */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    /**
     * This gets the current location of user using geocoder
     * URL: https://developer.android.com/reference/android/location/Geocoder
     * */
    public void getLocation(Location loc) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            //getfromlocation referred from android studio docs
            //URL: https://developer.android.com/reference/android/location/Geocoder
            List<Address> adds = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if(!adds.isEmpty()){
                Address add = adds.get(0);
                String userAddress = add.getAddressLine(0);
                userLocationCity = add.getLocality();
                locationText.setText("Your Location : " + userLocationCity);
            }
            else{
                locationText.setText("Your Location : " + loc.getLongitude() + ", "+ loc.getLatitude());
            }
        }
        catch (IOException e){
            locationText.setText("Your Location : " + loc.getLongitude() + ", "+ loc.getLatitude());
        }

    }

    /**
     *  Handles the result of location permission request
     *  Referred from Tutorial 6 - GoogleMaps
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
            else{
                Toast.makeText(this, "Unable to detect location. Please enable GPS to see nearby jobs.", Toast.LENGTH_LONG).show();
                locationText.setText("GPS Permission Denied! Please enable GPS to see nearby jobs.");
            }
        }
    }

    /**
     * Referred from Tutorial 6 - GoogleMaps
     * */
    @Override
    protected void onStop() {
        super.onStop();
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

}