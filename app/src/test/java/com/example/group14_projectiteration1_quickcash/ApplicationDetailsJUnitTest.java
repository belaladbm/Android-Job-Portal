package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Application;
import com.example.group14_projectiteration1_quickcash.view.ApplicationDetailsActivity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationDetailsJUnitTest {

    private Application application;

    @Before
    public void setup() {
        application = new Application("id111", "employee@email.com", "test");
        application.status = "pending";
    }

    @Test
    public void testApplicationModelFields() {
        assertEquals("id111", application.jobId);
        assertEquals("employee@email.com", application.employeeEmail);
        assertEquals("test", application.coverLetter);
        assertEquals("pending", application.status);
    }

    @Test
    public void testStatusUpdateLogic() {
        application.status = "accepted";
        assertEquals("accepted", application.status);
    }

    @Test
    public void testNullCoverLetter() {
        application.coverLetter = null;
        assertNull(application.coverLetter);
    }

    @Test
    public void testEmptyCoverLetter() {
        application.coverLetter = "";
        assertTrue(application.coverLetter.isEmpty());
    }
}