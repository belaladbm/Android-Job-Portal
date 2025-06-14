package com.example.group14_projectiteration1_quickcash.util;



/**
 * Checks if the role is not empty and on the default "Select your role" option.
 */
public class RoleValidator {

    /**
     * Checks is role is valid, either Employee or Employer
     * @param role selected role
     * @return true if valid otherwise false
     * */
    public static boolean isValidRole(String role) {
        return role != null && (role.equals("Employer") || role.equals("Employee"));
    }

}
