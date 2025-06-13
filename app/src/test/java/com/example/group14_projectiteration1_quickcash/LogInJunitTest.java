package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogInJunitTest {

    /**
     * Checks if correct email passed validation.
     * */
    @Test
    public void testValidEmail() {
        assertTrue(isValidEmail("user@example.com"));
    }

    /**
     * Checks if incorrect email fails validation.
     * */
    @Test
    public void testInvalidEmailMissingAtSymbol() {
        assertFalse(isValidEmail("userexample.com"));
    }

    /**
     * Checks if empty email fails validation.
     * */
    @Test
    public void testInvalidEmailEmpty() {
        assertFalse(isValidEmail(""));
    }

    /**
     * Checks if null email fails validation.
     * */
    @Test
    public void testInvalidEmailNull() {
        assertFalse(isValidEmail(null));
    }

    /**
     * Checks if non empty password passed validation.
     * */
    @Test
    public void testNonEmptyPassword() {
        assertFalse(isPasswordEmpty("securepassword"));
    }

    /**
     * Checks if empty password fails validation.
     * */
    @Test
    public void testEmptyPassword() {
        assertTrue(isPasswordEmpty(""));
    }

    /**
     * Checks if null password fails validation.
     * */
    @Test
    public void testNullPassword() {
        assertTrue(isPasswordEmpty(null));
    }

    // Helper Methods

    /**
     *Validates email.
     * @param email user's email.
     * @return true if valid otherwise false.
     * */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Validates password.
     * @param password user's account password
     * @return true if is valid otherwise false.
     * */
    private boolean isPasswordEmpty(String password) {
        return password == null || password.trim().isEmpty();
    }


}