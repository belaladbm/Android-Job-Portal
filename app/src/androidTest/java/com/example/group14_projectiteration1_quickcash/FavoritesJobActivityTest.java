package com.example.group14_projectiteration1_quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.group14_projectiteration1_quickcash.model.CurrentUser;
import com.example.group14_projectiteration1_quickcash.model.User;
import com.example.group14_projectiteration1_quickcash.view.FavoritesJobActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FavoritesJobActivityTest {

    @Rule
    public ActivityScenarioRule<FavoritesJobActivity> activityRule =
            new ActivityScenarioRule<>(FavoritesJobActivity.class);

    @Before
    public void setUp() {
        // Set up a mock current user to prevent IllegalStateException
        User mockUser = new User();
        mockUser.setUserId("test_user_id");
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setRole("Employee");

        CurrentUser.getInstance().setCurrentUser(mockUser);
    }

    /**
     * Test to verify that the activity launches successfully
     */
    @Test
    public void testActivityLaunchesSuccessfully() {
        // The activity should launch successfully with the ActivityScenarioRule
        activityRule.getScenario().onActivity(activity -> {
            // Activity should be created successfully
            assert activity != null;
        });
    }

    /**
     * Test to verify that the header text is displayed
     */
    @Test
    public void testHeaderTextIsDisplayed() {
        // Check that the header text is displayed
        onView(withText("Your Favorite Jobs"))
                .check(matches(isDisplayed()));
    }

    /**
     * Test to verify that the "Remove All Favorite Jobs" button is displayed
     */
    @Test
    public void testRemoveAllFavoritesButtonIsDisplayed() {
        // Check that the remove all favorites button is displayed
        onView(withId(R.id.removeAllFavoritesButton))
                .check(matches(isDisplayed()));
    }

    /**
     * Test to verify that the RecyclerView is displayed
     */
    @Test
    public void testRecyclerViewIsDisplayed() {
        // Check that the RecyclerView is displayed
        onView(withId(R.id.favoritesRecyclerView))
                .check(matches(isDisplayed()));
    }

    /**
     * Test to verify that the "Remove All Favorite Jobs" button is clickable
     */
    @Test
    public void testRemoveAllFavoritesButtonIsClickable() {
        // Check that the remove all favorites button is clickable
        onView(withId(R.id.removeAllFavoritesButton))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    /**
     * Test to verify the activity layout structure
     */
    @Test
    public void testActivityLayoutStructure() {
        // Verify all main UI elements are present
        onView(withText("Your Favorite Jobs")).check(matches(isDisplayed()));
        onView(withId(R.id.removeAllFavoritesButton)).check(matches(isDisplayed()));
        onView(withId(R.id.favoritesRecyclerView)).check(matches(isDisplayed()));
    }

    /**
     * Test to verify that the activity handles empty favorites list
     */
    @Test
    public void testActivityHandlesEmptyFavorites() {
        // Verify the RecyclerView is still displayed even with no items
        onView(withId(R.id.favoritesRecyclerView))
                .check(matches(isDisplayed()));
    }
}