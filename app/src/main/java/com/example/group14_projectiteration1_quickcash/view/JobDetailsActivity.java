package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Job;

public class JobDetailsActivity extends AppCompatActivity {

    private Job selectedJob;
    private String employeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView titleText = findViewById(R.id.detailTitleTextView);
        TextView descriptionText = findViewById(R.id.detailDescriptionTextView);
        TextView categoryText = findViewById(R.id.detailCategoryTextView);
        TextView locationText = findViewById(R.id.detailLocationTextView);
        Button applyNowButton = findViewById(R.id.applyNowButton);
        Button backButton = findViewById(R.id.backButton);

        selectedJob = (Job) getIntent().getSerializableExtra("job");
        employeeEmail = getIntent().getStringExtra("userEmail");

        if (selectedJob != null) {
            titleText.setText(selectedJob.title);
            descriptionText.setText(selectedJob.description);
            categoryText.setText("Category: " + selectedJob.category);
            locationText.setText("Location: " + selectedJob.location);
        }

        applyNowButton.setOnClickListener(v -> {
            if (employeeEmail == null || employeeEmail.isEmpty()) {
                Toast.makeText(this, "Error: Cannot apply without employee email", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_SHORT).show();
        });
        backButton.setOnClickListener(v -> {
        finish();
        });
    }
}
