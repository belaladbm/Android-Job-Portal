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
import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;
import com.example.group14_projectiteration1_quickcash.view.JobDetailsActivity;

import java.util.List;

/**
 * JobAdapter class for displaying jobs in RecyclerView
 * Referred from Tutorial 4 - Firebase CRUD
 */
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {



    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    private List<Job> jobList;
    private OnJobClickListener listener;


    // Updated constructor
    public JobAdapter(List<Job> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
    }

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
        this.listener = null;
    }
    // Constructor


    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_view, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.jobTitle.setText(job.title);
        holder.jobLocation.setText("Location: " + job.location);
        holder.jobCategory.setText("Category: " + job.category);

        holder.btnViewOnMap.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobClick(job);
            }
        });

        // Apply Now button
        holder.applyNowButton.setOnClickListener(v -> {
            // Open JobDetailsActivity
            Intent intent = new Intent(holder.itemView.getContext(), JobDetailsActivity.class);
            intent.putExtra("job", job);

            // Assuming the email is passed to the adapter or activity (you'll need to handle this properly)
            if (holder.itemView.getContext() instanceof DashboardActivity) {
                DashboardActivity activity = (DashboardActivity) holder.itemView.getContext();
                String userEmail = activity.getIntent().getStringExtra("userEmail");
                intent.putExtra("userEmail", userEmail);
            }

            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void updateJobs(List<Job> newJobs) {
        this.jobList = newJobs;

        //notifyDataSetChanged referred from android studio docs
        // URL: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#notifyDataSetChanged()
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        Button btnViewOnMap;
        Button applyNowButton;
        TextView jobTitle;
        TextView jobLocation;
        TextView jobCategory;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewOnMap = itemView.findViewById(R.id.btnViewOnMap);
            jobTitle = itemView.findViewById(R.id.jobTitleTextView);
            applyNowButton = itemView.findViewById(R.id.applyNowButton);
            jobLocation = itemView.findViewById(R.id.jobLocationTextView);
            jobCategory = itemView.findViewById(R.id.jobCategoryTextView);
        }
    }
}