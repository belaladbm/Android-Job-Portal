package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.view.EmployerJobDetailsActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.containsString;

/**
 * Instrumented test for EmployerJobDetailsActivity
 * Verifies UI components are visible and interactable.
 */
@RunWith(AndroidJUnit4.class)
public class EmployerJobDetailsActivityTest {

    /**
     * Verifies all important UI elements are visible on screen.
     */
    @Test
    public void testAreAllFieldsDisplayedAndFilled() {
        // Randomized job for testing
        Job job = new Job(
                "Gardener",
                "Gardening gardens",
                "Landscaping & Gardening",
                "Halifax",
                "employer@email.com"
        );
        job.setJobId("mockJobId");

        // Prepare intent
        Intent intent = new Intent();
        intent.putExtra("job", job);

        // Launch EmployerJobDetailsActivity
        try (ActivityScenario<EmployerJobDetailsActivity> scenario = ActivityScenario.launch(
                intent.setClassName(
                        "com.example.group14_projectiteration1_quickcash",
                        "com.example.group14_projectiteration1_quickcash.view.EmployerJobDetailsActivity"
                )
        )) {
            onView(withId(R.id.detailTitleTextView)).check(matches(withText("Gardener")));
            onView(withId(R.id.detailDescriptionTextView)).check(matches(withText("Gardening gardens")));
            onView(withId(R.id.detailCategoryTextView)).check(matches(withText(containsString("Landscaping"))));
            onView(withId(R.id.detailLocationTextView)).check(matches(withText(containsString("Halifax"))));
            onView(withId(R.id.editJobButton)).check(matches(isDisplayed()));
            onView(withId(R.id.editJobButton)).check(matches(withText("Edit")));
        }
    }

    /**
     *Simulates clicking the Edit button.
     */
    @Test
    public void testClickEditButton() {
        Job job = new Job(
                "Title",
                "Description",
                "Cleaning & Housekeeping",
                "Halifax",
                "employer@email.com"
        );
        job.setJobId("mockJobId");

        Intent intent = new Intent();
        intent.putExtra("job", job);

        try (ActivityScenario<EmployerJobDetailsActivity> scenario = ActivityScenario.launch(
                intent.setClassName(
                        "com.example.group14_projectiteration1_quickcash",
                        "com.example.group14_projectiteration1_quickcash.view.EmployerJobDetailsActivity"
                )
        )) {
            onView(withId(R.id.editJobButton)).check(matches(isDisplayed()));
            onView(withId(R.id.editJobButton)).perform(click());

            // If EditJobActivity is opened it will show the edit title field
            onView(withId(R.id.editJobTitle)).check(matches(isDisplayed()));
        }
    }
}