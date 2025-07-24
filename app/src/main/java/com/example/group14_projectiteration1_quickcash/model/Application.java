package com.example.group14_projectiteration1_quickcash.model;

import java.io.Serializable;

/**
 * Represents a job application submitted by an employee.
 * Stores job ID, employee email, cover letter, and application status.
 */
public class Application implements Serializable {


    private String id;
    private String jobId;
    private String employeeEmail;
    private double jobPay;
    private String coverLetter;
    private String status;

    /**
     * Empty constructor required by Firebase.
     */
    public Application() {}

    /**
     * Constructor to create a new application.
     * The status is set to "pending" by default.
     *
     * @param jobId         ID of the job being applied to
     * @param employeeEmail Email of the employee applying
     * @param coverLetter   Cover letter/message from the employee
     */
    public Application(String jobId, String employeeEmail, String coverLetter, double jobpay) {
        this.jobId = jobId;
        this.employeeEmail = employeeEmail;
        this.coverLetter = coverLetter;
        this.jobPay = jobpay;
        this.status = "pending";
    }

    // --- Getters ---

    /**
     * Gets the application ID.
     * @return Application ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the job ID related to this application.
     * @return Job ID
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Gets the employee's email.
     * @return Employee's email
     */
    public String getEmployeeEmail() {
        return employeeEmail;
    }

    /**
     * Gets the cover letter or message.
     * @return Cover letter
     */
    public String getCoverLetter() {
        return coverLetter;
    }

    /**
     * Gets the current status of the application.
     * @return Status (e.g. "pending", "accepted", "declined")
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the current job's pay
     * @return job pay
     */
    public double getJobPay() {
        return jobPay;
    }


    // --- Setters ---

    /**
     * Sets the job pay.
     * @param jobPay job pay
     */
    public void setJobPay(double jobPay) {
        this.jobPay = jobPay;
    }

    /**
     * Sets the application ID.
     * @param id Application ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the job ID for this application.
     * @param jobId Job ID
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Sets the employee's email.
     * @param employeeEmail Email of the employee
     */
    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    /**
     * Sets the cover letter or message.
     * @param coverLetter Cover letter
     */
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    /**
     * Sets the application status.
     * @param status New status (e.g. "accepted", "declined")
     */
    public void setStatus(String status) {
        this.status = status;
    }
}