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
        application.status = "pending";
    }

    @Test
    public void testApplicationInitialization() {
        assertEquals("id111", application.jobId);
        assertEquals("employee@email.com", application.employeeEmail);
        assertEquals("cover letter template", application.coverLetter);
        assertEquals("pending", application.status);
    }

    @Test
    public void testUpdateToAcceptedStatus() {
        application.status = "accepted";
        assertEquals("accepted", application.status);
    }

    @Test
    public void testUpdateToRejectedStatus() {
        application.status = "rejected";
        assertEquals("rejected", application.status);
    }

    @Test
    public void testEmailIsUsedAsApplicationId() {
        String key = application.employeeEmail;
        assertEquals("employee@email.com", key);
    }

    @Test
    public void testNullJobId() {
        application.jobId = null;
        assertNull(application.jobId);
    }

    @Test
    public void testEmptyEmployeeEmail() {
        application.employeeEmail = "";
        assertTrue(application.employeeEmail.isEmpty());
    }
}