package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.assertEquals;

import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;

import org.junit.Test;

public class DashboardJunitTest {

    /**
     * Checks if welcome message is correct.
     * */
    @Test
    public void checkIfWelcomeMessageIsCorrect(){
        String firstName = "Testing";
        String role = "Employee";
        assertEquals("Welcome, Testing! Your role is Employee!", DashboardActivity.getMessage(firstName, role));
    }

    /**
     * Checks if default welcome message is correct.
     * */
    @Test
    public void checkIfDefaultWelcomeMessageIsCorrect(){
        String defaultWelcomeMessage = "Welcome to QuickCash :)";
        assertEquals(defaultWelcomeMessage, DashboardActivity.getDefaultMessage());
    }
}
