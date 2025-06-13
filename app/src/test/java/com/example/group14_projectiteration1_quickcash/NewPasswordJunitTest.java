package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewPasswordJunitTest {

    /**
     * Checks if password matches.
     * */
    @Test
    public void testPasswordsMatch() {
        assertTrue(passwordsMatch("mdp213", "mdp213"));
    }

    /**
     * Checks if password does not match.
     * */
    @Test
    public void testPasswordsDoNotMatch() {
        assertFalse(passwordsMatch("mdp213", "mdp312"));
    }

    /**
     * Checks if password is empty but matches.
     * */
    @Test
    public void testEmptyPasswords() {
        assertTrue(passwordsMatch("", ""));
    }

    /**
     * Checks if password is empty.
     * */
    @Test
    public void testPasswordIsEmpty() {
        assertTrue(isFieldEmpty(""));
    }

    /**
     * Checks if password is not empty.
     * */
    @Test
    public void testPasswordNotEmpty() {
        assertFalse(isFieldEmpty("mdp213"));
    }

    /**
     * Checks if password is null.
     * */
    @Test
    public void testPasswordIsNull() {
        assertTrue(isFieldEmpty(null));
    }

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
     * Check if field is empty.
     * @param field new password form field
     * @return true if is empty or null otherwise false.
     * */
    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}