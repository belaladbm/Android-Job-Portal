package com.example.group14_projectiteration1_quickcash;

import static org.junit.Assert.*;

import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.model.Job;

import org.junit.Test;

public class ApplicationTest {
    @Test
    public void testApplicationCreation() {
        Application app = new Application("job123", "user@test.com", "I'm interested");
        assertEquals("job123", app.getJobId());
        assertEquals("user@test.com", app.getEmployeeEmail());
        assertEquals("I'm interested", app.getCoverLetter());
        assertEquals("pending", app.getStatus());
    }

    @Test
    public void testJobWithApplications() {
        Job job = new Job();
        job.generateId();
        assertNotNull(job.getJobId());

        Application app = new Application(job.getJobId(), "user@test.com", "Cover letter");
        job.getApplications().put("user@test.com", app);

        assertEquals(1, job.getApplications().size());
        assertTrue(job.getApplications().containsKey("user@test.com"));
    }
}