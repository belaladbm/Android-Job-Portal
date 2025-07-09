package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.model.Job;
import com.example.group14_projectiteration1_quickcash.view.JobDetailsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JobDetailsActivityTest {

    @Test
    public void jobDetailsScreen_displaysJobTitleDescriptionAndApplyNowButton() {
        // Create test job
        Job job = new Job(
                "Dog Walker",
                "Walk the dog every morning",
                "Pet Care",
                "Halifax",
                "employer@example.com"
        );

        // Prepare intent
        Intent intent = new Intent();
        intent.putExtra("job", job);
        intent.putExtra("userEmail", "employee@example.com");

        // Launch JobDetailsActivity
        try (ActivityScenario<JobDetailsActivity> scenario = ActivityScenario.launch(intent.setClassName(
                "com.example.group14_projectiteration1_quickcash",
                "com.example.group14_projectiteration1_quickcash.view.JobDetailsActivity"
        ))) {
            // Check title
            onView(withId(R.id.detailTitleTextView))
                    .check(matches(withText("Dog Walker")));

            // Check description
            onView(withId(R.id.detailDescriptionTextView))
                    .check(matches(withText("Walk the dog every morning")));

            // Check Apply Now button is visible and has correct text
            onView(withId(R.id.applyNowButton))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Apply Now")));
        }
    }
}
