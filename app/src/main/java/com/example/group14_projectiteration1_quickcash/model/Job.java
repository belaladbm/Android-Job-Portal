package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;

public class Job implements Serializable {

    public String title;
    public String description;
    public String category;
    public String location;
    public String employerEmail;
    public String latitude;
    public String longitude;

    // empty constructor
    public Job() {
    }

    public Job(String title, String description, String category,
               String location, String employerEmail) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.employerEmail = employerEmail;
    }

    public Job(String title, String description, String category,
               String location, String employerEmail, String latitude, String longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.employerEmail = employerEmail;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
