package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.Application;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationDetailsJUnitTest {

    private Application application;

    @Before
    public void setup() {
        application = new Application("id111", "employee@email.com", "test");
        application.setStatus("pending");
    }

    @Test
    public void testApplicationModelFields() {
        assertEquals("id111", application.getJobId());
        assertEquals("employee@email.com", application.getEmployeeEmail());
        assertEquals("test", application.getCoverLetter());
        assertEquals("pending", application.getStatus());
    }

    @Test
    public void testStatusUpdateLogic() {
        application.setStatus("accepted");
        assertEquals("accepted", application.getStatus());
    }

    @Test
    public void testNullCoverLetter() {
        application.setCoverLetter(null);
        assertNull(application.getCoverLetter());
    }

    @Test
    public void testEmptyCoverLetter() {
        application.setCoverLetter("");
        assertTrue(application.getCoverLetter().isEmpty());
    }
}