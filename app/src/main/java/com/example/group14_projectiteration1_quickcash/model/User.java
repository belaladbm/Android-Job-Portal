package com.example.group14_projectiteration1_quickcash.model;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String confirmPassword;
    public String role;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }
}