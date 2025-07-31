package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Job;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for EmployerJobDetailsActivity behavior with job data.
 */
public class EmployerJobDetailsJUnitTest {

    private Job job;

    /**
     * Initializes a sample job object before each test.
     */
    @Before
    public void setup() {
        job = new Job(
                "Tutor",
                "Tutoring French for kids",
                "Childcare & Education",
                "Halifax",
                20,
                "employer@email.com"
        );
        job.setJobId("job213");
    }

    /**
     * Tests if the job details have correctly been set up and gotten.
     */
    @Test
    public void testJobDetailsAreCorrect() {
        assertEquals("Tutor", job.getTitle());
        assertEquals("Tutoring French for kids", job.getDescription());
        assertEquals("Childcare & Education", job.getCategory());
        assertEquals("Halifax", job.getLocation());
        assertEquals("employer@email.com", job.getEmployerEmail());
        assertEquals("job213", job.getJobId());
    }

    /**
     * Tests if the jobID has effectively been created.
     */
    @Test
    public void testJobIdIsSet() {
        assertNotNull(job.getJobId());
    }

    /**
     * Tests error handling for null jobID.
     */
    @Test
    public void testInvalidJobId() {
        job.setJobId(null);
        assertNull(job.getJobId());
    }

    /**
     * Tests detection for empty description.
     */
    @Test
    public void testEmptyDescription() {
        job.setDescription("");
        assertTrue(job.getDescription().isEmpty());
    }

    /**
     * Tests detection for empty title.
     */
    @Test
    public void testEmptyJobTitle(){
        job.setTitle("");
        assertTrue(job.getTitle().isEmpty());
    }
}