package com.example.group14_projectiteration1_quickcash;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.group14_projectiteration1_quickcash.view.SettingsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Before
    public void launchSettings() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), SettingsActivity.class);
        intent.putExtra("userEmail", "test@example.com");
        ActivityScenario.launch(intent);
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
    public void testResetPasswordButtonText() {
        onView(withId(R.id.resetButton)).check(matches(withText("Reset Password")));
    }
}