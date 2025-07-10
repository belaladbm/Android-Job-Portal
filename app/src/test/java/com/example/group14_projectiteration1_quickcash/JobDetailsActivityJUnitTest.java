package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for checking when a toast should be shown in JobDetailsActivity
 * based on whether the user email is null or empty.
 */
public class JobDetailsActivityJUnitTest {

    /**
     * Simulates email validation logic from JobDetailsActivity.
     *
     * @param email The user's email
     * @return true if a toast should be shown (i.e., email is invalid)
     */
    private boolean shouldShowToast(String email) {
        return email == null || email.isEmpty();
    }

    @Test
    public void testApplyNow_withNullEmail_shouldShowToast() {
        String email = null;
        assertTrue(shouldShowToast(email));
    }

    @Test
    public void testApplyNow_withEmptyEmail_shouldShowToast() {
        String email = "";
        assertTrue(shouldShowToast(email));
    }

    @Test
    public void testApplyNow_withValidEmail_shouldNotShowToast() {
        String email = "employee@example.com";
        assertFalse(shouldShowToast(email));
    }
}
