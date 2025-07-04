package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.adapter.JobAdapter;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements JobAdapter.OnJobClickListener {

    private Spinner spinnerCategory;
    private EditText editLocation, editKeyword;
    private Button btnSearch;
    private RecyclerView recyclerViewJobs;
    private JobAdapter jobAdapter;
    private List<Job> jobList = new ArrayList<>();

    DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        editLocation = findViewById(R.id.editLocation);
        editKeyword = findViewById(R.id.editKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerViewJobs = findViewById(R.id.recyclerViewJobs);

        setupCategorySpinner();
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(this));
        jobAdapter = new JobAdapter(jobList, this);
        recyclerViewJobs.setAdapter(jobAdapter);

        btnSearch.setOnClickListener(v -> searchJobs());
    }

    private void setupCategorySpinner() {
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
        spinnerCategory.setAdapter(spinnerAdapter);
    }

    private void searchJobs() {
        String category = spinnerCategory.getSelectedItem().toString().trim().toLowerCase();
        String location = editLocation.getText().toString().trim().toLowerCase();
        String keyword = editKeyword.getText().toString().trim().toLowerCase();

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    Job job = jobSnap.getValue(Job.class);
                    if (job == null) continue;

                    boolean match = (category.equals("select job category") || job.category.toLowerCase().contains(category)) &&
                            (location.isEmpty() || job.location.toLowerCase().contains(location)) &&
                            (keyword.isEmpty() || job.title.toLowerCase().contains(keyword) || job.description.toLowerCase().contains(keyword));

                    if (match) {
                        jobList.add(job);
                    }
                }

                if (jobList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No jobs found.", Toast.LENGTH_SHORT).show();
                }

                jobAdapter.updateJobs(jobList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onJobClick(Job job) {
        if (job.latitude != null && job.longitude != null) {
            String uri = "http://maps.google.com/maps?q=loc:" + job.latitude + "," + job.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    }
}