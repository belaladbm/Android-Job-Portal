package com.example.group14_projectiteration1_quickcash.util;



//Checks if the role is not empty and on the default "Select your role" option.
public class RoleValidator {

    public static boolean isValidRole(String role) {
        return role != null && (role.equals("Employer") || role.equals("Employee"));
    }

}
