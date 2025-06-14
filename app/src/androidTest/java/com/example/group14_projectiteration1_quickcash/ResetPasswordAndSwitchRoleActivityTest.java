package com.example.group14_projectiteration1_quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.view.ResetPasswordAndSwitchRoleActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordAndSwitchRoleActivityTest {

    @Rule
    public ActivityScenarioRule<ResetPasswordAndSwitchRoleActivity> activityRule =
            new ActivityScenarioRule<>(ResetPasswordAndSwitchRoleActivity.class);

    /**
     * check error message for when email field is empty.
     * */
    @Test
    public void testEmptyEmailField() {
        onView(withId(R.id.resetButton)).perform(click());
        onView(withId(R.id.your_email)).check(matches(hasErrorText("please type in your correct registered email")));
    }
    /**
     * check error message for invalid email (without @).
     * */
    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.your_email)).perform(typeText("invalidEmail"), closeSoftKeyboard());
        onView(withId(R.id.resetButton)).perform(click());
        onView(withId(R.id.your_email)).check(matches(hasErrorText("please type in your correct registered email")));
    }
    /**
     * check valid input.
     * */
    @Test
    public void testValidEmail() {
        onView(withId(R.id.your_email)).perform(typeText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.resetButton)).perform(click());
    }
}