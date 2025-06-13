package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewPasswordJunitTest {

    @Test
    public void testPasswordsMatch() {
        assertTrue(passwordsMatch("mdp213", "mdp213"));
    }

    @Test
    public void testPasswordsDoNotMatch() {
        assertFalse(passwordsMatch("mdp213", "mdp312"));
    }

    @Test
    public void testEmptyPasswords() {
        assertTrue(passwordsMatch("", ""));
    }

    @Test
    public void testPasswordIsEmpty() {
        assertTrue(isFieldEmpty(""));
    }

    @Test
    public void testPasswordNotEmpty() {
        assertFalse(isFieldEmpty("mdp213"));
    }

    @Test
    public void testPasswordIsNull() {
        assertTrue(isFieldEmpty(null));
    }

    private boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}