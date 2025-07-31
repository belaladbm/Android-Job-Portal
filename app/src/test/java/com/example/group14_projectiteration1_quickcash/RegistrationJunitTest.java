package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.group14_projectiteration1_quickcash.model.User;

public class RegistrationJunitTest {

    /**
     * Checks if valid user creation is passing validation.
     * */
    @Test
    public void testValidUserCreation() {
        User user = new User(
                "22",
                "Abeed",
                "Mulla",
                "abeed.mulla@dal.ca",
                "password123",
                "password123",
                "Employee"
        );

        assertEquals("Abeed", user.getFirstName());
        assertEquals("Mulla", user.getLastName());
        assertEquals("abeed.mulla@dal.ca", user.getEmail());
        assertEquals("Employee", user.getRole());
    }

    /**
     * Checks if password does not match.
     * */
    @Test
    public void testPasswordMismatchValidation() {
        assertFalse(passwordsMatch("password123", "different"));
    }

    /**
     * Checks if password matches.
     * */
    @Test
    public void testPasswordMatchValidation() {
        assertTrue(passwordsMatch("password123", "password123"));
    }

    /**
     * Checks if correct email is passing validation and incorrect is failing validation.
     * */
    @Test
    public void testEmailValidation() {
        assertTrue(isValidEmail("abeed.mulla@dal.ca"));
        assertFalse(isValidEmail("invalid-email"));
    }

    /**
     * Checks validation for required text field.
     * */
    @Test
    public void testRequiredFieldValidation() {
        assertFalse(isFieldEmpty("text"));
        assertTrue(isFieldEmpty(""));
        assertTrue(isFieldEmpty(null));
    }

    // Helper methods that validate the input

    /**
     * Checks if both password matches.
     * @param password user's password
     * @param confirmPassword repeated password
     * @return true if both matched otherwise false.
     * */
    private boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Checks if email is valid.
     * @param email  user's email
     * @return true if is valid otherwise false.
     * */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    /**
     * Checks if field is empty.
     * @param field required text field
     * @return true if is empty or null otherwise false.
     * */
    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}