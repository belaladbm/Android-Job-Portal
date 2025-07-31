package com.example.group14_projectiteration1_quickcash.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.Application;

import java.util.List;

/**
 * Adapter class for displaying a list of job applications in a RecyclerView.
 * Allows employers to view application status and accept/decline applications.
 */
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    private List<Application> applicationList;
    private OnApplicationClickListener listener;

    /**
     * Interface for handling application click actions.
     */
    public interface OnApplicationClickListener {
        void onApplicationClick(Application application);
        void onAccept(Application application);
        void onDecline(Application application);
        void onApprove(Application application);
    }

    /**
     * Constructor to initialize the application list and listener.
     */
    public ApplicationAdapter(List<Application> applicationList, OnApplicationClickListener listener) {
        this.applicationList = applicationList;
        this.listener = listener;
    }

    /**
     * Updates the application list and refreshes the RecyclerView.
     */
    public void updateApplications(List<Application> newApplications) {
        this.applicationList = newApplications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application application = applicationList.get(position);

        // Set email and status
        holder.applicantEmail.setText(application.getEmployeeEmail());
        holder.status.setText("Status: " + application.getStatus());


        // Handle click actions
        holder.itemView.setOnClickListener(v -> listener.onApplicationClick(application));
        holder.acceptButton.setOnClickListener(v -> listener.onAccept(application));
        holder.declineButton.setOnClickListener(v -> listener.onDecline(application));
        holder.approveButton.setOnClickListener(v -> listener.onApprove(application));

        // Disable buttons if already accepted/declined
        if (application.getStatus().equalsIgnoreCase("completed")) {
            holder.acceptButton.setEnabled(false);
            holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setEnabled(false);
            holder.declineButton.setVisibility(View.GONE);
            holder.approveButton.setEnabled(true);
            holder.approveButton.setVisibility(View.VISIBLE);
        } else if (!application.getStatus().equalsIgnoreCase("pending")) {
            holder.acceptButton.setEnabled(false);
            holder.declineButton.setEnabled(false);
            holder.approveButton.setEnabled(false);
            holder.approveButton.setVisibility(View.GONE);
        } else {
            holder.acceptButton.setEnabled(true);
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.declineButton.setEnabled(true);
            holder.declineButton.setVisibility(View.VISIBLE);
            holder.approveButton.setEnabled(false);
            holder.approveButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    /**
     * ViewHolder for application item views.
     */
    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView applicantEmail;
        TextView status;
        Button acceptButton;
        Button declineButton;
        Button approveButton;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantEmail = itemView.findViewById(R.id.applicantEmail);
            status = itemView.findViewById(R.id.status);
            acceptButton = itemView.findViewById(R.id.acceptBTN);
            declineButton = itemView.findViewById(R.id.declineBTN);
            approveButton = itemView.findViewById(R.id.approveBTN);
        }
    }
}