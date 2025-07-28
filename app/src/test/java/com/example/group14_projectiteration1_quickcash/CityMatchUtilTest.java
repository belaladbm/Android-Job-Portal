// File: CityMatchUtilTest.java

package com.example.group14_projectiteration1_quickcash.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CityMatchUtilTest {

    @Test
    public void testMatchingCities() {
        String userCity = "Halifax";
        String jobCity = "halifax";

        // Case-insensitive match
        boolean matches = jobCity.toLowerCase().contains(userCity.toLowerCase());

        assertTrue("City should match", matches);
    }

    @Test
    public void testNonMatchingCities() {
        String userCity = "Halifax";
        String jobCity = "Dartmouth";

        boolean matches = jobCity.toLowerCase().contains(userCity.toLowerCase());

        assertFalse("City should not match", matches);
    }
}
