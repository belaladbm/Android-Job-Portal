package com.example.group14_projectiteration1_quickcash.view;

public interface AccessTokenListener {
    void onAccessTokenReceived(String token);
    void onAccessTokenError(Exception exception);
}