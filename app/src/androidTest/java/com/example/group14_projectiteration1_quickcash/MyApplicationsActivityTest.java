package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;
import com.example.group14_projectiteration1_quickcash.view.MyApplicationsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MyApplicationsActivityTest {

    @Rule
    public ActivityScenarioRule<MyApplicationsActivity> activityRule =
            new ActivityScenarioRule<>(MyApplicationsActivity.class);

    @Test
    public void testMyApplicationsUI() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MyApplicationsActivity.class);
        intent.putExtra("userEmail", "abeed@gmail.com");
        ActivityScenario<MyApplicationsActivity> scenario = ActivityScenario.launch(intent);
        onView(withId(R.id.myApplicationsTitle))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTitleIsDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MyApplicationsActivity.class);
        intent.putExtra("userEmail", "abd@gmail.com");
        ActivityScenario.launch(intent);

        onView(withId(R.id.myApplicationsTitle))
                .check(matches(isDisplayed()))
                .check(matches(withText("My Job Applications")));
    }

    @Test
    public void testRecyclerViewIsDisplayed() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MyApplicationsActivity.class);
        intent.putExtra("userEmail", "abeed@gmail.com");
        ActivityScenario.launch(intent);

        onView(withId(R.id.myApplicationsRecyclerView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyStateIsDisplayedWhenNoApplications() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MyApplicationsActivity.class);
        intent.putExtra("userEmail", "test@testing.com");
        ActivityScenario.launch(intent);

        onView(withId(R.id.emptyStateText))
                .check(matches(isDisplayed()))
                .check(matches(withText("No applications yet")));
    }
}