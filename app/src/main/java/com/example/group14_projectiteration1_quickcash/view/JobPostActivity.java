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
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

public class JobPostActivity extends AppCompatActivity {

    private EditText jobNameEditText;

    private EditText jobDescriptionEditText;

    private EditText locationEditText;
    private Spinner categorySpinner;
    private Button submitJobButton;

    private firebaseHelper firebase_helper;
    private DatabaseReference jobsdbRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        jobNameEditText = findViewById(R.id.jobNameEditText);
        jobDescriptionEditText = findViewById(R.id.jobDescriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        submitJobButton = findViewById(R.id.submitJobButton);

        firebase_helper = new firebaseHelper();

        categorySpinner();

        submitJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_helper.connectFirebase();
                jobsdbRef = firebase_helper.dbref.child("Jobs:");
                submitJob();
            }
        });



    }

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

        //Creating a job object with the input data
        Job job = new Job(
                jobNameEditText.getText().toString(),
                jobDescriptionEditText.getText().toString(),
                categorySpinner.getSelectedItem().toString(),
                locationEditText.getText().toString(),
                getIntent().getStringExtra("userEmail")
        );

        // Saving the job to database.
        jobsdbRef.push().setValue(job).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "Job Posted Successfully!", Toast.LENGTH_SHORT).show();
                redirectingToDashboard();
            }else {
                Toast.makeText(this, "Job Posting Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Populates the category spinner with categories.
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

    private void redirectingToDashboard() {
        Intent intent = new Intent(JobPostActivity.this, DashboardActivity.class);
        String email = getIntent().getStringExtra("userEmail");
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }
}
