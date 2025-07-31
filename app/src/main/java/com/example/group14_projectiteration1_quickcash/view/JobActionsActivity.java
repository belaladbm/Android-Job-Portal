package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Job;

/**
 * Activity that allows employers to choose actions for a selected job they've posted:
 * - View applications
 * - Edit job details
 * - Go back
 */
public class JobActionsActivity extends AppCompatActivity {

    private Job job;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_actions);

        job = (Job) getIntent().getSerializableExtra("job");
        userEmail = getIntent().getStringExtra("userEmail");

        if (job == null || userEmail == null)
        {
            Toast.makeText(this, "Missing job or user data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Button viewApplicationsBtn = findViewById(R.id.viewApplicationsButton);
        Button editJobBtn = findViewById(R.id.editJobButton);
        Button backBtn = findViewById(R.id.backButton);

        viewApplicationsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, JobApplicationsActivity.class);
            intent.putExtra("jobId", job.getJobId());
            intent.putExtra("jobTitle", job.getTitle());
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        });

        editJobBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditJobActivity.class);
            intent.putExtra("job", job);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> finish());
    }
}