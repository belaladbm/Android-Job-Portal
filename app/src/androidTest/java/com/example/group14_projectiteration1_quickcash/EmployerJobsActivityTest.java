package com.example.group14_projectiteration1_quickcash;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.group14_projectiteration1_quickcash.view.EmployerJobsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EmployerJobsActivityTest {

    private static final String TEST_EMPLOYER_EMAIL = "testemployer@email.com";

    @Before
    public void launchEmployerJobsActivity() {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                EmployerJobsActivity.class
        );
        intent.putExtra("userEmail", TEST_EMPLOYER_EMAIL);
        ActivityScenario.launch(intent);
    }

    /**
     * Test if RecyclerView is displayed.
     */
    @Test
    public void testRecyclerViewIsVisible() {
        onView(withId(R.id.recyclerViewJobs)).check(matches(isDisplayed()));
    }

    /**
     * Test if jobs load and show in RecyclerView.
     */
    @Test
    public void testJobsDisplayAfterLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recyclerViewJobs)).check(matches(isDisplayed()));
    }
}