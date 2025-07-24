package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Job;
import java.util.Arrays;
import java.util.List;

/**
 * Form for the employer t modify the information of a job he has posted.
 */
public class EditJobActivity extends AppCompatActivity {

    private EditText titleEdit, descriptionEdit, locationEdit;
    private Spinner categorySpinner;
    private Button saveButton, cancelButton;
    private Job job;
    private firebaseHelper fbHelper;

    /**
     * Initializes UI components and loads job data.
     * @param savedInstanceState The saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        job = (Job) getIntent().getSerializableExtra("job");
        if (job == null) {
            Toast.makeText(this, "Error. Could not find the job.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        titleEdit = findViewById(R.id.editJobTitle);
        descriptionEdit = findViewById(R.id.editJobDescription);
        locationEdit = findViewById(R.id.editJobLocation);
        categorySpinner = findViewById(R.id.editJobCategorySpinner);
        saveButton = findViewById(R.id.saveJobButton);
        cancelButton = findViewById(R.id.cancelEditButton);

        fbHelper = new firebaseHelper();
        fbHelper.connectFirebase();

        populateCategorySpinner();
        fillFieldsWithJobData();

        saveButton.setOnClickListener(v -> saveJobEdits());
        cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * Populates the spinner with a bunch of job categories to select from.
     */
    private void populateCategorySpinner() {
        List<String> categories = Arrays.asList(
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                categories);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set current category
        int index = categories.indexOf(job.getCategory());
        if (index >= 0) {
            categorySpinner.setSelection(index);
        }
    }

    /**
     * Fills the UI fields with the existing job data.
     */
    private void fillFieldsWithJobData() {
        titleEdit.setText(job.getTitle());
        descriptionEdit.setText(job.getDescription());
        locationEdit.setText(job.getLocation());
    }

    /**
     * Saves the new job details into firebase.
     */
    private void saveJobEdits() {
        String newTitle = titleEdit.getText().toString().trim();
        String newDescription = descriptionEdit.getText().toString().trim();
        String newLocation = locationEdit.getText().toString().trim();
        String newCategory = categorySpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(newTitle)) {
            titleEdit.setError("Need a job title");
            return;
        }
        if (TextUtils.isEmpty(newDescription)) {
            descriptionEdit.setError("Need a job description");
            return;
        }
        if (TextUtils.isEmpty(newLocation)) {
            locationEdit.setError("Need a job location");
            return;
        }
        if (newCategory == null || newCategory.isEmpty()) {
            Toast.makeText(this, "Please make sure you select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update job object
        job.setTitle(newTitle);
        job.setDescription(newDescription);
        job.setLocation(newLocation);
        job.setCategory(newCategory);

        // Update Firebase job node
        fbHelper.jobsdbRef.child(job.getJobId())
                .setValue(job)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Job updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update job. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}