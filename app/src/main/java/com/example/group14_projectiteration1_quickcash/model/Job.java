package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Job implements Serializable {
    public String jobId;
    public String title;
    public String description;
    public String category;
    public String location;
    public String employerEmail;
    public HashMap<String, Application> applications;

    public Job() {
        // Required empty constructor for Firebase
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
}
