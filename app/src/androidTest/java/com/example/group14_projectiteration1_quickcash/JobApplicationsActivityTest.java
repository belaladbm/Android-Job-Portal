package com.example.group14_projectiteration1_quickcash;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.group14_projectiteration1_quickcash.view.JobApplicationsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class JobApplicationsActivityTest {
    private static final String TEST_JOB_ID = "randomJob111";
    private static final String TEST_JOB_TITLE = "randomJobTitle";

    @Before
    public void launchJobApplicationsActivity() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                JobApplicationsActivity.class
        );
        intent.putExtra("jobId", TEST_JOB_ID);
        intent.putExtra("jobTitle", TEST_JOB_TITLE);

        ActivityScenario.launch(intent);
    }

    /**
     * Test if the applications RecyclerView is visible on screen
     */
    @Test
    public void testRecyclerViewIsVisible() {
        onView(withId(R.id.recyclerViewApplications)).check(matches(isDisplayed()));
    }

    /**
     * Verify RecyclerView is still present
     */
    @Test
    public void testApplicationsDisplayAfterLoad() {
        try {
            Thread.sleep(2000); // Let Firebase load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recyclerViewApplications)).check(matches(isDisplayed()));
    }
}