package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.adapter.EmployerJobAdapter;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays all jobs posted by the current employer
 * and allows them to view applications for each job.
 */

public class EmployerJobsActivity extends AppCompatActivity implements EmployerJobAdapter.OnJobClickListener {

    private static final String TAG = "EmployerJobsActivity";
    private EmployerJobAdapter adapter;
    private List<Job> jobList;
    private firebaseHelper fbHelper;
    private String employerEmail;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_jobs);

        employerEmail = getIntent().getStringExtra("userEmail");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        adapter = new EmployerJobAdapter(jobList, this);
        recyclerView.setAdapter(adapter);

        fbHelper = new firebaseHelper();
        fbHelper.connectFirebase();

        loadEmployerJobs();
        debugApplications();
    }

    /**
     * Loads all jobs posted by the current employer from Firebase database.
     */
    private void loadEmployerJobs() {
        // Query jobs where employerEmail matches the current user
        fbHelper.jobsdbRef.orderByChild("employerEmail").equalTo(employerEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        jobList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Job job = snapshot.getValue(Job.class);
                            if (job != null) {
                                jobList.add(job);
                            }
                        }
                        adapter.updateJobs(jobList);
                        if (jobList.isEmpty()) {
                            Toast.makeText(EmployerJobsActivity.this,
                                    "You haven't posted any jobs yet",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Loaded " + jobList.size() + " jobs");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(EmployerJobsActivity.this,
                                "Error loading jobs: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Handles click events on job items in the RecyclerView.
     * @param job The job that was clicked
     */
    @Override
    public void onJobClick(Job job) {
        if (job.getJobId() == null || job.getJobId().isEmpty()) {
            Toast.makeText(this, "Error: Job ID not found", Toast.LENGTH_SHORT).show();
            return;
        }
        // Open job actions page to let the employer chose what to do
        Intent intent = new Intent(this, JobActionsActivity.class);
        intent.putExtra("job", job);
        intent.putExtra("userEmail", employerEmail);
        startActivity(intent);
    }

    /**
     * Debug method to log information about jobs and applications.
     * Helps verify data structure in Firebase.
     */
    private void debugApplications() {
        // Log all jobs posted by this employer
        fbHelper.jobsdbRef.orderByChild("employerEmail").equalTo(employerEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Your jobs:");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Job job = snapshot.getValue(Job.class);
                            if (job != null) {
                                Log.d(TAG, "Job: " + job.getTitle() + " | jobId: " + job.getJobId());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });
        // Log all applications in the database
        fbHelper.applydbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "\nAll applications:");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String appJobId = snapshot.child("jobId").getValue(String.class);
                    String email = snapshot.child("employeeEmail").getValue(String.class);
                    Log.d(TAG, "Application from: " + email + " | for jobId: " + appJobId);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });
    }
}