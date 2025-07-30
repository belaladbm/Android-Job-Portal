package com.example.group14_projectiteration1_quickcash.model;

import androidx.annotation.NonNull;

public class CurrentUser {
    private static volatile CurrentUser instance;
    private User currentUser;

    private CurrentUser() {}

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

    public void setCurrentUser(User user) {
        System.out.println("\n\n\n\n\n\n\nSetting current user: " + user + "\n\n\n\n\n\n\n");
        this.currentUser = user;
    }

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
