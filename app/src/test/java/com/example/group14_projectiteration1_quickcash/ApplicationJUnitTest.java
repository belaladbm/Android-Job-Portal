package com.example.group14_projectiteration1_quickcash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.app.Application;

public class ApplicationJunitTest {

    private Application application;

    @Before
    public void setUp() {
        application = new Application("job123", "employee@example.com", "I am interested in this position.");
    }

    @Test
    public void testConstructor_initializesFieldsCorrectly() {
        assertEquals("job123", application.jobId);
        assertEquals("employee@example.com", application.employeeEmail);
        assertEquals("I am interested in this position.", application.coverLetter);
        assertEquals("pending", application.status);
    }

    @Test
    public void testDefaultConstructor_doesNotThrowException() {
        Application defaultApp = new Application();
        assertNotNull(defaultApp);
        assertNull(defaultApp.jobId);
        assertNull(defaultApp.employeeEmail);
        assertNull(defaultApp.coverLetter);
        assertNull(defaultApp.status);
    }

    @Test
    public void testStatus_canBeChangedAfterInitialization() {
        application.status = "accepted";
        assertEquals("accepted", application.status);
    }

    @Test
    public void testNullSafeFields() {
        application = new Application(null, null, null);
        assertNull(application.jobId);
        assertNull(application.employeeEmail);
        assertNull(application.coverLetter);
        assertEquals("pending", application.status);
    }
}