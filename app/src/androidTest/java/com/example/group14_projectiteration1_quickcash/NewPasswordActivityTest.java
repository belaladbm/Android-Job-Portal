package com.example.group14_projectiteration1_quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.view.NewPasswordActivity;

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
public class NewPasswordActivityTest {

    @Rule
    public ActivityScenarioRule<NewPasswordActivity> activityRule = new ActivityScenarioRule<>(NewPasswordActivity.class);
    //check error message for when password field is empty
    @Test
    public void testEmptyPasswordField() {
        onView(withId(R.id.submitButton)).perform(click());
        onView(withId(R.id.your_new_password)).check(matches(hasErrorText("please fill in all the blank fields")));
        onView(withId(R.id.confirm_your_new_password)).check(matches(hasErrorText("please fill in all the blank fields")));
    }
    //check error message for invalid password (passwords don't match)
    @Test
    public void testMismatchedPasswordsShowError() {
        onView(withId(R.id.your_new_password)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.confirm_your_new_password)).perform(typeText("Password321"), closeSoftKeyboard());
        onView(withId(R.id.submitButton)).perform(click());

        onView(withId(R.id.confirm_your_new_password)).check(matches(hasErrorText("passwords don't match")));
    }
    //check valid input
    @Test
    public void testValidPasswordsTriggersUpdate() {
        onView(withId(R.id.your_new_password)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.confirm_your_new_password)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.submitButton)).perform(click());
    }
    //check submit button text
    @Test
    public void testSubmitdButtonText() {
        onView(withId(R.id.submitButton)).check(matches(withText("Submit")));
    }
}