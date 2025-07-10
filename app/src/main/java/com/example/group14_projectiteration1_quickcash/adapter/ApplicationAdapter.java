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

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    private List<Application> applicationList;
    private OnApplicationClickListener listener;

    public interface OnApplicationClickListener {
        void onApplicationClick(Application application);
        void onAccept(Application application);
        void onDecline(Application application);
    }

    public ApplicationAdapter(List<Application> applicationList, OnApplicationClickListener listener) {
        this.applicationList = applicationList;
        this.listener = listener;
    }

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
        holder.applicantEmail.setText(application.getEmployeeEmail());
        holder.status.setText("Status: " + application.getStatus());

        holder.itemView.setOnClickListener(v -> listener.onApplicationClick(application));
        holder.acceptButton.setOnClickListener(v -> listener.onAccept(application));
        holder.declineButton.setOnClickListener(v -> listener.onDecline(application));


        if (!application.getStatus().equalsIgnoreCase("pending")) {
            holder.acceptButton.setEnabled(false);
            holder.declineButton.setEnabled(false);
        } else {
            holder.acceptButton.setEnabled(true);
            holder.declineButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView applicantEmail;
        TextView status;
        Button acceptButton;
        Button declineButton;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantEmail = itemView.findViewById(R.id.applicantEmail);
            status = itemView.findViewById(R.id.status);
            acceptButton = itemView.findViewById(R.id.acceptBTN);
            declineButton = itemView.findViewById(R.id.declineBTN);
        }
    }
}