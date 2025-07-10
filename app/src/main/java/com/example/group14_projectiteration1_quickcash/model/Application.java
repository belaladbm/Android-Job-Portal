package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;

public class Application implements Serializable {
    private String id;
    private String jobId;
    private String employeeEmail;
    private String coverLetter;
    private String status;

    // Empty constructor for Firebase
    public Application() {}

    public Application(String jobId, String employeeEmail, String coverLetter) {
        this.jobId = jobId;
        this.employeeEmail = employeeEmail;
        this.coverLetter = coverLetter;
        this.status = "pending";
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getJobId() {
        return jobId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}