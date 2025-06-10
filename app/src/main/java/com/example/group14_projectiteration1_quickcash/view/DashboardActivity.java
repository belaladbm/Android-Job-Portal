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

    public String getMessage(String firstName, String role){
        return "Welcome, " + firstName + "! Your role is " + role + "!";
    }
}