package com.example.group14_projectiteration1_quickcash;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.group14_projectiteration1_quickcash.view.SearchActivity;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    @Rule
    public ActivityScenarioRule<SearchActivity> rule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void testSearchUIElementsVisible() {
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.editLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.editKeyword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewJobs)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchButtonIsClickable() {
        onView(withId(R.id.btnSearch)).check(matches(isClickable()));
    }


    @Test
    public void testSearchShowsResultsInRecyclerView() {
        onView(withId(R.id.editKeyword)).perform(typeText("cleaning"), closeSoftKeyboard());
        onView(withId(R.id.btnSearch)).perform(click());

        // Give time for Firebase to respond and UI to update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the RecyclerView is still displayed
        onView(withId(R.id.recyclerViewJobs)).check(matches(isDisplayed()));
    }
}
