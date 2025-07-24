package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.*;

import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.model.Job;

import org.junit.Test;

/**
 * Unit tests for Application and Job model classes.
 */
public class ApplicationTest {

    /**
     * Tests whether an Application object is correctly initialized.
     */
    @Test
    public void testApplicationCreation() {
        Application app = new Application("job123", "user@test.com", "I'm interested", 30, "Test1");

        assertEquals("job123", app.getJobId());
        assertEquals("user@test.com", app.getEmployeeEmail());
        assertEquals("I'm interested", app.getCoverLetter());
        assertEquals("pending", app.getStatus());
    }

    /**
     * Tests that a Job can store applications and that they are retrievable.
     */
    @Test
    public void testJobWithApplications() {
        Job job = new Job();
        job.generateId();  // Ensure jobId is not null
        assertNotNull(job.getJobId());

        Application app = new Application(job.getJobId(), "user@test.com", "Cover letter", 20, "Test2");
        job.getApplications().put("user@test.com", app);

        assertEquals(1, job.getApplications().size());
        assertTrue(job.getApplications().containsKey("user@test.com"));
    }
}