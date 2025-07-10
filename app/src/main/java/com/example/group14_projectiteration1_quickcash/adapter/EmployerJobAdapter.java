package com.example.group14_projectiteration1_quickcash.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Job;

import java.util.List;

/**
 * Adapter for displaying a list of jobs posted by the employer.
 * Used in EmployerJobsActivity to show job title, location, and category.
 */
public class EmployerJobAdapter extends RecyclerView.Adapter<EmployerJobAdapter.EmployerJobViewHolder> {

    /**
     * Interface for handling job item clicks.
     */
    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    private List<Job> jobList;
    private OnJobClickListener listener;

    /**
     * Constructor to initialize job list and click listener.
     */
    public EmployerJobAdapter(List<Job> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployerJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employer_job, parent, false);
        return new EmployerJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerJobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.jobTitle.setText(job.getTitle() != null ? job.getTitle() : "No Title");
        holder.jobLocation.setText(job.getLocation() != null ? "Location: " + job.getLocation() : "");
        holder.jobCategory.setText(job.getCategory() != null ? "Category: " + job.getCategory() : "");

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobClick(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    /**
     * Updates the list of jobs and refreshes the RecyclerView.
     */
    public void updateJobs(List<Job> newJobs) {
        this.jobList = newJobs;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for job items.
     */
    static class EmployerJobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle;
        TextView jobLocation;
        TextView jobCategory;

        public EmployerJobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobLocation = itemView.findViewById(R.id.jobLocation);
            jobCategory = itemView.findViewById(R.id.jobCategory);
        }
    }
}