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
        assertEquals("Pilot", job.getTitle());
        assertEquals("Pilot airplanes", job.getDescription());
        assertEquals("Aviation", job.getCategory());
        assertEquals("Halifax", job.getLocation());
        assertEquals("employer@email.com", job.getEmployerEmail());
        assertNotNull(job.getJobId());
    }

    @Test
    public void testJobIdGeneration() {
        Job emptyJob = new Job();
        assertNull(emptyJob.getJobId());
        emptyJob.generateId();
        assertNotNull(emptyJob.getJobId());
        assertFalse(emptyJob.getJobId().isEmpty());
    }

    @Test
    public void testJobLocationNotEmpty() {
        assertNotNull(job.getLocation());
        assertFalse(job.getLocation().trim().isEmpty());
    }

    @Test
    public void testEmptyCategoryAllowed() {
        job.setCategory("");
        assertEquals("", job.getCategory());
    }

    @Test
    public void testNullTitle() {
        job.setTitle(null);
        assertNull(job.getTitle());
    }

    @Test
    public void testEmailIdentifier() {
        String key = job.getEmployerEmail();
        assertEquals("employer@email.com", key);
    }
}