package com.example.group14_projectiteration1_quickcash;

import org.junit.Test;
import static org.junit.Assert.*;

public class JobDetailsActivityJUnitTest {

    private boolean shouldShowToast(String email) {
        // Simulates the logic in JobDetailsActivity
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
