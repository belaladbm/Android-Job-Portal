package com.example.group14_projectiteration1_quickcash;

import android.view.View;

import com.example.group14_projectiteration1_quickcash.view.SettingsActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Before
    public void launchSettings() {
        ActivityScenario.launch(SettingsActivity.class);
    }

    @Test
    public void logoutDialogAppearsAndConfirms() {
        onView(withId(R.id.logoutButton)).perform(click());
        onView(withText("Yes")).perform(click());
    }

    @Test
    public void logoutDialogCancelsProperly() {
        onView(withId(R.id.logoutButton)).perform(click());
        onView(withText("Cancel")).perform(click());
    }

    @Test
    //checks if a toast message appears saying "Role updated to Employer"
    public void roleUpdateShowsToastConfirmation() {
        try (ActivityScenario<SettingsActivity> scenario = ActivityScenario.launch(SettingsActivity.class)) {
            final View[] decorView = new View[1];
            scenario.onActivity(activity -> decorView[0] = activity.getWindow().getDecorView());

            onView(withId(R.id.roleSpinner)).perform(click());
            onData(allOf(is(instanceOf(String.class)), is("Employer"))).perform(click());

            onView(withId(R.id.updateRoleButton)).perform(click());

            onView(withText(containsString("Role updated to Employer")))
                    .inRoot(withDecorView(not(is(decorView[0]))))
                    .check(matches(isDisplayed()));
        }
    }
}
