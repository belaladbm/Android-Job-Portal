package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.model.Job;

import java.util.Arrays;
import java.util.List;

/**
 * JobPostActivity allows employers to post a job by filling in the job title,
 * description, category, and location. The job is then stored in Firebase.
 */
public class JobPostActivity extends AppCompatActivity {

    private EditText jobNameEditText;
    private EditText jobDescriptionEditText;
    private EditText locationEditText;
    private Spinner categorySpinner;
    private firebaseHelper firebase_helper;

    /**
     * Called when the activity is first created.
     * Initializes UI elements and sets up event handlers.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_post);

        // Handle layout padding for system bars (status bar, nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize input fields
        jobNameEditText = findViewById(R.id.jobNameEditText);
        jobDescriptionEditText = findViewById(R.id.jobDescriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        Button submitJobButton = findViewById(R.id.submitJobButton);
        Button backToDashboard = findViewById(R.id.backButton);

        String email = getIntent().getStringExtra("userEmail");
        firebase_helper = new firebaseHelper();

        // Populate spinner with job categories
        categorySpinner();

        // Navigate back to dashboard
        backToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(JobPostActivity.this, DashboardActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });

        // Handle submit job button click
        submitJobButton.setOnClickListener(v -> {
            firebase_helper.connectFirebase();
            submitJob(); // Call method to validate and post job
        });
    }

    /**
     * Validates input and creates a new job entry in Firebase.
     */
    private void submitJob(){

        // Validation for job posting fields.
        /* 1. Job title */
        if (TextUtils.isEmpty(jobNameEditText.getText().toString().trim())) {
            jobNameEditText.setError("Job title is required");
            Toast.makeText(this, "Job Posting Failed!, Please enter a job title", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 2. Description */
        if (TextUtils.isEmpty(jobDescriptionEditText.getText().toString().trim())) {
            jobDescriptionEditText.setError("Description is required");
            Toast.makeText(this, "Job Posting Failed!, Please enter a job description", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 3. Category */
        String category = categorySpinner.getSelectedItem().toString();
        if (category.equals("Select job category")) {
            Toast.makeText(this, "Job Posting Failed!, Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 4. Location */
        if (TextUtils.isEmpty(locationEditText.getText().toString().trim())) {
            locationEditText.setError("Location is required");
            Toast.makeText(this, "Job Posting Failed!, Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Job object from input
        Job job = new Job(
                jobNameEditText.getText().toString(),
                jobDescriptionEditText.getText().toString(),
                category,
                locationEditText.getText().toString(),
                getIntent().getStringExtra("userEmail")
        );

        // Save job in Firebase database
        firebase_helper.jobsdbRef.push().setValue(job).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Job Posted Successfully!", Toast.LENGTH_SHORT).show();
                redirectingToDashboard();
            } else {
                Toast.makeText(this, "Job Posting Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fills the job category spinner with predefined values.
     */
    private void categorySpinner() {
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
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
    }

    /**
     * Redirects the user back to the Dashboard after a successful job post.
     */
    private void redirectingToDashboard() {
        Intent intent = new Intent(JobPostActivity.this, DashboardActivity.class);
        String email = getIntent().getStringExtra("userEmail");
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }
}