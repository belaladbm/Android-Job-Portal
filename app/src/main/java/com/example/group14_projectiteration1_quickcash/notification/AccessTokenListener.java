package com.example.group14_projectiteration1_quickcash.notification;


public interface AccessTokenListener {
    void onAccessTokenReceived(String token);
    void onAccessTokenError(Exception exception);
}