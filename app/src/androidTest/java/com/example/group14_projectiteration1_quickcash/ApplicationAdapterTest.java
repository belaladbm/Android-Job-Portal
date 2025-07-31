package com.example.group14_projectiteration1_quickcash;

import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.content.Intent;

import com.example.group14_projectiteration1_quickcash.view.JobApplicationsActivity;

@RunWith(AndroidJUnit4.class)
public class ApplicationAdapterTest {

    @Test
    public void testActivityLaunchesSuccessfully() {
        // Create intent with required extras
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), JobApplicationsActivity.class);
        intent.putExtra("jobId", "test_job_id");
        intent.putExtra("jobTitle", "Test Job");
        intent.putExtra("userEmail", "test@example.com");

        // Launch activity using ActivityScenario
        ActivityScenario<JobApplicationsActivity> scenario = ActivityScenario.launch(intent);

        // Verify that the activity launches and the RecyclerView is present
        onView(withId(R.id.recyclerViewApplications))
                .check(matches(isDisplayed()));
    }


    /**
     * This tests checks if Accept/Decline buttons is displayed after employee has applied for the jkob
     * */
    @Test
    public void testAcceptDeclineButtonsVisibleWhenStatusIsPending() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), JobApplicationsActivity.class);
        intent.putExtra("userEmail", "abeed@gmail.com");
        intent.putExtra("jobId", "-OWRouoo2Nk-HcSoAYNP");
        ActivityScenario.launch(intent);

        onView(withId(R.id.recyclerViewApplications))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("Status: pending"))
                ));

        // Launch activity using ActivityScenario
        ActivityScenario<JobApplicationsActivity> scenario = ActivityScenario.launch(intent);

        // Verify that the header text is displayed
        onView(withId(R.id.textApplicationsHeader))
                .check(matches(withText("Applications")));
    }
}