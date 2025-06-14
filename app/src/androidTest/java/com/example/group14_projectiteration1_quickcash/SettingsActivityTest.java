import com.example.group14_projectiteration1_quickcash.view.SettingsActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.group14_projectiteration1_quickcash.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Before
    public void launchSettings() {
        ActivityScenario.launch(SettingsActivity.class);
    }

    @Test
    public void logoutDialogAppearsAndConfirms() {
        onView(withId(R.id.logoutButton)).perform(click());
        onView(withText("Yes")).perform(click());
    }

    @Test
    public void logoutDialogCancelsProperly() {
        onView(withId(R.id.logoutButton)).perform(click());
        onView(withText("Cancel")).perform(click());
    }
}
