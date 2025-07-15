package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MyApplicationsActivityTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> activityRule =
            new ActivityScenarioRule<>(DashboardActivity.class);

    @Test
    public void testNavigateToMyApplications() {
        // Click the My Applications button
        onView(withId(R.id.myApplicationsButton)).perform(click());

        // Check that the MyApplicationsActivity UI is displayed
        onView(withId(R.id.myApplicationsTitle))
                .check(matches(isDisplayed()));
    }
}