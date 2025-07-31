package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Application;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Application} model.
 */
public class JobApplicationsJUnitTest {

    private Application application;

    /**
     * Initializes a sample {@link Application} object before each test.
     */
    @Before
    public void setup() {
        application = new Application("id111", "employee@email.com", "cover letter template", 20.2, "test1");
        application.setStatus("pending");
    }

    /**
     * Tests whether the application fields are initialized correctly.
     */
    @Test
    public void testApplicationInitialization() {
        assertEquals("id111", application.getJobId());
        assertEquals("employee@email.com", application.getEmployeeEmail());
        assertEquals("cover letter template", application.getCoverLetter());
        assertEquals("pending", application.getStatus());
    }

    /**
     * Tests if the application status is updated to "accepted".
     */
    @Test
    public void testUpdateToAcceptedStatus() {
        application.setStatus("accepted");
        assertEquals("accepted", application.getStatus());
    }

    /**
     * Tests if the application status is updated to "rejected".
     */
    @Test
    public void testUpdateToRejectedStatus() {
        application.setStatus("rejected");
        assertEquals("rejected", application.getStatus());
    }

    /**
     * Tests if the employee email is used correctly.
     */
    @Test
    public void testEmailIsUsedAsApplicationId() {
        String key = application.getEmployeeEmail();
        assertEquals("employee@email.com", key);
    }

    /**
     * Tests if the job ID can be set to null.
     */
    @Test
    public void testNullJobId() {
        application.setJobId(null);
        assertNull(application.getJobId());
    }

    /**
     * Tests if setting an empty string as employee email is okay.
     */
    @Test
    public void testEmptyEmployeeEmail() {
        application.setEmployeeEmail("");
        assertTrue(application.getEmployeeEmail().isEmpty());
    }
}