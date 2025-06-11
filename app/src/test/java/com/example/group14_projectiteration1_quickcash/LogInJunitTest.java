package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogInJunitTest {

    @Test
    public void testValidEmail() {
        assertTrue(isValidEmail("user@example.com"));
    }

    @Test
    public void testInvalidEmailMissingAtSymbol() {
        assertFalse(isValidEmail("userexample.com"));
    }

    @Test
    public void testInvalidEmailEmpty() {
        assertFalse(isValidEmail(""));
    }

    @Test
    public void testInvalidEmailNull() {
        assertFalse(isValidEmail(null));
    }

    @Test
    public void testNonEmptyPassword() {
        assertFalse(isPasswordEmpty("securepassword"));
    }

    @Test
    public void testEmptyPassword() {
        assertTrue(isPasswordEmpty(""));
    }

    @Test
    public void testNullPassword() {
        assertTrue(isPasswordEmpty(null));
    }

    // Helper Methods

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isPasswordEmpty(String password) {
        return password == null || password.trim().isEmpty();
    }


}