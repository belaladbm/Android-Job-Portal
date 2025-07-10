package com.example.group14_projectiteration1_quickcash.model;

/***
 * This is User business model class which deals with data related to user such as user name, email etc.
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;

    //empty constructor
    public User() {
    }

    /***
     * Constructor to set user details like their firstname, last name, email and so on.
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email
     * @param password User's account password
     * @param confirmPassword User's account confirm password
     * @param role User's role
     */
    public User(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setRole(String role) {
        this.role = role;
    }
}