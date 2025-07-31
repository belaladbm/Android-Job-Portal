package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.adapter.EmployeeApplicationAdapter;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplicationsActivity extends AppCompatActivity implements EmployeeApplicationAdapter.OnApplicationClickListener {

    private static final String USER_EMAIL_KEY = "userEmail";

    private RecyclerView applicationsRecyclerView;
    private EmployeeApplicationAdapter applicationAdapter;
    private List<Application> applicationList;
    private Map<String, Job> jobMap;
    private TextView emptyStateText;
    private String userEmail;

    private DatabaseReference applicationsRef;
    private DatabaseReference jobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_applications);

        // Retrieve the logged-in user's email from the intent
        userEmail = getIntent().getStringExtra(USER_EMAIL_KEY);

        // Initialize Firebase database references
        applicationsRef = FirebaseDatabase.getInstance().getReference("Applications:");
        jobsRef = FirebaseDatabase.getInstance().getReference("Jobs:");

        // Initialize views
        applicationsRecyclerView = findViewById(R.id.myApplicationsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        // Set up RecyclerView and adapter
        applicationList = new ArrayList<>();
        jobMap = new HashMap<>();
        applicationAdapter = new EmployeeApplicationAdapter(applicationList, jobMap, this);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicationsRecyclerView.setAdapter(applicationAdapter);

        // Load applications for the current user
        loadMyApplications();
    }

    // Required by the adapter for click events, currently unused
    @Override
    public void onApplicationClick(Application application) {}

    // Load applications submitted by the current user from Firebase
    private void loadMyApplications() {
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if email is not found
            return;
        }

        applicationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                applicationList.clear();

                // Filter applications that belong to the current user
                for (DataSnapshot applicationSnapshot : dataSnapshot.getChildren()) {
                    Application application = applicationSnapshot.getValue(Application.class);

                    if (application != null && userEmail.equals(application.getEmployeeEmail())) {
                        application.setId(applicationSnapshot.getKey()); // Set application ID
                        applicationList.add(application);
                    }
                }

                // If no applications found, show empty state
                if (applicationList.isEmpty()) {
                    applicationsRecyclerView.setVisibility(View.GONE);
                    emptyStateText.setVisibility(View.VISIBLE);
                } else {
                    // If applications found, load corresponding job details
                    fetchJobDetailsForApplications();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error when loading applications
                Toast.makeText(MyApplicationsActivity.this,
                        "Error loading applications: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load job details for each application from Firebase
    private void fetchJobDetailsForApplications() {
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobMap.clear();

                // Store job details using jobId as key
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null && job.getJobId() != null) {
                        jobMap.put(job.getJobId(), job);
                    }
                }

                // Update RecyclerView with applications and job info
                applicationsRecyclerView.setVisibility(View.VISIBLE);
                emptyStateText.setVisibility(View.GONE);
                applicationAdapter.updateData(applicationList, jobMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error when loading jobs
                Toast.makeText(MyApplicationsActivity.this,
                        "Error loading job details: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}