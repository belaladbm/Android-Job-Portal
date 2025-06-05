package com.example.group14_projectiteration1_quickcash.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firebaseHelper {

    //constructor
    public firebaseHelper(){}

    // Firebase database instance and reference
    private FirebaseDatabase firebaseDatabase;

    public DatabaseReference dbref;

    // Firebase URL
    private static final String FIREBASE_URL = "https://group-14-project-quickca-c43bd-default-rtdb.firebaseio.com";

    /**
     * Connects to Firebase Database and initializes the database reference.
     */
    public void connectFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        dbref = firebaseDatabase.getReference("User details");
    }
}
