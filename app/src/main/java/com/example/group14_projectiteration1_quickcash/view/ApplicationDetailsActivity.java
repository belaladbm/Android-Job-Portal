package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ApplicationDetailsActivity extends AppCompatActivity {

    private TextView applicantName;
    private TextView applicantEmail;
    private TextView coverLetter;
    private TextView status;
    private firebaseHelper fbHelper;
    private String applicationId;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);

        // Extract passed intent extras
        String jobId = getIntent().getStringExtra("jobId");
        applicationId = getIntent().getStringExtra("applicationId");

        // Initialize UI components
        applicantName = findViewById(R.id.applicantName);
        applicantEmail = findViewById(R.id.applicantEmail);
        coverLetter = findViewById(R.id.coverLetter);
        status = findViewById(R.id.status);
        Button acceptBTN = findViewById(R.id.acceptBTN);
        Button declineBTN = findViewById(R.id.declineBTN);

        fbHelper = new firebaseHelper();
        fbHelper.connectFirebase();

        // Validate that we have both required IDs
        if (applicationId == null || applicationId.isEmpty() || jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "Application or Job not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadApplicationDetails();

        // Set click listeners for Accept and Decline buttons
        acceptBTN.setOnClickListener(v -> updateApplicationStatus("Accepted"));
        declineBTN.setOnClickListener(v -> updateApplicationStatus("Rejected"));
    }

    /**
     * Loads the application details from Firebase database
     * Once loaded it also triggers loading of the applicant's profile
     */
    private void loadApplicationDetails() {
        fbHelper.applydbref.child(applicationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Application application = snapshot.getValue(Application.class);
                        if (application != null) {
                            email = application.getEmployeeEmail();
                            coverLetter.setText(application.getCoverLetter() != null ? application.getCoverLetter() : "N/A");
                            status.setText(application.getStatus() != null ? application.getStatus() : "Pending");
                            loadApplicantProfile(email);
                        } else {
                            // Application not found in database
                            Toast.makeText(ApplicationDetailsActivity.this, "Application not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(ApplicationDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Loads the applicant's profile information from firebase
     */
    private void loadApplicantProfile(String email) {
        // Query users by email to find the applicant's profile
        fbHelper.dbref.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            User user = userSnap.getValue(User.class);
                            if (user != null) {
                                String fullName = user.firstName + " " + user.lastName;
                                applicantName.setText(fullName);
                                applicantEmail.setText(user.email);
                                return;
                            }
                        }
                        Toast.makeText(ApplicationDetailsActivity.this, "Couldn't find the applicant", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(ApplicationDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Updates the status of the application in firebase
     */
    private void updateApplicationStatus(String newStatus) {
        fbHelper.applydbref.child(applicationId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    status.setText(newStatus);
                    String message = newStatus.equals("Accepted") ?
                            "You have accepted this application" :
                            "You have rejected this application";
                    Toast.makeText(ApplicationDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ApplicationDetailsActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                });
    }
}