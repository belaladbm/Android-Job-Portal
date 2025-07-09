package com.example.group14_projectiteration1_quickcash.view;

import android.app.AlertDialog;
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

public class JobDetailsActivity extends AppCompatActivity {

    private Job selectedJob;
    private String employeeEmail;

    private firebaseHelper firebase_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView titleText = findViewById(R.id.detailTitleTextView);
        TextView descriptionText = findViewById(R.id.detailDescriptionTextView);
        TextView categoryText = findViewById(R.id.detailCategoryTextView);
        TextView locationText = findViewById(R.id.detailLocationTextView);
        Button applyNowButton = findViewById(R.id.applyNowButton);

        selectedJob = (Job) getIntent().getSerializableExtra("job");
        employeeEmail = getIntent().getStringExtra("userEmail");
        firebase_helper = new firebaseHelper();

        if (selectedJob != null) {
            if (selectedJob.jobId == null || selectedJob.jobId.isEmpty()) {
                selectedJob.generateId();
            }
            titleText.setText(selectedJob.title);
            descriptionText.setText(selectedJob.description);
            categoryText.setText("Category: " + selectedJob.category);
            locationText.setText("Location: " + selectedJob.location);
        }
        if (employeeEmail == null || employeeEmail.isEmpty()) {
            Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
            return;
        }
        applyNowButton.setOnClickListener(v -> {
            showApplicationDialog(employeeEmail);
        });
    }

    private void showApplicationDialog(String employeeEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_job_application, null);
        builder.setView(dialogView);

        EditText coverLetterInput = dialogView.findViewById(R.id.coverLetterInput);
        Button submitButton = dialogView.findViewById(R.id.submitApplicationButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelApplicationButton);

        AlertDialog dialog = builder.create();

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

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void submitApplication(String employeeEmail, String coverLetter) {
        if (selectedJob == null || selectedJob.jobId == null) {
            Toast.makeText(this, "Application failed. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedJob.applications == null) {
            selectedJob.applications = new HashMap<>();
        }

        if (selectedJob.applications.containsKey(employeeEmail)) {
            Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
            return;
        }

        Application application = new Application(selectedJob.jobId, employeeEmail, coverLetter);
        selectedJob.applications.put(employeeEmail, application);

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
