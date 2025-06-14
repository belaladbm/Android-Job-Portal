package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.*;
import com.example.group14_projectiteration1_quickcash.util.RoleValidator;

import org.junit.Test;

public class RoleValidatorJunitTest {

    @Test
    public void testInvalidRoleRejected() {
        String role = "Select your role";
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