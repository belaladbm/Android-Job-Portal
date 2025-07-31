package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.view.EditJobActivity;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for EditJobActivity
 * Verifies UI components are visible and interactable.
 */
@RunWith(AndroidJUnit4.class)
public class EditJobActivityTest {

    /**
     * Verifies all important UI elements are visible on screen.
     */
    @Test
    public void testAreAllFieldsDisplayed() {
        // Randomized job for testing
        Job job = new Job(
                "Test Title",
                "Test Description",
                "IT & Software",
                "Test Location",
                40.5,
                "employer@email.com"
        );
        job.setJobId("mockJobId");

        // Prepare intent
        Intent intent = new Intent();
        intent.putExtra("job", job);

        // Launch EditJobActivity
        try (ActivityScenario<EditJobActivity> scenario = ActivityScenario.launch(
                intent.setClassName(
                        "com.example.group14_projectiteration1_quickcash",
                        "com.example.group14_projectiteration1_quickcash.view.EditJobActivity"
                )
        )) {
            onView(withId(R.id.editJobTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.editJobDescription)).check(matches(isDisplayed()));
            onView(withId(R.id.editJobLocation)).check(matches(isDisplayed()));
            onView(withId(R.id.editJobCategorySpinner)).check(matches(isDisplayed()));
            onView(withId(R.id.saveJobButton)).check(matches(isDisplayed()));
            onView(withId(R.id.cancelEditButton)).check(matches(isDisplayed()));
        }
    }

    /**
     *Simulates editing job details and saving
     */
    @Test
    public void testEditAndSaveJob() {
        Job job = new Job(
                "Random Title",
                "Random Description",
                "IT & Software",
                "Random Location",
                50,
                "employer@email.com"
        );
        job.setJobId("mockJobId");

        Intent intent = new Intent();
        intent.putExtra("job", job);

        try (ActivityScenario<EditJobActivity> scenario = ActivityScenario.launch(
                intent.setClassName(
                        "com.example.group14_projectiteration1_quickcash",
                        "com.example.group14_projectiteration1_quickcash.view.EditJobActivity"
                )
        )) {
            // Edit fields
            onView(withId(R.id.editJobTitle)).perform(clearText(), typeText("Modified Title"), closeSoftKeyboard());
            onView(withId(R.id.editJobDescription)).perform(clearText(), typeText("Modified Description"), closeSoftKeyboard());
            onView(withId(R.id.editJobLocation)).perform(clearText(), typeText("Modified Location"), closeSoftKeyboard());

            // Click save
            onView(withId(R.id.saveJobButton)).perform(click());
        }
    }
}