package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.Firebase.*;
import com.example.group14_projectiteration1_quickcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText yourEmail;
    private Button resetButton;
    private firebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        yourEmail = findViewById(R.id.your_email);
        resetButton = findViewById(R.id.resetButton);
        firebaseHelper = new firebaseHelper();
        firebaseHelper.connectFirebase();

        resetButton.setOnClickListener(view -> checkEmail());
    }
    private void checkEmail() {
        Toast.makeText(this, "Checking email...", Toast.LENGTH_SHORT).show();
        String email = yourEmail.getText().toString().trim();

        // check email is entered in correctly
        if (email.isEmpty() || !email.contains("@")) {
            yourEmail.setError("please provide your correct registered email");
            return;
        }
        //check if typed in email exists in the database
        Query emailQuery = firebaseHelper.dbref.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // if the email exists in the db we redeirect the user to newpassword page
                    Intent intent = new Intent(ResetPasswordActivity.this, NewPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "The email you entered doesn't exist in our records.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ResetPasswordActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
