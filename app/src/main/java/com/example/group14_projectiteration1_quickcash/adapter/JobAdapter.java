package com.example.group14_projectiteration1_quickcash.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.model.CurrentUser;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.view.JobDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<String> appliedJobIds = new HashSet<>();
    private final String userId = CurrentUser.getInstance().getCurrentUser().getUserId();
    private Set<String> favoriteJobIds = new HashSet<>();
    private final Context context;
    private firebaseHelper firebaseHelper;

    /**
     * Constructor used when map button click needs to be handled.
     */
    public JobAdapter(@NonNull Context ctx, List<Job> jobList, OnJobClickListener listener, String userEmail) {
        this.context = ctx;
        this.jobList = jobList;
        this.listener = listener;
        this.userEmail = userEmail;
        this.firebaseHelper = new firebaseHelper();
        firebaseHelper.connectFirebase();
        checkAppliedJobs();
    }

    /**
     * Basic constructor without listener.
     */
    public JobAdapter(@NonNull Context context, List<Job> jobList, OnJobClickListener listener, String userEmail, String userId, Set<String> favoriteJobIds) {
        this.context = context;
        this.jobList = jobList;
        this.listener = listener;
        this.userEmail = userEmail;
        this.favoriteJobIds = favoriteJobIds;
    }

    public JobAdapter(@NonNull Context ctx, List<Job> jobList) {
        this.context = ctx;
        this.jobList = jobList;
        this.listener = null;
        this.appliedJobIds = new HashSet<>();
    }

    /**
     * Checks which jobs the current user has already applied to
     */
    private void checkAppliedJobs() {
        if (userEmail == null || userEmail.isEmpty()) return;

        firebaseHelper.applydbref
                .orderByChild("employeeEmail")
                .equalTo(userEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        appliedJobIds.clear();
                        for (DataSnapshot applicationSnapshot : snapshot.getChildren()) {
                            Application app = applicationSnapshot.getValue(Application.class);
                            if (app != null && app.getJobId() != null) {
                                appliedJobIds.add(app.getJobId());
                            }
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error silently
                    }
                });
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
        holder.jobPay.setText("Job Pay: $" + job.getJobPay());

        // Handle "View on Map" click
        holder.btnViewOnMap.setOnClickListener(v -> {
            if (listener != null) listener.onJobClick(job);
        });

        // Check if user has already applied
        boolean hasApplied = appliedJobIds.contains(job.getJobId());

        if (hasApplied) {
            holder.applyNowButton.setVisibility(View.GONE);
            holder.appliedTextView.setVisibility(View.VISIBLE);
        } else {
            holder.applyNowButton.setVisibility(View.VISIBLE);
            holder.appliedTextView.setVisibility(View.GONE);

            holder.applyNowButton.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), JobDetailsActivity.class);
                intent.putExtra("job", job);
                intent.putExtra("userEmail", userEmail);
                holder.itemView.getContext().startActivity(intent);
            });
        }

        // Favorite star display
        holder.favoriteButton.setText(favoriteJobIds.contains(job.getJobId()) ? "★" : "☆");

        holder.favoriteButton.setOnClickListener(v -> {
            if (userId == null || userId.isEmpty()) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            if (job == null || job.getJobId() == null) {
                Toast.makeText(context, "Invalid job data", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference favRef = FirebaseDatabase.getInstance()
                    .getReference("Favorites")
                    .child(userId)
                    .child(job.getJobId());

            if (favoriteJobIds.contains(job.getJobId())) {
                favRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteJobIds.remove(job.getJobId());
                        holder.favoriteButton.setText("☆");
                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove favorite", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                favRef.setValue(job).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteJobIds.add(job.getJobId());
                        holder.favoriteButton.setText("★");
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add favorite", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
        Button favoriteButton;
        TextView jobPay;
        TextView appliedTextView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewOnMap = itemView.findViewById(R.id.btnViewOnMap);
            applyNowButton = itemView.findViewById(R.id.applyNowButton);
            jobTitle = itemView.findViewById(R.id.jobTitleTextView);
            jobLocation = itemView.findViewById(R.id.jobLocationTextView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            jobCategory = itemView.findViewById(R.id.jobCategoryTextView);
            jobPay = itemView.findViewById(R.id.jobPayTextView);
            appliedTextView = itemView.findViewById(R.id.appliedTextView);
        }
    }
}