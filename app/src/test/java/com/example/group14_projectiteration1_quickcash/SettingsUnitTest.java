package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.*;

import org.junit.Test;

public class SettingsUnitTest {

    @Test
    public void testLogoutFlagChange() {
        boolean isLoggedIn = true;
        isLoggedIn = false;
        assertFalse("User should be logged out", isLoggedIn);
    }


}
