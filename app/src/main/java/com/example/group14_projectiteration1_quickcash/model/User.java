package com.example.group14_projectiteration1_quickcash.model;

/**
 * Represents a user in the system.
 * Stores basic user information like name, email, password, and role.
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;

    /**
     * Empty constructor required for Firebase.
     */
    public User() {
    }

    /**
     * Constructor to initialize all fields of a User.
     *
     * @param firstName       User's first name
     * @param lastName        User's last name
     * @param email           User's email address
     * @param password        User's password
     * @param confirmPassword Password confirmation
     * @param role            Role of the user (e.g., "Employer", "Employee")
     */
    public User(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    //Getters

    /**
     * Gets the user's first name.
     * @return First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the user's last name.
     * @return Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the user's email.
     * @return Email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's password.
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's confirm password.
     * @return Confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Gets the user's role.
     * @return Role
     */
    public String getRole() {
        return role;
    }

    // Setters

    /**
     * Sets the user's first name.
     * @param firstName First name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the user's last name.
     * @param lastName Last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the user's email.
     * @param email Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's password.
     * @param password Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user's confirm password.
     * @param confirmPassword Confirm password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Sets the user's role.
     * @param role Role (e.g., "Employer", "Employee")
     */
    public void setRole(String role) {
        this.role = role;
    }
}