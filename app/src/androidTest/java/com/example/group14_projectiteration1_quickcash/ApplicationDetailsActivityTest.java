package com.example.group14_projectiteration1_quickcash;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.group14_projectiteration1_quickcash.view.ApplicationDetailsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ApplicationDetailsActivityTest {

    private static final String TEST_APPLICATION_ID = "-OUfZJzYO-WwI7zHXydE"; //imported directly from firebase
    private static final String TEST_JOB_ID = "9949632f-f8e5-4af7-893b-95620d40bfdf"; //corresponding jobID from firebase

    @Before
    public void launchActivity() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                ApplicationDetailsActivity.class);
        intent.putExtra("applicationId", TEST_APPLICATION_ID);
        intent.putExtra("jobId", TEST_JOB_ID);
        ActivityScenario.launch(intent);
    }

    @Test
    public void testUIElementsVisible() {
        onView(withId(R.id.applicantName)).check(matches(isDisplayed()));
        onView(withId(R.id.applicantEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.coverLetter)).check(matches(isDisplayed()));
        onView(withId(R.id.status)).check(matches(isDisplayed()));
        onView(withId(R.id.acceptBTN)).check(matches(isDisplayed()));
        onView(withId(R.id.declineBTN)).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonsAreClickable() {
        onView(withId(R.id.acceptBTN)).check(matches(isClickable()));
        onView(withId(R.id.declineBTN)).check(matches(isClickable()));
    }

    @Test
    public void testAcceptButtonClick() {
        onView(withId(R.id.acceptBTN)).perform(click());
    }

    @Test
    public void testDeclineButtonClick() {
        onView(withId(R.id.declineBTN)).perform(click());
    }
}