package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
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

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeText = findViewById(R.id.dashboardText);

        //extracting email passed through intent
        String email = getIntent().getStringExtra("userEmail");

        //if there is not email
        if(email == null || email.isEmpty()) {
            welcomeText.setText("Welcome to QuickCash :)");
        }

        //getting database reference
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User details");

        // getting user email
        Query userQuery = dbRef.orderByChild("email").equalTo(email);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if user found
                if (dataSnapshot.exists()) {
                    //looping through dataset
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //getting data
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        //setting the welcome message
                        welcomeText.setText(getMessage(firstName, role));
                    }
                } else {
                    //default welcome message
                    welcomeText.setText("Welcome to QuickCash!");
                }
            }

            //if any error are there
            @Override
            public void onCancelled(DatabaseError error) {
                // 12. Handle errors
                welcomeText.setText("Welcome to QuickCash!");
                Toast.makeText(DashboardActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String getMessage(String firstName, String role){
        return "Welcome, " + firstName + "! Your role is " + role + "!";
    }
}