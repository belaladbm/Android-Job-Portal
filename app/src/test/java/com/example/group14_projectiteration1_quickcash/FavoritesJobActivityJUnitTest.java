package com.example.group14_projectiteration1_quickcash;

import com.example.group14_projectiteration1_quickcash.model.CurrentUser;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.model.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for the FavoritesJobActivity functionality.
 * Tests the business logic for managing favorite jobs.
 */
public class FavoritesJobActivityJUnitTest {

    private List<Job> favoriteJobs;
    private Set<String> favoriteJobIds;
    private User testUser;
    private CurrentUser currentUser;

    //Sets up test data before each test method.

    @Before
    public void setup() {
        // Initialize test data
        favoriteJobs = new ArrayList<>();
        favoriteJobIds = new HashSet<>();

        // Create test user
        testUser = new User("testUserId123", "John", "Doe", "john@example.com",
                "password123", "password123", "Employee");

        // Set up CurrentUser singleton
        currentUser = CurrentUser.getInstance();
        currentUser.setCurrentUser(testUser);
    }

    //Tests that the current user ID is correctly retrieved.
    @Test
    public void testGetCurrentUserId() {
        String userId = currentUser.getCurrentUser().getUserId();
        assertEquals("testUserId123", userId);
    }

    //Tests adding a job to favorites list.
    @Test
    public void testAddJobToFavorites() {
        Job testJob = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");

        favoriteJobs.add(testJob);
        favoriteJobIds.add(testJob.getJobId());

        assertEquals(1, favoriteJobs.size());
        assertTrue(favoriteJobIds.contains(testJob.getJobId()));
        assertEquals("Software Developer", favoriteJobs.get(0).getTitle());
    }

    //Tests adding multiple jobs to favorites.
    @Test
    public void testAddMultipleJobsToFavorites() {
        Job job1 = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");
        Job job2 = new Job("Data Analyst", "Analyze data",
                "Analytics", "Toronto", 30.0, "analyst@example.com");

        favoriteJobs.add(job1);
        favoriteJobs.add(job2);
        favoriteJobIds.add(job1.getJobId());
        favoriteJobIds.add(job2.getJobId());

        assertEquals(2, favoriteJobs.size());
        assertEquals(2, favoriteJobIds.size());
        assertTrue(favoriteJobIds.contains(job1.getJobId()));
        assertTrue(favoriteJobIds.contains(job2.getJobId()));
    }

    //Tests removing all jobs from favorites.
    @Test
    public void testRemoveAllFavorites() {
        // Add some jobs first
        Job job1 = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");
        Job job2 = new Job("Data Analyst", "Analyze data",
                "Analytics", "Toronto", 30.0, "analyst@example.com");

        favoriteJobs.add(job1);
        favoriteJobs.add(job2);
        favoriteJobIds.add(job1.getJobId());
        favoriteJobIds.add(job2.getJobId());

        // Verify jobs were added
        assertEquals(2, favoriteJobs.size());
        assertEquals(2, favoriteJobIds.size());

        // Remove all favorites
        favoriteJobs.clear();
        favoriteJobIds.clear();

        // Verify all jobs were removed
        assertEquals(0, favoriteJobs.size());
        assertEquals(0, favoriteJobIds.size());
        assertFalse(favoriteJobIds.contains(job1.getJobId()));
        assertFalse(favoriteJobIds.contains(job2.getJobId()));
    }

    //Tests that favorite jobs list is not null after initialization.
    @Test
    public void testFavoritesListNotNull() {
        assertNotNull(favoriteJobs);
        assertNotNull(favoriteJobIds);
    }

    // Tests that a job can be found in favorites by its ID.
    @Test
    public void testFindJobInFavoritesById() {
        Job testJob = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");

        favoriteJobs.add(testJob);
        favoriteJobIds.add(testJob.getJobId());

        boolean found = favoriteJobIds.contains(testJob.getJobId());
        assertTrue(found);
    }

    //Tests that a non-existent job ID is not found in favorites.
    @Test
    public void testNonExistentJobIdNotInFavorites() {
        String nonExistentJobId = "non-existent-id-123";
        assertFalse(favoriteJobIds.contains(nonExistentJobId));
    }

    //Tests that duplicate job IDs are not added to the set.
    @Test
    public void testDuplicateJobIdsNotAdded() {
        Job job1 = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");
        Job job2 = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");

        // Set the same job ID for both jobs
        job2.setJobId(job1.getJobId());

        favoriteJobIds.add(job1.getJobId());
        favoriteJobIds.add(job2.getJobId());

        // Should only have one unique job ID
        assertEquals(1, favoriteJobIds.size());
    }

    //Tests that job details are correctly stored and retrieved.
    @Test
    public void testJobDetailsCorrectlyStored() {
        Job testJob = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");

        favoriteJobs.add(testJob);

        Job retrievedJob = favoriteJobs.get(0);
        assertEquals("Software Developer", retrievedJob.getTitle());
        assertEquals("Develop Android apps", retrievedJob.getDescription());
        assertEquals("Technology", retrievedJob.getCategory());
        assertEquals("Halifax", retrievedJob.getLocation());
        assertEquals(25.0, retrievedJob.getJobPay(), 0.01);
        assertEquals("employer@example.com", retrievedJob.getEmployerEmail());
    }

    //Tests that the current user has valid information.
    @Test
    public void testCurrentUserValidInformation() {
        User currentUserData = currentUser.getCurrentUser();

        assertNotNull(currentUserData);
        assertEquals("testUserId123", currentUserData.getUserId());
        assertEquals("John", currentUserData.getFirstName());
        assertEquals("Doe", currentUserData.getLastName());
        assertEquals("john@example.com", currentUserData.getEmail());
        assertEquals("Employee", currentUserData.getRole());
    }

    //Tests that favorite jobs list maintains order when jobs are added.
    @Test
    public void testFavoritesListOrder() {
        Job job1 = new Job("First Job", "First description",
                "Category1", "Location1", 20.0, "employer1@example.com");
        Job job2 = new Job("Second Job", "Second description",
                "Category2", "Location2", 30.0, "employer2@example.com");

        favoriteJobs.add(job1);
        favoriteJobs.add(job2);

        assertEquals("First Job", favoriteJobs.get(0).getTitle());
        assertEquals("Second Job", favoriteJobs.get(1).getTitle());
    }

    //Tests that removing a specific job from favorites works correctly.

    @Test
    public void testRemoveSpecificJobFromFavorites() {
        Job job1 = new Job("Software Developer", "Develop Android apps",
                "Technology", "Halifax", 25.0, "employer@example.com");
        Job job2 = new Job("Data Analyst", "Analyze data",
                "Analytics", "Toronto", 30.0, "analyst@example.com");

        favoriteJobs.add(job1);
        favoriteJobs.add(job2);
        favoriteJobIds.add(job1.getJobId());
        favoriteJobIds.add(job2.getJobId());

        // Remove job1
        favoriteJobs.remove(job1);
        favoriteJobIds.remove(job1.getJobId());

        assertEquals(1, favoriteJobs.size());
        assertEquals(1, favoriteJobIds.size());
        assertFalse(favoriteJobIds.contains(job1.getJobId()));
        assertTrue(favoriteJobIds.contains(job2.getJobId()));
        assertEquals("Data Analyst", favoriteJobs.get(0).getTitle());
    }
}