package com.example.group14_projectiteration1_quickcash.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.group14_projectiteration1_quickcash.R;

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

    }
}