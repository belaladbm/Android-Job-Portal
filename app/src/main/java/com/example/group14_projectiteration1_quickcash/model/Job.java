package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Job model class representing a job posting created by an employer.
 * Contains job details and a map of applications submitted by employees.
 */
public class Job implements Serializable {

    private String jobId;
    private String title;
    private String description;
    private String category;
    private String location;
    private String employerEmail;
    private HashMap<String, Application> applications;

    /**
     * Empty constructor required for Firebase deserialization.
     */
    public Job() {
    }

    /**
     * Constructor to create a Job object with all necessary details.
     *
     * @param title         Job title
     * @param description   Job description
     * @param category      Job category (e.g., IT, Construction)
     * @param location      Job location (city or region)
     * @param employerEmail Email of the employer who posted the job
     */
    public Job(String title, String description, String category,
               String location, String employerEmail) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.employerEmail = employerEmail;
        this.jobId = UUID.randomUUID().toString(); // Generates a unique ID
        this.applications = new HashMap<>();
    }

    /**
     * Generates and sets a new unique job ID.
     */
    public void generateId() {
        this.jobId = UUID.randomUUID().toString();
    }

    //Getters

    /**
     * Gets the job ID.
     * @return Job ID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Gets the job title.
     * @return Job title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the job description.
     * @return Job description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the job category.
     * @return Job category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the job location.
     * @return Job location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the employer's email who posted the job.
     * @return Employer's email
     */
    public String getEmployerEmail() {
        return employerEmail;
    }

    /**
     * Gets the map of applications submitted to this job.
     * @return Map of applications (email → Application)
     */
    public HashMap<String, Application> getApplications() {
        return applications;
    }

    //Setters

    /**
     * Sets the job ID.
     * @param jobId Job ID
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Sets the job title.
     * @param title Job title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the job description.
     * @param description Job description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the job category.
     * @param category Job category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the job location.
     * @param location Job location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the employer's email.
     * @param employerEmail Email of the employer
     */
    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    /**
     * Sets the map of applications submitted to this job.
     * @param applications Map of applications (email → Application)
     */
    public void setApplications(HashMap<String, Application> applications) {
        this.applications = applications;
    }
}