package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;
import static org.junit.Assert.*;
public class JobPostActivityJunitTest {

    // if All fields are present it should pass.
    @Test
    public void testValidJobPost() {
        assertFalse(isFieldEmpty("Software Developer"));
        assertFalse(isFieldEmpty("Build and maintain Android apps"));
        assertTrue(isCategorySelected("IT & Software"));
        assertFalse(isFieldEmpty("Halifax, NS"));
    }

    //Job title is required.
    @Test
    public void testJobTitleRequired() {
        assertTrue(isFieldEmpty(""));
        assertTrue(isFieldEmpty(null));
    }

    //Job Description is required.
    @Test
    public void testDescriptionRequired() {
        assertFalse(isFieldEmpty("Part-time worker needed!!"));
        assertTrue(isFieldEmpty("   "));
    }

    // Job Location is required.
    @Test
    public void testLocationRequired() {
        assertFalse(isFieldEmpty("Toronto"));
        assertTrue(isFieldEmpty(""));
    }

    // Category must be selected.
    @Test
    public void testCategoryValidation() {
        assertFalse(isCategorySelected("Select job category"));
        assertTrue(isCategorySelected("Retail & Sales"));
    }

    // Helper methods
    /**
     * Returns true if the text field is empty
     */
    private boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    /**
     * Returns true if the chosen category is something other than "Select job category".
     */
    private boolean isCategorySelected(String category) {
        return category != null && !category.equals("Select job category");
    }
}

