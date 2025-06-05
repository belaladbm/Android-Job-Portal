package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.group14_projectiteration1_quickcash.model.User;

public class RegistrationJunitTest {

    @Test
    public void testValidUserCreation() {
        User user = new User(
                "Abeed",
                "Mulla",
                "abeed.mulla@dal.ca",
                "password123",
                "password123",
                "Employee"
        );

        assertEquals("Abeed", user.firstName);
        assertEquals("Mulla", user.lastName);
        assertEquals("abeed.mulla@dal.ca", user.email);
        assertEquals("Employee", user.role);
    }

    @Test
    public void testPasswordMismatchValidation() {
        assertFalse(passwordsMatch("password123", "different"));
    }

    @Test
    public void testPasswordMatchValidation() {
        assertTrue(passwordsMatch("password123", "password123"));
    }

    @Test
    public void testEmailValidation() {
        assertTrue(isValidEmail("abeed.mulla@dal.ca"));
        assertFalse(isValidEmail("invalid-email"));
    }

    @Test
    public void testRequiredFieldValidation() {
        assertFalse(isFieldEmpty("text"));
        assertTrue(isFieldEmpty(""));
        assertTrue(isFieldEmpty(null));
    }

    // Helper methods that validate the input

    private boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}