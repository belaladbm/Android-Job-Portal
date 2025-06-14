package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResetPasswordJunitTest {

    /**
     * Checks if correct email is passing validation.
     * */
    @Test
    public void testValidEmail() {
        assertTrue(isValidEmail("user@example.com"));
    }

    /**
     * Checks if incorrect email is failing validation.
     * */
    @Test
    public void testInvalidEmailMissingSymbol() {
        assertFalse(isValidEmail("userexample.com"));
    }

    /**
     * Checks if empty email is failing validation.
     * */
    @Test
    public void testInvalidEmail_empty() {
        assertFalse(isValidEmail(""));
    }

    /**
     * Checks id null email is failing validation.
     * */
    @Test
    public void testInvalidEmail_null() {
        assertFalse(isValidEmail(null));
    }

    /**
     * Checks if email is valid.
     * @param email user's email
     * @return if is valid otherwise false.
     * */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}