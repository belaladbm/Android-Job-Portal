package com.example.group14_projectiteration1_quickcash;
import com.example.group14_projectiteration1_quickcash.model.Job;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmployerJobsJUnitTest {

    private Job job;

    @Before
    public void setup() {
        job = new Job("Pilot", "Pilot airplanes", "Aviation", "Halifax", "employer@email.com");
    }

    @Test
    public void testJobModel() {
        assertEquals("Pilot", job.title);
        assertEquals("Pilot airplanes", job.description);
        assertEquals("Aviation", job.category);
        assertEquals("Halifax", job.location);
        assertEquals("employer@email.com", job.employerEmail);
        assertNotNull(job.jobId);
    }

    @Test
    public void testJobIdGeneration() {
        Job emptyJob = new Job();
        assertNull(emptyJob.jobId);
        emptyJob.generateId();
        assertNotNull(emptyJob.jobId);
        assertFalse(emptyJob.jobId.isEmpty());
    }

    @Test
    public void testJobLocationNotEmpty() {
        assertNotNull(job.location);
        assertFalse(job.location.trim().isEmpty());
    }

    @Test
    public void testEmptyCategoryAllowed() {
        job.category = "";
        assertEquals("", job.category);
    }

    @Test
    public void testNullTitle() {
        job.title = null;
        assertNull(job.title);
    }

    @Test
    public void testEmailIdentifier() {
        String key = job.employerEmail;
        assertEquals("employer@email.com", key);
    }
}