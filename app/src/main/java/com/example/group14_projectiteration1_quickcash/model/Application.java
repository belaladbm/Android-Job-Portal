package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;

public class Application implements Serializable {
    public String id;
    public String jobId;
    public String employeeEmail;
    public String coverLetter;
    public String status;

    //Empty constructor
    public Application() {}

    public Application(String jobId, String employeeEmail, String coverLetter) {
        this.jobId = jobId;
        this.employeeEmail = employeeEmail;
        this.coverLetter = coverLetter;
        this.status = "pending";
    }
}