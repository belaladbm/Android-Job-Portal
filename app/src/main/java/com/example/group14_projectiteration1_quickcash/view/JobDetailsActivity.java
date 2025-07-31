package com.example.group14_projectiteration1_quickcash.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Displays job details and allows an employee to apply to the job.
 */
public class JobDetailsActivity extends AppCompatActivity {

    private Job selectedJob;
    private firebaseHelper firebaseHelper;

    /**
     * Called when the activity is first created.
     * Sets up job details UI and application button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        // UI references
        TextView titleText = findViewById(R.id.detailTitleTextView);
        TextView descriptionText = findViewById(R.id.detailDescriptionTextView);
        TextView categoryText = findViewById(R.id.detailCategoryTextView);
        TextView locationText = findViewById(R.id.detailLocationTextView);
        Button applyNowButton = findViewById(R.id.applyNowButton);
        Button backButton = findViewById(R.id.backButton);

        // Get job from intent
        selectedJob = (Job) getIntent().getSerializableExtra("job");

        String emailFromIntent = getIntent().getStringExtra("userEmail");
        if (emailFromIntent == null || emailFromIntent.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPreferences("Employee_Email", Context.MODE_PRIVATE);
            emailFromIntent = sharedPreferences.getString("userEmail", null);
        }
        final String employeeEmail = emailFromIntent;

        firebaseHelper = new firebaseHelper();

        // Back to dashboard button
        moveToDashboard(backButton, employeeEmail);

        // Display job details
        if (selectedJob != null) {
            if (selectedJob.getJobId() == null || selectedJob.getJobId().isEmpty())
                selectedJob.generateId();

            setUpJobDetailsUI(titleText, descriptionText, categoryText, locationText);
        } else {
            String jobId = getIntent().getStringExtra("job_id");
            if (jobId != null && !jobId.isEmpty()) {
                fetchJobFromFirebase(jobId, titleText, descriptionText, categoryText, locationText, applyNowButton);
            }
        }

        // Check if user is logged in
        if (employeeEmail == null || employeeEmail.isEmpty()) {
            Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
            return;
        }

        // Apply Now button logic
        applyNowButton.setOnClickListener(v -> showApplicationDialog(employeeEmail));
    }

    /**
     * Navigates back to the DashboardActivity.
     *
     * @param backButton The button clicked
     * @param email The user email to pass back
     */
    public void moveToDashboard(Button backButton, String email) {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(JobDetailsActivity.this, DashboardActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
        });
    }

    /**
     * Shows a dialog where user can enter a cover letter and apply.
     *
     * @param employeeEmail The email of the employee applying
     */
    private void showApplicationDialog(String employeeEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_job_application, null);
        builder.setView(dialogView);

        EditText coverLetterInput = dialogView.findViewById(R.id.coverLetterInput);
        Button submitButton = dialogView.findViewById(R.id.submitApplicationButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelApplicationButton);

        AlertDialog dialog = builder.create();

        // Submit button logic
        submitButton.setOnClickListener(v -> {
            String coverLetter = coverLetterInput.getText().toString().trim();
            if (coverLetter.isEmpty()) {
                coverLetterInput.setError("Please enter a message or cover letter");
                return;
            }
            firebaseHelper.connectFirebase();
            submitApplication(employeeEmail, coverLetter);
            dialog.dismiss();
        });

        // Cancel button
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * Submits the application to Firebase if the user hasn’t applied before.
     *
     * @param employeeEmail The email of the employee applying
     * @param coverLetter The cover letter entered by the user
     */
    private void submitApplication(String employeeEmail, String coverLetter) {
        if (selectedJob == null || selectedJob.getJobId() == null) {
            Toast.makeText(this, "Application failed. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if application map exists
        if (selectedJob.getApplications() == null) {
            selectedJob.setApplications(new HashMap<>());
        }

        // Prevent duplicate applications
        if (selectedJob.getApplications().containsKey(employeeEmail)) {
            Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create application and store it
        Application application = new Application(selectedJob.getJobId(), employeeEmail, coverLetter, selectedJob.getJobPay(), selectedJob.getTitle());
        selectedJob.getApplications().put(employeeEmail, application);

        firebaseHelper.applydbref.push().setValue(application)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Application sent successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Application failed. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchJobFromFirebase(String jobId, TextView titleText, TextView descriptionText,
                                      TextView categoryText, TextView locationText, Button applyNowButton) {
        DatabaseReference jobRef = FirebaseDatabase.getInstance().getReference("Jobs:").child(jobId);
        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedJob = snapshot.getValue(Job.class);
                if (selectedJob != null) {
                    if (selectedJob.getJobId() == null || selectedJob.getJobId().isEmpty())
                        selectedJob.setJobId(jobId);

                    setUpJobDetailsUI(titleText, descriptionText, categoryText, locationText);
                } else {
                    Toast.makeText(JobDetailsActivity.this, "Job not found", Toast.LENGTH_SHORT).show();
                    applyNowButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JobDetailsActivity.this, "Error loading job", Toast.LENGTH_SHORT).show();
                applyNowButton.setEnabled(false);
            }
        });
    }

    private void setUpJobDetailsUI(TextView titleText, TextView descriptionText, TextView categoryText, TextView locationText) {
        if (selectedJob != null) {
            titleText.setText(selectedJob.getTitle());
            descriptionText.setText(selectedJob.getDescription());
            categoryText.setText("Category: " + selectedJob.getCategory());
            locationText.setText("Location: " + selectedJob.getLocation());
        }
    }
}