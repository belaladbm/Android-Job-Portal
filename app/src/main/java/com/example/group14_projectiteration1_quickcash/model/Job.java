package com.example.group14_projectiteration1_quickcash.model;

public class Job {

    public String title;
    public String description;
    public String category;
    public String location;
    public String employerEmail;

    // empty constructor
    public Job() {}

    public Job(String title, String description, String category,
               String location, String employerEmail) {
        this.title        = title;
        this.description  = description;
        this.category     = category;
        this.location     = location;
        this.employerEmail= employerEmail;
    }
}
