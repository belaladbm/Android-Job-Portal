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
 * JobAdapter class for displaying jobs in RecyclerView
 * Referred from Tutorial 4 - Firebase CRUD
 */
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Job> jobList;

    // Constructor
    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
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

        // Setting the job details using public fields
        holder.jobTitle.setText(job.title);
        holder.jobLocation.setText("Location: " + job.location);
        holder.jobCategory.setText("Category: " + job.category);
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
        TextView jobTitle;
        TextView jobLocation;
        TextView jobCategory;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitleTextView);
            jobLocation = itemView.findViewById(R.id.jobLocationTextView);
            jobCategory = itemView.findViewById(R.id.jobCategoryTextView);
        }
    }
}