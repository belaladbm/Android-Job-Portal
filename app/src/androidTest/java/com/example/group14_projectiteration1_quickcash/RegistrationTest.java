package com.example.group14_projectiteration1_quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

@RunWith(AndroidJUnit4.class)
public class RegistrationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEmptyFields() {

        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.editTextText)).check(matches(hasErrorText("First name is required")));
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.editTextText)).perform(typeText("abeed"), closeSoftKeyboard());
        onView(withId(R.id.editTextText2)).perform(typeText("mulla"), closeSoftKeyboard());
        onView(withId(R.id.editTextText3)).perform(typeText("invalidemail"), closeSoftKeyboard());
        onView(withId(R.id.editTextText4)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextText5)).perform(typeText("Password123"), closeSoftKeyboard());

        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.editTextText3)).check(matches(hasErrorText("Enter a valid email")));
    }

    @Test
    public void testPasswordsDoNotMatch() {
        onView(withId(R.id.editTextText)).perform(typeText("abeed"), closeSoftKeyboard());
        onView(withId(R.id.editTextText2)).perform(typeText("mulla"), closeSoftKeyboard());
        onView(withId(R.id.editTextText3)).perform(typeText("abeed.mulla@dal.ca"), closeSoftKeyboard());
        onView(withId(R.id.editTextText4)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextText5)).perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.editTextText5)).check(matches(hasErrorText("Passwords do not match")));
    }
}