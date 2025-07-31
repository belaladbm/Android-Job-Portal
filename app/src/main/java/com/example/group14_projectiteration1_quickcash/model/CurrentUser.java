package com.example.group14_projectiteration1_quickcash.model;

import androidx.annotation.NonNull;
/**
 * This is a Singleton class that keeps track of the user who is logged in.
 * It makes sure there's only one copy of this class so the app always knows who the current user is.
 * Other parts of the app can use this to get or set the logged-in user anytime.
 */

public class CurrentUser {

    // The only instance of this class
    private static volatile CurrentUser instance;

    // This holds the user who is currently logged in
    private User currentUser;

    // Private constructor so no one else can make a new version of this class
    private CurrentUser() {}
    // This gives you the only CurrentUser object used everywhere in the app
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

    // Set who the current user is
    public void setCurrentUser(User user) {
        System.out.println("\n\n\n\n\n\n\nSetting current user: " + user + "\n\n\n\n\n\n\n");
        this.currentUser = user;
    }

    // Get the current user is
    public User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Current user not set");
        }
        return currentUser;
    }
    // Show the current user as a string
    @NonNull
    @Override
    public String toString() {
        return currentUser.toString();
    }
}
