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

        // Get user email from intent
        userEmail = getIntent().getStringExtra(USER_EMAIL_KEY);

        // Initialize Firebase references
        applicationsRef = FirebaseDatabase.getInstance().getReference("Applications:");
        jobsRef = FirebaseDatabase.getInstance().getReference("Jobs:");

        // Initialize views
        applicationsRecyclerView = findViewById(R.id.myApplicationsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        // Setup RecyclerView
        applicationList = new ArrayList<>();
        jobMap = new HashMap<>();
        applicationAdapter = new EmployeeApplicationAdapter(applicationList, jobMap, this);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicationsRecyclerView.setAdapter(applicationAdapter);

        // Load applications
        loadMyApplications();
    }

    @Override
    public void onApplicationClick(Application application) {}

    private void loadMyApplications() {
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        applicationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                applicationList.clear();

                // Get all applications for this user
                for (DataSnapshot applicationSnapshot : dataSnapshot.getChildren()) {
                    Application application = applicationSnapshot.getValue(Application.class);

                    if (application != null && userEmail.equals(application.getEmployeeEmail())) {
                        application.setId(applicationSnapshot.getKey());
                        applicationList.add(application);
                    }
                }

                // If no applications found
                if (applicationList.isEmpty()) {
                    applicationsRecyclerView.setVisibility(View.GONE);
                    emptyStateText.setVisibility(View.VISIBLE);
                } else {
                    // Fetch job details for each application
                    fetchJobDetailsForApplications();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyApplicationsActivity.this,
                        "Error loading applications: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchJobDetailsForApplications() {
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobMap.clear();

                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    Job job = jobSnapshot.getValue(Job.class);
                    if (job != null && job.getJobId() != null) {
                        jobMap.put(job.getJobId(), job);
                    }
                }

                // Update adapter with both applications and job details
                applicationsRecyclerView.setVisibility(View.VISIBLE);
                emptyStateText.setVisibility(View.GONE);
                applicationAdapter.updateData(applicationList, jobMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyApplicationsActivity.this,
                        "Error loading job details: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}