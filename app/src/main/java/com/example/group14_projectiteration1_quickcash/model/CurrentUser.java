package com.example.group14_projectiteration1_quickcash.model;

import android.util.Log;
import androidx.annotation.NonNull;

/**
 * Singleton class to store the currently logged-in user.
 */
public class CurrentUser {

    // Tag for logging
    private static final String TAG = "CurrentUser";

    // Volatile ensures visibility across threads
    private static volatile CurrentUser instance;
    private User currentUser;

    // Private constructor to prevent external instantiation
    private CurrentUser() {}

    /**
     * Returns the singleton instance of CurrentUser.
     * Uses double-checked locking for thread safety and performance.
     */
    public static CurrentUser getInstance() {
        if (instance == null) {
            synchronized (CurrentUser.class) {
                if (instance == null) {
                    instance = new CurrentUser();
                }
            }
        }
        return instance;
    }

    /**
     * Sets the current user and logs the operation.
     * @param user The user to set as current.
     */
    public void setCurrentUser(User user) {
        Log.d(TAG, "Setting current user: " + user);
        this.currentUser = user;
    }

    /**
     * Returns the current logged-in user.
     * @return The current User instance.
     * @throws IllegalStateException if user is not set.
     */
    public User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Current user not set");
        }
        return currentUser;
    }

    @NonNull
    @Override
    public String toString() {
        return currentUser.toString();
    }
}