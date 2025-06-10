package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.group14_projectiteration1_quickcash.view.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;

public class DashboardTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(DashboardActivity.class);

    //Test to check if dashboard Text view is being displayed
    @Test
    public void dashboardTextExists(){
        ActivityScenario.launch(DashboardActivity.class);
        onView(withId(R.id.dashboardText)).check(matches(isDisplayed()));
    }

    //Test to check if default welcome message is displayed
    @Test
    public void defaultWelcomeMessageIsDisplayed() {
        ActivityScenario.launch(DashboardActivity.class);

        onView(withId(R.id.dashboardText))
                .check(matches(withText("Welcome to QuickCash!")));
    }
}
