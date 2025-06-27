package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.group14_projectiteration1_quickcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * After successfully login user gets redirected to dashboard.
 * */

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button settingsButton;
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //initializing dashboard page UI elements
        welcomeText = findViewById(R.id.dashboardText);
        settingsButton = findViewById(R.id.settingsButton);
        locationText = findViewById(R.id.locationTextView);

        //extracting email passed through intent
        String email = getIntent().getStringExtra("userEmail");

        moveToSettingsPage(settingsButton, email);

        //if there is not email
        if (email == null || email.isEmpty()) {
            welcomeText.setText(getDefaultMessage());
        }

        //getting database reference
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User details");

        // making query
        Query userQuery = dbRef.orderByChild("email").equalTo(email);

        //executing query
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if user found
                if (dataSnapshot.exists()) {
                    //looping through database
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //getting data
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        //setting the welcome message
                        welcomeText.setText(getMessage(firstName, role));
                    }
                }
                //if user is not found
                else {
                    //default welcome message
                    welcomeText.setText(getDefaultMessage());
                }
            }

            //if any error are there while extracting data from firebase database
            @Override
            public void onCancelled(DatabaseError error) {
                welcomeText.setText(getDefaultMessage());
                Toast.makeText(DashboardActivity.this, "Error while extracting data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Generated customized welcome message
     * @param firstName User's first name
     * @param role User's current role
     * @return Customized welcome message String.
     * */
    public static String getMessage(String firstName, String role) {
        return "Welcome, " + firstName + "! Your role is " + role + "!";
    }

    /**
     * @return Default Welcome message
     * */
    public static String getDefaultMessage() {
        return "Welcome to QuickCash :)";
    }

    /**
     * Moves the user to settings page
     * @param settingsPageButton Button to take user to settings page
     * @param email User's email
     * */
    public void moveToSettingsPage(Button settingsPageButton, String email) {
        settingsPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
            intent.putExtra("userEmail", email); //  Send user email to settings
            startActivity(intent);
        });
    }
}