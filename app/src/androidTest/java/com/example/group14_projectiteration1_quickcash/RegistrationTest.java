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
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.group14_projectiteration1_quickcash.view.RegistrationActivity;

@RunWith(AndroidJUnit4.class)
public class RegistrationTest {

    @Rule
    public ActivityScenarioRule<RegistrationActivity> activityScenarioRule = new ActivityScenarioRule<>(RegistrationActivity.class);

    // Test case 1: Checking error message when all fields are empty
    @Test
    public void testEmptyFields() {

        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.editTextText)).check(matches(hasErrorText("First name is required")));
    }

    // Test case 2: Check error message when an invalid email is entered
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

    // Test case 3: Check error message when passwords do not match
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

    // Test case 4: Check successful registration
    @Test
    public void testSuccessfulRegistration() {
        onView(withId(R.id.editTextText)).perform(typeText("Abeed"), closeSoftKeyboard());
        onView(withId(R.id.editTextText2)).perform(typeText("Mulla"), closeSoftKeyboard());
        onView(withId(R.id.editTextText3)).perform(typeText("abeed.mulla@dal.ca"), closeSoftKeyboard());
        onView(withId(R.id.editTextText4)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.editTextText5)).perform(typeText("Password123"), closeSoftKeyboard());

        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Employee")).perform(click());

        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.editTextText3)).check(matches(withText("abeed.mulla@dal.ca")));
    }

}