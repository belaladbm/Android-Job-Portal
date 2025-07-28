package com.example.group14_projectiteration1_quickcash.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.util.ToastMatcher;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DashboardActivityToastTest {

    @Test
    public void testToastShownOnNearbyJob() {
        // Launch the DashboardActivity
        ActivityScenario<DashboardActivity> scenario = ActivityScenario.launch(DashboardActivity.class);

        scenario.onActivity(activity -> {
            // Create mock job
            Job mockJob = new Job("Title", "Description", "IT", "Halifax", 100.0, "employer@example.com");
            mockJob.setJobId("mock-id-001");

            // Display toast manually (you must have this method in DashboardActivity)
            activity.displayNearbyJobAlert(mockJob);
        });

        // Wait for toast to appear
        SystemClock.sleep(2000);

        // Check if the toast is shown
        onView(withText("New job near you: Title"))
                .inRoot(new ToastMatcher())
                .check(matches(is(not(null))));
    }
}
