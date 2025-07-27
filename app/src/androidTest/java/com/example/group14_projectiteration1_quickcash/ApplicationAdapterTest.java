package com.example.group14_projectiteration1_quickcash;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.allOf;

import com.example.group14_projectiteration1_quickcash.view.JobApplicationsActivity;

@RunWith(AndroidJUnit4.class)
public class ApplicationAdapterTest {

    @Rule
    public ActivityScenarioRule<JobApplicationsActivity> activityRule =
            new ActivityScenarioRule<>(JobApplicationsActivity.class);

    @Test
    public void testApproveButtonVisibleWhenStatusIsCompleted() {
        onView(withId(R.id.recyclerViewApplications))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("Status: completed"))
                ));

        onView(allOf(withId(R.id.approveBTN), isDisplayed()))
                .check(matches(withText("Approve")));
    }

    @Test
    public void testAcceptDeclineButtonsVisibleWhenStatusIsPending() {
        onView(withId(R.id.recyclerViewApplications))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("Status: pending"))
                ));

        onView(allOf(withId(R.id.acceptBTN), isDisplayed()))
                .check(matches(withText("Accept")));

        onView(allOf(withId(R.id.declineBTN), isDisplayed()))
                .check(matches(withText("Decline")));
    }
}