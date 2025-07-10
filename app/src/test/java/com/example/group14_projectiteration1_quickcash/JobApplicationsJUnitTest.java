package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Application;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JobApplicationsJUnitTest {

    private Application application;

    @Before
    public void setup() {
        application = new Application("id111", "employee@email.com", "cover letter template");
        application.setStatus("pending");
    }

    @Test
    public void testApplicationInitialization() {
        assertEquals("id111", application.getJobId());
        assertEquals("employee@email.com", application.getEmployeeEmail());
        assertEquals("cover letter template", application.getCoverLetter());
        assertEquals("pending", application.getStatus());
    }

    @Test
    public void testUpdateToAcceptedStatus() {
        application.setStatus("accepted");
        assertEquals("accepted", application.getStatus());
    }

    @Test
    public void testUpdateToRejectedStatus() {
        application.setStatus("rejected");
        assertEquals("rejected", application.getStatus());
    }

    @Test
    public void testEmailIsUsedAsApplicationId() {
        String key = application.getEmployeeEmail();
        assertEquals("employee@email.com", key);
    }

    @Test
    public void testNullJobId() {
        application.setJobId(null);
        assertNull(application.getJobId());
    }

    @Test
    public void testEmptyEmployeeEmail() {
        application.setEmployeeEmail("");
        assertTrue(application.getEmployeeEmail().isEmpty());
    }
}