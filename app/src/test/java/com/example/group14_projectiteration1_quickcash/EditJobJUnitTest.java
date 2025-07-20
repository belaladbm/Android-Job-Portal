package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Job;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for job editing
 */
public class EditJobJUnitTest {

    private Job job;

    /**
     * Initializes a sample job object before each test.
     */
    @Before
    public void setup() {
        job = new Job(
                "Old Title",
                "Old Description",
                "IT & Software",
                "Old Location",
                "employer@email.com"
        );
        job.setJobId("job123");
    }

    /**
     * Tests if the job title has correctly been updated
     */
    @Test
    public void testUpdateJobTitle() {
        job.setTitle("New Title");
        assertEquals("New Title", job.getTitle());
    }

    /**
     * Tests if the job description has correctly been updated
     */
    @Test
    public void testUpdateJobDescription() {
        job.setDescription("New Description");
        assertEquals("New description", job.getDescription());
    }

    /**
     * Tests if the job location has correctly been updated
     */
    @Test
    public void testUpdateJobLocation() {
        job.setLocation("New Location");
        assertEquals("New Location", job.getLocation());
    }

    /**
     * Tests if the job category has correctly been updated
     */
    @Test
    public void testUpdateJobCategory() {
        job.setCategory("Retail & Sales");
        assertEquals("Retail & Sales", job.getCategory());
    }

    /**
     * Tests error handling when fields are set to null.
     */
    @Test
    public void testJobFieldsNotNull() {
        assertNotNull(job.getTitle());
        assertNotNull(job.getDescription());
        assertNotNull(job.getCategory());
        assertNotNull(job.getLocation());
    }
}