package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JobPostActivityTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> rule =
            new ActivityScenarioRule<>(DashboardActivity.class);

    // The Job post button opens the form screen.
    @Test
    public void postJobButton_opensJobPostForm() {
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.jobNameEditText)).check(matches(isDisplayed()));
    }

    // The form fields are available.
    @Test
    public void jobPostFormFields_areDisplayed() {
        onView(withId(R.id.postJobButton)).perform(click());

        onView(withId(R.id.jobNameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.jobDescriptionEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.categorySpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.submitJobButton)).check(matches(isDisplayed()));
    }
}