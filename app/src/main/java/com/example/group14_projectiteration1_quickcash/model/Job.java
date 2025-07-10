package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Job implements Serializable {

    private String jobId;
    private String title;
    private String description;
    private String category;
    private String location;
    private String employerEmail;
    private HashMap<String, Application> applications;

    // Required empty constructor for Firebase
    public Job() {
    }

    public Job(String title, String description, String category,
               String location, String employerEmail) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.employerEmail = employerEmail;
        this.jobId = UUID.randomUUID().toString();
        this.applications = new HashMap<>();
    }

    public void generateId() {
        this.jobId = UUID.randomUUID().toString();
    }

    // Getters
    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public HashMap<String, Application> getApplications() {
        return applications;
    }

    // Setters
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    public void setApplications(HashMap<String, Application> applications) {
        this.applications = applications;
    }
}