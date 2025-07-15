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
 * DashboardActivity is the home screen after login.
 * It shows nearby jobs to employees or post/view buttons for employers.
 */
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

        // Setup UI elements
        welcomeText = findViewById(R.id.dashboardText);
        TextView locationText = findViewById(R.id.locationTextView);
        nearbyJobsText = findViewById(R.id.nearbyJobsTextView);
        jobsDisplay = findViewById(R.id.jobsDisplayView);
        Button postJobButton = findViewById(R.id.postJobButton);
        Button buttonViewJobs = findViewById(R.id.buttonViewJobs);
        Button search = findViewById(R.id.search);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button myApplicationsButton = findViewById(R.id.myApplicationsButton);

        email = getIntent().getStringExtra(USER_EMAIL_KEY);
        if (email == null || email.isEmpty()) {
            welcomeText.setText(getDefaultMessage());
        }

        // Setup RecyclerView
        jobList = new ArrayList<>();
        setupJobClickListener();
        jobsDisplay.setLayoutManager(new LinearLayoutManager(this));
        jobsDisplay.setAdapter(jobAdapter);

        // Setup location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initializeLocationListener(locationText);
        checkLocationPermissionAndStartUpdates();

        // Navigation buttons
        moveToSettingsPage(settingsButton);
        buttonViewJobs.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EmployerJobsActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });

        // Role-specific dashboard setup
        fetchUserDetailsAndSetupUI(locationText, postJobButton, buttonViewJobs, search, myApplicationsButton);
    }

    /** Initializes listener that runs when location changes */
    public void initializeLocationListener(TextView locationText) {
        locationListener = location -> getLocation(location, locationText);
    }

    /** Builds welcome message from name and role */
    public static String getMessage(String firstName, String role) {
        if (firstName == null || role == null) return "Welcome to QuickCash :)";
        return "Welcome, " + firstName + "! Your role is " + role + "!";
    }

    /** Default welcome message if no user info found */
    public static String getDefaultMessage() {
        return "Welcome to QuickCash :)";
    }

    /** Goes to settings screen */
    public void moveToSettingsPage(Button settingsPageButton) {
        settingsPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    /** Goes to job posting page (for employers) */
    public void moveToPostJobPage(Button postJobButton) {
        postJobButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, JobPostActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    /** Goes to job search page (for employees) */
    public void moveToSearchPage(Button search) {
        search.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    /** Goes to my Applications page (for employees) */
    public void moveToMyApplications(Button myApplicationButton) {
        myApplicationButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MyApplicationsActivity.class);
            intent.putExtra(USER_EMAIL_KEY, email);
            startActivity(intent);
        });
    }

    /** Checks location permission and starts updates if allowed */
    private void checkLocationPermissionAndStartUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            startLocationUpdates();
        }
    }

    /** Starts listening for location updates */
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    /** Gets user's city from GPS location and updates nearby jobs */
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

    /** Handles location permission result */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    /** Stops location updates when activity stops */
    @Override
    protected void onStop() {
        super.onStop();
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    /** Loads jobs that match user's city from Firebase */
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

    private void setupDashboardUI(String role, TextView locationText, Button postJobButton, Button buttonViewJobs, Button search, Button myApplicationsButton) {
        if ("Employee".equals(role)) {
            locationText.setVisibility(View.VISIBLE);
            nearbyJobsText.setVisibility(View.VISIBLE);
            jobsDisplay.setVisibility(View.VISIBLE);
            postJobButton.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            buttonViewJobs.setVisibility(View.GONE);
            moveToSearchPage(search);
            moveToMyApplications(myApplicationsButton);

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

    private void fetchUserDetailsAndSetupUI(TextView locationText, Button postJobButton, Button buttonViewJobs, Button search, Button myApplicationsButton) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User details");
        Query userQuery = dbRef.orderByChild("email").equalTo(email);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        welcomeText.setText(getMessage(firstName, role));
                        setupDashboardUI(role, locationText, postJobButton, buttonViewJobs, search, myApplicationsButton);
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

    private void showJobOnMap(Job job) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(job.getLocation() + " Canada", 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                Intent intent = new Intent(this, GoogleMapActivity.class);
                intent.putExtra("latitude", address.getLatitude());
                intent.putExtra("longitude", address.getLongitude());
                intent.putExtra("jobTitle", job.getTitle());
                intent.putExtra("location", job.getLocation());
                intent.putExtra(USER_EMAIL_KEY, email);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Could not find location: " + job.getLocation(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupJobClickListener() {
        jobAdapter = new JobAdapter(jobList, job -> {
            if (job.getLocation() != null && !job.getLocation().isEmpty()) {
                showJobOnMap(job);
            } else {
                Toast.makeText(this, "Location not available for this job", Toast.LENGTH_SHORT).show();
            }
        }, email);
    }
}