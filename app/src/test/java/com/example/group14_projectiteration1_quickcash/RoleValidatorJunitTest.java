package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.*;
import com.example.group14_projectiteration1_quickcash.util.RoleValidator;

import org.junit.Test;

/**
 * Unit tests for the RoleValidator utility class.
 * Ensures that valid roles are accepted and invalid roles are rejected.
 */
public class RoleValidatorJunitTest {

    @Test
    public void testInvalidRoleRejected() {
        String role = "Select your role"; // Default spinner value
        assertFalse(RoleValidator.isValidRole(role));
    }

    @Test
    public void testValidRoleAccepted() {
        String role = "Employer";
        assertTrue(RoleValidator.isValidRole(role));
    }

    @Test
    public void testNullRoleRejected() {
        String role = null;
        assertFalse(RoleValidator.isValidRole(role));
    }

    @Test
    public void testEmptyStringRoleRejected() {
        String role = "";
        assertFalse(RoleValidator.isValidRole(role));
    }
}