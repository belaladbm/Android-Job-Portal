package com.example.group14_projectiteration1_quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.group14_projectiteration1_quickcash.view.LogInActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogInActivityTest {

    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void testEmailPasswordFieldsAndLoginButtonAreVisible() {
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void testErrorShownWhenEmailIsEmpty() {
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.editTextEmail)).check(matches(hasErrorText("Enter a valid email")));
    }

    @Test
    public void testErrorShownForInvalidEmailFormat() {
        onView(withId(R.id.editTextEmail))
                .perform(typeText("invalidemail"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.editTextEmail)).check(matches(hasErrorText("Enter a valid email")));
    }

    @Test
    public void testErrorShownWhenPasswordIsEmpty() {
        onView(withId(R.id.editTextEmail))
                .perform(typeText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.editTextPassword)).check(matches(hasErrorText("Enter your password")));
    }

    @Test
    public void testValidInputPerformsLoginAttempt() {
        onView(withId(R.id.editTextEmail))
                .perform(typeText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

    }

    @Test
    public void testLoginButtonText() {
        onView(withId(R.id.buttonLogin)).check(matches(withText("Login")));
    }

}
