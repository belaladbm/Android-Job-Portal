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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.adapter.JobAdapter;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
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
    private Button postJobButton;
    private Button search;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private String email;
    private Button buttonViewJobs;


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
        postJobButton = findViewById(R.id.postJobButton);
        buttonViewJobs = findViewById(R.id.buttonViewJobs);
        search = findViewById(R.id.search);

        // Get the email from intent
        email = getIntent().getStringExtra("userEmail");
        // Initializing RecyclerView for jobs
        jobList = new ArrayList<>();
        // In your DashboardActivity's onCreate method, update the JobAdapter initialization:
        jobAdapter = new JobAdapter(jobList, new JobAdapter.OnJobClickListener() {
            @Override
            public void onJobClick(Job job) {
                if (job.location != null && !job.location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(job.location + " Canada", 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);

                            Intent intent = new Intent(DashboardActivity.this, GoogleMapActivity.class);
                            intent.putExtra("latitude", address.getLatitude());
                            intent.putExtra("longitude", address.getLongitude());
                            intent.putExtra("jobTitle", job.title);
                            intent.putExtra("location", job.location);
                            intent.putExtra("userEmail", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DashboardActivity.this,
                                    "Could not find location: " + job.location,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(DashboardActivity.this,
                                "Error finding location",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this,
                            "Location not available for this job",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, email);
        jobsDisplay.setLayoutManager(new LinearLayoutManager(this));
        jobsDisplay.setAdapter(jobAdapter);

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

        //view posted jobs when logged in as an employer
        buttonViewJobs.setOnClickListener(v->{
            Intent intent = new Intent(DashboardActivity.this, EmployerJobsActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });

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
                            nearbyJobsText.setVisibility(View.VISIBLE);
                            jobsDisplay.setVisibility(View.VISIBLE);
                            postJobButton.setVisibility(View.GONE);
                            search.setVisibility(View.VISIBLE);
                            buttonViewJobs.setVisibility(View.GONE);
                            moveToSearchPage(search, email);
                        }
                        else if("Employer".equals(role)){
                            locationText.setVisibility(View.GONE);
                            nearbyJobsText.setVisibility(View.GONE);
                            jobsDisplay.setVisibility(View.GONE);
                            postJobButton.setVisibility(View.VISIBLE);
                            search.setVisibility(View.GONE);
                            buttonViewJobs.setVisibility(View.VISIBLE);
                            moveToPostJobPage(postJobButton, email);
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
        if(firstName == null || role == null){
            return "Welcome to QuickCash :)";
        }
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
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });
    }

    /**
     * Moves the user to post job page
     * @param postJobButton Button to take user to settings page
     * @param email Employer's email.
     * */
    public void moveToPostJobPage(Button postJobButton, String email) {
        postJobButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, JobPostActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });
    }

    public void moveToSearchPage(Button search, String email){
        search.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
            intent.putExtra("userEmail", email);
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

                //calling nearby jobs method after getting user's location
                fetchNearbyJobs(userLocationCity);
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
                nearbyJobsText.setText("Unable to detect location. Please enable GPS to see nearby jobs.");
                jobList.clear();
                jobAdapter.updateJobs(jobList);
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

    /**
     * Fetches jobs from Firebase based on user's location
     * @param userCity The city where the user is located
     */
    public void fetchNearbyJobs(String userCity) {
        if (userCity == null || userCity.isEmpty()) {
            nearbyJobsText.setText("Unable to determine location");
            return;
        }

        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs:");

        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobList.clear();
                boolean jobsFound = false;

                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);

                    if (job != null && job.location != null &&
                            job.location.toLowerCase().contains(userCity.toLowerCase())) {
                        jobList.add(job);
                        jobsFound = true;
                    }
                }

                if (jobsFound) {
                    jobAdapter.updateJobs(jobList);
                    nearbyJobsText.setText("Nearby Jobs in " + userCity);
                } else {
                    nearbyJobsText.setText("No jobs found in " + userCity);
                    jobAdapter.updateJobs(jobList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "Error fetching jobs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}