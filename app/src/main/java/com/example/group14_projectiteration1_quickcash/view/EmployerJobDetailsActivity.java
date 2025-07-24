package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Job;

/**
 * Shows a job's details
 * Allows the employer to select editing it.
 */
public class EmployerJobDetailsActivity extends AppCompatActivity {

    private Job job;
    private TextView titleText, descriptionText, categoryText, locationText;
    private Button editButton, backButton;

    /**
     * Initializes UI components and displays job data.
     * @param savedInstanceState The saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_job_details);

        job = (Job) getIntent().getSerializableExtra("job");

        titleText = findViewById(R.id.detailTitleTextView);
        descriptionText = findViewById(R.id.detailDescriptionTextView);
        categoryText = findViewById(R.id.detailCategoryTextView);
        locationText = findViewById(R.id.detailLocationTextView);
        editButton = findViewById(R.id.editJobButton);
        backButton = findViewById(R.id.backButton);

        if (job == null) {
            Toast.makeText(this, "Error. Could not find the job", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayJobDetails();

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerJobDetailsActivity.this, EditJobActivity.class);
            intent.putExtra("job", job);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Displays job details.
     */
    private void displayJobDetails() {
        titleText.setText(job.getTitle());
        descriptionText.setText(job.getDescription());
        categoryText.setText("Category: " + job.getCategory());
        locationText.setText("Location: " + job.getLocation());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}