package com.example.group14_projectiteration1_quickcash.model;

/***
 * This is User business model class which deals with data related to user such as user name, email etc.
 */

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String confirmPassword;
    public String role;

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
}