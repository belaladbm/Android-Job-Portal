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
import androidx.core.content.ContextCompat;
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

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String USER_EMAIL_KEY = "userEmail";
    private static final String LOCATION_LABEL = "Your Location : ";

    private TextView welcomeText;
    private TextView nearbyJobsText;
    private RecyclerView jobsDisplay;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private String email;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //initializing dashboard page UI elements
        welcomeText = findViewById(R.id.dashboardText);
        TextView locationText = findViewById(R.id.locationTextView);
        nearbyJobsText = findViewById(R.id.nearbyJobsTextView);
        jobsDisplay = findViewById(R.id.jobsDisplayView);
        Button postJobButton = findViewById(R.id.postJobButton);
        Button buttonViewJobs = findViewById(R.id.buttonViewJobs);
        Button search = findViewById(R.id.search);
        Button settingsButton = findViewById(R.id.settingsButton);

        // Get the email from intent
        email = getIntent().getStringExtra(USER_EMAIL_KEY);
        // Initializing RecyclerView for jobs
        jobList = new ArrayList<>();
        // In your DashboardActivity's onCreate method, update the JobAdapter initialization:
        jobAdapter = new JobAdapter(jobList, job -> {
            if (job.getLocation() != null && !job.getLocation().isEmpty()) {
                Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(job.getLocation() + " Canada", 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);

                        Intent intent = new Intent(DashboardActivity.this, GoogleMapActivity.class);
                        intent.putExtra("latitude", address.getLatitude());
                        intent.putExtra("longitude", address.getLongitude());
                        intent.putExtra("jobTitle", job.getTitle());
                        intent.putExtra("location", job.getLocation());
                        intent.putExtra(USER_EMAIL_KEY, email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DashboardActivity.this,
                                "Could not find location: " + job.getLocation(),
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
        }, email);
        jobsDisplay.setLayoutManager(new LinearLayoutManager(this));
        jobsDisplay.setAdapter(jobAdapter);

        // Initialize location manager
        //Referred from Tutorial 6 - GoogleMaps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initializeLocationListener(locationText);
        checkLocationPermissionAndStartUpdates();

        moveToSettingsPage(settingsButton);

        //if there is not email
        if (email == null || email.isEmpty()) {
            welcomeText.setText(getDefaultMessage());
        }

        //view posted jobs when logged in as an employer
        buttonViewJobs.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EmployerJobsActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
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
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        welcomeText.setText(getMessage(firstName, role));

                        if ("Employee".equals(role)) {
                            locationText.setVisibility(View.VISIBLE);
                            nearbyJobsText.setVisibility(View.VISIBLE);
                            jobsDisplay.setVisibility(View.VISIBLE);
                            postJobButton.setVisibility(View.GONE);
                            search.setVisibility(View.VISIBLE);
                            buttonViewJobs.setVisibility(View.GONE);
                            moveToSearchPage(search);
                        } else if ("Employer".equals(role)) {
                            locationText.setVisibility(View.GONE);
                            nearbyJobsText.setVisibility(View.GONE);
                            jobsDisplay.setVisibility(View.GONE);
                            postJobButton.setVisibility(View.VISIBLE);
                            search.setVisibility(View.GONE);
                            buttonViewJobs.setVisibility(View.VISIBLE);
                            moveToPostJobPage(postJobButton);
                        }
                    }
                } else {
                    welcomeText.setText(getDefaultMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                welcomeText.setText(getDefaultMessage());
                Toast.makeText(DashboardActivity.this, "Error while extracting data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initializeLocationListener(TextView locationText) {
        locationListener = location -> getLocation(location, locationText);
    }

    public static String getMessage(String firstName, String role) {
        if (firstName == null || role == null) {
            return "Welcome to QuickCash :)";
        }
        return "Welcome, " + firstName + "! Your role is " + role + "!";
    }

    public static String getDefaultMessage() {
        return "Welcome to QuickCash :)";
    }

    public void moveToSettingsPage(Button settingsPageButton) {
        settingsPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    public void moveToPostJobPage(Button postJobButton) {
        postJobButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, JobPostActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    public void moveToSearchPage(Button search) {
        search.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    private void checkLocationPermissionAndStartUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void getLocation(Location loc, TextView locationText) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> adds = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (!adds.isEmpty()) {
                Address add = adds.get(0);
                String userCity = add.getLocality();
                locationText.setText(LOCATION_LABEL + userCity);
                fetchNearbyJobs(userCity);
            } else {
                locationText.setText(LOCATION_LABEL + loc.getLongitude() + ", " + loc.getLatitude());
            }
        } catch (IOException e) {
            locationText.setText(LOCATION_LABEL + loc.getLongitude() + ", " + loc.getLatitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Unable to detect location. Please enable GPS to see nearby jobs.", Toast.LENGTH_LONG).show();
                nearbyJobsText.setText("Unable to detect location. Please enable GPS to see nearby jobs.");
                jobList.clear();
                jobAdapter.updateJobs(jobList);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

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

                    if (job != null && job.getLocation() != null &&
                            job.getLocation().toLowerCase().contains(userCity.toLowerCase())) {
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