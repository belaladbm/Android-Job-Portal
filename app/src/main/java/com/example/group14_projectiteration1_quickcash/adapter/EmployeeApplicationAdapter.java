package com.example.group14_projectiteration1_quickcash.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

/**
 * Adapter for displaying a list of job applications submitted by the employee.
 * Used in MyApplicationsActivity to show job details and application status.
 */
public class EmployeeApplicationAdapter extends RecyclerView.Adapter<EmployeeApplicationAdapter.EmployeeApplicationViewHolder> {

    /**
     * Interface for handling application item clicks.
     */
    public interface OnApplicationClickListener {
        void onApplicationClick(Application application);
    }

    private List<Application> applicationList;
    private Map<String, Job> jobMap;
    private OnApplicationClickListener listener;

    /**
     * Constructor to initialize application list, job map and click listener.
     */
    public EmployeeApplicationAdapter(List<Application> applicationList, Map<String, Job> jobMap, OnApplicationClickListener listener) {
        this.applicationList = applicationList;
        this.jobMap = jobMap;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_application, parent, false);
        return new EmployeeApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeApplicationViewHolder holder, int position) {
        Application application = applicationList.get(position);
        Job job = jobMap != null ? jobMap.get(application.getJobId()) : null;

        // Set job details
        if (job != null) {
            holder.jobTitle.setText(job.getTitle() != null ? job.getTitle() : "No Title");
            holder.jobLocation.setText(job.getLocation() != null ? "Location: " + job.getLocation() : "");
            holder.jobCategory.setText(job.getCategory() != null ? "Category: " + job.getCategory() : "");
            holder.employerEmail.setText(job.getEmployerEmail() != null ? "Employer: " + job.getEmployerEmail() : "");
            holder.jobPay.setText((String.format("Pay: $%.2f", job.getJobPay())));
        } else {
            holder.jobTitle.setText("Job details not available");
            holder.jobLocation.setText("");
            holder.jobCategory.setText("");
            holder.jobPay.setText("$");
            holder.employerEmail.setText("");
        }

        // Set application details
        holder.applicationStatus.setText(application.getStatus() != null ? "Status: " + application.getStatus() : "");
        holder.coverLetter.setText(application.getCoverLetter() != null ? "Cover Letter: " + application.getCoverLetter() : "");

        if ("accepted".equalsIgnoreCase(application.getStatus())) {
            holder.markCompletedButton.setVisibility(View.VISIBLE);
            holder.markCompletedButton.setOnClickListener(v -> {
                // Update status in Firebase
                FirebaseDatabase.getInstance()
                        .getReference("Applications:")
                        .child(application.getId())
                        .child("status")
                        .setValue("Completed")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(holder.itemView.getContext(), "Marked as Completed", Toast.LENGTH_SHORT).show();
                        });
            });
        } else {
            holder.markCompletedButton.setVisibility(View.GONE);
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApplicationClick(application);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicationList != null ? applicationList.size() : 0;
    }

    /**
     * Updates the list of applications and job map, then refreshes the RecyclerView.
     */
    public void updateData(List<Application> newApplications, Map<String, Job> newJobMap) {
        this.applicationList = newApplications;
        this.jobMap = newJobMap;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for application items.
     */
    static class EmployeeApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle;
        TextView jobLocation;
        TextView jobPay;
        TextView jobCategory;
        TextView employerEmail;
        TextView applicationStatus;
        TextView coverLetter;
        Button markCompletedButton;

        public EmployeeApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.applicationJobTitle);
            jobLocation = itemView.findViewById(R.id.applicationJobLocation);
            jobPay = itemView.findViewById(R.id.applicationJobPay);
            jobCategory = itemView.findViewById(R.id.applicationJobCategory);
            employerEmail = itemView.findViewById(R.id.applicationEmployerEmail);
            applicationStatus = itemView.findViewById(R.id.applicationStatus);
            coverLetter = itemView.findViewById(R.id.applicationCoverLetter);
            markCompletedButton = itemView.findViewById(R.id.markCompletedButton);
        }
    }
}