package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Application;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Application} model class.
 */
public class ApplicationDetailsJUnitTest {

    private Application application;

    /**
     * Initializes a sample Application object before each test.
     */
    @Before
    public void setup() {
        application = new Application("id111", "employee@email.com", "test");
        application.setStatus("pending");
    }

    /**
     * Tests that all fields of an application are set and gotten
     */
    @Test
    public void testApplicationModelFields() {
        assertEquals("id111", application.getJobId());
        assertEquals("employee@email.com", application.getEmployeeEmail());
        assertEquals("test", application.getCoverLetter());
        assertEquals("pending", application.getStatus());
    }

    /**
     * Tests that the status field is updated correctly.
     */
    @Test
    public void testStatusUpdateLogic() {
        application.setStatus("accepted");
        assertEquals("accepted", application.getStatus());
    }

    /**
     * Tests error handling when cover letter is set to null.
     */
    @Test
    public void testNullCoverLetter() {
        application.setCoverLetter(null);
        assertNull(application.getCoverLetter());
    }

    /**
     * Tests error handling when an empty string is assigned to the cover letter.
     */
    @Test
    public void testEmptyCoverLetter() {
        application.setCoverLetter("");
        assertTrue(application.getCoverLetter().isEmpty());
    }
}