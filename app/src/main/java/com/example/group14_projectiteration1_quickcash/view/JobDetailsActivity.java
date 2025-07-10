package com.example.group14_projectiteration1_quickcash.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Displays job details and allows an employee to apply to the job.
 */
public class JobDetailsActivity extends AppCompatActivity {

    private Job selectedJob;
    private firebaseHelper firebase_helper;

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

        // Get job and user email from intent
        selectedJob = (Job) getIntent().getSerializableExtra("job");
        String employeeEmail = getIntent().getStringExtra("userEmail");

        firebase_helper = new firebaseHelper();

        // Back to dashboard button
        moveToDashboard(backButton, employeeEmail);

        // Display job details
        if (selectedJob != null) {
            if (selectedJob.getJobId() == null || selectedJob.getJobId().isEmpty()) {
                selectedJob.generateId();
            }
            titleText.setText(selectedJob.getTitle());
            descriptionText.setText(selectedJob.getDescription());
            categoryText.setText("Category: " + selectedJob.getCategory());
            locationText.setText("Location: " + selectedJob.getLocation());
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
            firebase_helper.connectFirebase();
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
        Application application = new Application(selectedJob.getJobId(), employeeEmail, coverLetter);
        selectedJob.getApplications().put(employeeEmail, application);

        firebase_helper.applydbref.push().setValue(application)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Application sent successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Application failed. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}