package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.adapter.ApplicationAdapter;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays all applications for a specific job posting.
 * Allows employers to view, accept, or reject applications.
 */
public class JobApplicationsActivity extends AppCompatActivity implements ApplicationAdapter.OnApplicationClickListener {

    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private List<Application> applicationsList;
    private firebaseHelper fbHelper;
    private String jobId;
    private String jobTitle;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applications);

        jobId = getIntent().getStringExtra("jobId");
        jobTitle = getIntent().getStringExtra("jobTitle");

        recyclerView = findViewById(R.id.recyclerViewApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicationsList = new ArrayList<>();
        adapter = new ApplicationAdapter(applicationsList, this);
        recyclerView.setAdapter(adapter);

        fbHelper = new firebaseHelper();
        fbHelper.connectFirebase();

        if (jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "Error: missing job ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadApplications();
    }

    /**
     * Loads all applications for the current job from Firebase database.
     */
    private void loadApplications() {
        // Query applications where jobId matches the current job
        fbHelper.applydbref.orderByChild("jobId").equalTo(jobId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        applicationsList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Application application = snapshot.getValue(Application.class);
                                if (application != null) {
                                    application.setId(snapshot.getKey());
                                    applicationsList.add(application);
                                }
                            }
                            adapter.updateApplications(applicationsList);
                        } else {
                            Toast.makeText(JobApplicationsActivity.this,
                                    "No one applied for this job yet",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                        Toast.makeText(JobApplicationsActivity.this,
                                "Error. Cannot load the applications: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Handles click events on application items in the RecyclerView.
     * @param application The application that was clicked
     */
    @Override
    public void onApplicationClick(Application application) {
        // Start ApplicationDetailsActivity for the selected application
        Intent intent = new Intent(this, ApplicationDetailsActivity.class);
        intent.putExtra("jobId", jobId);
        intent.putExtra("applicationId", application.getId());
        startActivity(intent);
    }

    /**
     * Handles accept action for an application.
     * @param application The application to accept
     */
    @Override
    public void onAccept(Application application) {
        updateApplicationStatus(application, "accepted");
    }

    /**
     * Handles reject action for an application.
     * @param application The application to reject
     */
    @Override
    public void onDecline(Application application) {
        updateApplicationStatus(application, "rejected");
    }

    /**
     * Updates the status of an application in Firebase database.
     * @param application The application to update
     * @param status The new status
     */
    private void updateApplicationStatus(Application application, String status) {
        fbHelper.applydbref.child(application.getId())
                .child("status")
                .setValue(status)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String message = status.equals("accepted") ?
                                "Application accepted" : "Application rejected";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        loadApplications();
                    } else {
                        Toast.makeText(this,
                                "Error. Please try again later",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}