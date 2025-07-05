import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.core.StringContains.containsString;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.view.SearchActivity;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    @Rule
    public ActivityScenarioRule<SearchActivity> rule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void testSearchUIElementsAreVisible() {
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.editLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.editKeyword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewJobs)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchDisplaysExpectedJobItem() {
        // Type a known keyword (that matches job title or description in Firebase)
        onView(withId(R.id.editKeyword)).perform(typeText("developer"), closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.btnSearch)).perform(click());

        // Check if a job title containing "Software Developer" is displayed
        onView(withText(containsString("Developer"))).check(matches(isDisplayed()));
    }
}
