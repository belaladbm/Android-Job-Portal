package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Job;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Job} model class.
 */
public class EmployerJobsJUnitTest {

    private Job job;

    /**
     * Initializes a sample Job object before each test.
     */
    @Before
    public void setup() {
        job = new Job("Pilot", "Pilot airplanes", "Aviation", "Halifax", "employer@email.com");
    }

    /**
     * Tests if the Job model fields are correctly set and gotten.
     */
    @Test
    public void testJobModel() {
        assertEquals("Pilot", job.getTitle());
        assertEquals("Pilot airplanes", job.getDescription());
        assertEquals("Aviation", job.getCategory());
        assertEquals("Halifax", job.getLocation());
        assertEquals("employer@email.com", job.getEmployerEmail());
        assertNotNull(job.getJobId());
    }

    /**
     * Tests the generation of a job ID and error handling it is null or empty.
     */
    @Test
    public void testJobIdGeneration() {
        Job emptyJob = new Job();
        assertNull(emptyJob.getJobId());
        emptyJob.generateId();
        assertNotNull(emptyJob.getJobId());
        assertFalse(emptyJob.getJobId().isEmpty());
    }

    /**
     * Verifies that a job location is not null or empty.
     */
    @Test
    public void testJobLocationNotEmpty() {
        assertNotNull(job.getLocation());
        assertFalse(job.getLocation().trim().isEmpty());
    }

    /**
     * Tests that not setting a category is okay.
     */
    @Test
    public void testEmptyCategoryAllowed() {
        job.setCategory("");
        assertEquals("", job.getCategory());
    }

    /**
     * Tests that the job title can be set to null.
     */
    @Test
    public void testNullTitle() {
        job.setTitle(null);
        assertNull(job.getTitle());
    }

    /**
     * Tests if the employer email is correctly used as an identifier.
     */
    @Test
    public void testEmailIdentifier() {
        String key = job.getEmployerEmail();
        assertEquals("employer@email.com", key);
    }
}