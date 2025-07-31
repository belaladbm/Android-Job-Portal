package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.adapter.JobAdapter;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements JobAdapter.OnJobClickListener {

    private Spinner spinnerCategory;
    private EditText editLocation, editKeyword;
    private JobAdapter jobAdapter;
    private List<Job> jobList = new ArrayList<>();
    private String email;

    DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs:");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        editLocation = findViewById(R.id.editLocation);
        editKeyword = findViewById(R.id.editKeyword);
        Button btnSearch = findViewById(R.id.btnSearch);
        RecyclerView recyclerViewJobs = findViewById(R.id.recyclerViewJobs);
        Button backToDashboard = findViewById(R.id.backButton);
        email = getIntent().getStringExtra("userEmail");
        backToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, DashboardActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });

        setupCategorySpinner();
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(this));
        jobAdapter = new JobAdapter(this, jobList, this, email);
        recyclerViewJobs.setAdapter(jobAdapter);

        btnSearch.setOnClickListener(v -> searchJobs());
    }

    private void setupCategorySpinner() {
        List<String> roles = Arrays.asList(
                "Select job category",
                "Construction & Labor",
                "Cleaning & Housekeeping",
                "Delivery & Driving",
                "Food & Hospitality",
                "Retail & Sales",
                "Warehouse & Manufacturing",
                "Childcare & Education",
                "Healthcare & Support",
                "Office & Admin Work",
                "IT & Software",
                "Freelance & Creative",
                "Event Staffing",
                "Moving & Packing",
                "Pet Care",
                "Landscaping & Gardening",
                "Tutoring & Teaching"
        );

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                roles
        );
        spinnerCategory.setAdapter(spinnerAdapter);
    }

    private void searchJobs() {
        String category = spinnerCategory.getSelectedItem().toString().trim();
        String location = editLocation.getText().toString().trim();
        String keyword = editKeyword.getText().toString().trim();
        // Handle "Select job category" option
        boolean ignoreCategory = category.equals("Select job category");

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    Job job = jobSnap.getValue(Job.class);
                    if (job == null) continue;

                    // Case-insensitive matching
                    boolean categoryMatch = ignoreCategory ||
                            (job.getCategory() != null && job.getCategory().equalsIgnoreCase(category));
                    boolean locationMatch = location.isEmpty() ||
                            (job.getLocation() != null && job.getLocation().toLowerCase().contains(location.toLowerCase()));
                    boolean keywordMatch = keyword.isEmpty() ||
                            (job.getTitle() != null && job.getTitle().toLowerCase().contains(keyword.toLowerCase())) ||
                            (job.getDescription() != null && job.getDescription().toLowerCase().contains(keyword.toLowerCase()));

                    if (categoryMatch && locationMatch && keywordMatch) {
                        jobList.add(job);
                    }
                }

                if (jobList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No jobs found.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchActivity.this,
                            "Found " + jobList.size() + " jobs",
                            Toast.LENGTH_SHORT).show();
                }

                jobAdapter.updateJobs(jobList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this,
                        "Something went wrong. Please try again later.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onJobClick(Job job) {
        if (job.getLocation() != null && !job.getLocation().isEmpty()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(job.getLocation() + " Canada", 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    Intent intent = new Intent(SearchActivity.this, GoogleMapActivity.class);
                    intent.putExtra("latitude", address.getLatitude());
                    intent.putExtra("longitude", address.getLongitude());
                    intent.putExtra("jobTitle", job.getTitle());
                    intent.putExtra("location", job.getLocation());
                    intent.putExtra("userEmail", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Could not find location: " + job.getLocation(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location not available for this job", Toast.LENGTH_SHORT).show();
        }
    }
}