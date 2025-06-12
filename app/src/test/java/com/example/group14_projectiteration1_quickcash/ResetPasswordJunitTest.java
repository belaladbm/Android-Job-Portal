package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResetPasswordJunitTest {

    @Test
    public void testValidEmail() {
        assertTrue(isValidEmail("user@example.com"));
    }

    @Test
    public void testInvalidEmailMissingSymbol() {
        assertFalse(isValidEmail("userexample.com"));
    }

    @Test
    public void testInvalidEmail_empty() {
        assertFalse(isValidEmail(""));
    }

    @Test
    public void testInvalidEmail_null() {
        assertFalse(isValidEmail(null));
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}