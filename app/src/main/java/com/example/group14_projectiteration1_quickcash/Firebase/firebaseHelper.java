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










//import com.google.firebase.database.DatabaseReference;
//import com.example.group14_projectiteration1_quickcash.User;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class firebaseHelper {
//    private static final String FIREBASE_URL = "https://group-14-project-quickca-c43bd-default-rtdb.firebaseio.com";
//    private final FirebaseDatabase firebaseDatabase;
//    private DatabaseReference dbref;
//
//    public FirebaseHelper() {
//        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
//        dbref = firebaseDatabase.getReference("User details");
//    }
//
//    public void saveUser(User user, DatabaseOperationCallback callback) {
//        dbref.push().setValue(user).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                callback.onSuccess();
//            } else {
//                callback.onFailure(task.getException());
//            }
//        });
//    }
//
//    public interface DatabaseOperationCallback {
//        void onSuccess();
//        void onFailure(Exception exception);
//    }
//}

