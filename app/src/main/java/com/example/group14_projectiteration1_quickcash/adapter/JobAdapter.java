package com.example.group14_projectiteration1_quickcash.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.view.JobDetailsActivity;

import java.util.List;

/**
 * Adapter class for displaying a list of jobs in a RecyclerView.
 */
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    /**
     * Listener interface for handling map button clicks.
     */
    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    private List<Job> jobList;
    private OnJobClickListener listener;
    private String userEmail;

    /**
     * Constructor used when map button click needs to be handled.
     */
    public JobAdapter(List<Job> jobList, OnJobClickListener listener, String userEmail) {
        this.jobList = jobList;
        this.listener = listener;
        this.userEmail = userEmail;
    }

    /**
     * Basic constructor without listener.
     */
    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
        this.listener = null;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_view, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.jobTitle.setText(job.getTitle());
        holder.jobLocation.setText("Location: " + job.getLocation());
        holder.jobCategory.setText("Category: " + job.getCategory());

        // Handle "View on Map" click
        holder.btnViewOnMap.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobClick(job);
            }
        });

        // Handle "Apply Now" click
        holder.applyNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), JobDetailsActivity.class);
            intent.putExtra("job", job);
            intent.putExtra("userEmail", userEmail);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    /**
     * Updates the job list and refreshes the RecyclerView.
     */
    public void updateJobs(List<Job> newJobs) {
        this.jobList = newJobs;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for holding UI elements of a job item.
     */
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        Button btnViewOnMap;
        Button applyNowButton;
        TextView jobTitle;
        TextView jobLocation;
        TextView jobCategory;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewOnMap = itemView.findViewById(R.id.btnViewOnMap);
            applyNowButton = itemView.findViewById(R.id.applyNowButton);
            jobTitle = itemView.findViewById(R.id.jobTitleTextView);
            jobLocation = itemView.findViewById(R.id.jobLocationTextView);
            jobCategory = itemView.findViewById(R.id.jobCategoryTextView);
        }
    }
}