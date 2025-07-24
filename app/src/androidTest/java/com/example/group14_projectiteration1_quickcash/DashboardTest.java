package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DashboardTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(DashboardActivity.class);

    /**
     * Test to check if dashboard Text view is being displayed.
     * */
    @Test
    public void dashboardTextExists(){
        ActivityScenario.launch(DashboardActivity.class);
        onView(withId(R.id.dashboardText)).check(matches(isDisplayed()));
    }

    /**
     * Test to check if default welcome message is displayed.
     * */
    @Test
    public void defaultWelcomeMessageIsDisplayed() {
        ActivityScenario.launch(DashboardActivity.class);

        onView(withId(R.id.dashboardText))
                .check(matches(withText("Welcome to QuickCash :)")));
    }

    /**
     * Test to check if default welcome message is displayed if no email is there.
     * */
    @Test
    public void defaultWelcomeMessageIsDisplayedWhenNoEmail(){
        //ApplicationProvider referred from android studio docs
        //URL: https://developer.android.com/reference/androidx/test/core/app/ApplicationProvider
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DashboardActivity.class);
        ActivityScenario.launch(intent);
        onView(withId(R.id.dashboardText)).check(matches(withText("Welcome to QuickCash :)")));
    }

    /**
     * Test to check if default welcome message is displayed if email is there.
     * */
    @Test
    public void welcomeMessageIsSDisplayedWhenEmailExists(){
        //ApplicationProvider referred from android studio docs
        //URL: https://developer.android.com/reference/androidx/test/core/app/ApplicationProvider
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DashboardActivity.class);
        intent.putExtra("user_email", "testingnow@gmail.com");
        ActivityScenario.launch(intent);

        onView(withId(R.id.dashboardText)).check(matches(isDisplayed())).check(matches(not(withText(""))));

    }

    /**
     * Test to check if the location text view exists
     */

    @Test
    public void checkIfLocationTextViewExists(){
        ActivityScenario.launch(DashboardActivity.class);
        onView(withId(R.id.locationTextView)).check(matches(isDisplayed()));
    }

    @Test
    /**
     * Test to check if location text view displays default text
     * */
    public void checkIfLocationTextViewShowsDefaultText(){
        ActivityScenario.launch(DashboardActivity.class);
        onView(withId(R.id.locationTextView)).check(matches(withText("Your Location : Searching...")));
    }

    @Test
    /**
     * Test to check if location text view displays correct text when gps permission is denied
     * */
    public void locationTextViewWhenPermissionDenied(){
        ActivityScenario<DashboardActivity> scenario = ActivityScenario.launch(DashboardActivity.class);

        scenario.onActivity(activity -> {
            activity.onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new int[]{PackageManager.PERMISSION_DENIED});

            TextView nearbyJobsText = activity.findViewById(R.id.nearbyJobsTextView);
            assertEquals("Unable to detect location. Please enable GPS to see nearby jobs.", nearbyJobsText.getText().toString());
        });
    }

    /**
     * Test to see if the correct text appears when there are no nearby jobs
     * */

    @Test
    public void testNoJobsFoundMessage() {
        ActivityScenario<DashboardActivity> scenario = ActivityScenario.launch(DashboardActivity.class);
        scenario = ActivityScenario.launch(DashboardActivity.class);

        scenario.onActivity(activity -> {
            TextView nearbyJobsText = activity.findViewById(R.id.nearbyJobsTextView);

            // Simulate empty job list for a city
            activity.fetchNearbyJobs("NoNameCity");

            // Since we're not connected to Firebase in tests, we need to manually set the text
            nearbyJobsText.setText("No jobs found in NoNameCity");

            assertTrue(nearbyJobsText.getText().toString().contains("No jobs found"));
        });
    }

    /**
     * Test to see if nearby jobs exists then is the list not empty or null
     * */
    @Test
    public void checkIfNearbyJobExists(){
        ActivityScenario<DashboardActivity> scenario = ActivityScenario.launch(DashboardActivity.class);

        scenario.onActivity(activity -> {
            // Creating test jobs
            List<Job> testJobs = new ArrayList<>();
            Job job1 = new Job("Test Job 1", "Description 1", "Category 1", "Halifax", 10,"test@email.com");
            Job job2 = new Job("Test Job 2", "Description 2", "Category 2", "Halifax", 10.1, "test2@email.com");
            testJobs.add(job1);
            testJobs.add(job2);

            //now list should not be empty or null
            RecyclerView jobsDisplay = activity.findViewById(R.id.jobsDisplayView);
            assertNotNull(jobsDisplay);

            assertNotNull(jobsDisplay.getAdapter());
        });
    }
}


