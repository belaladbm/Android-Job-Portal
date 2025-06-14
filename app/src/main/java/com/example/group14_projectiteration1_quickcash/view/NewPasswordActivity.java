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

/**
 * Handles new(reset) password process.
 * */

public class NewPasswordActivity extends AppCompatActivity{
    private EditText yourNewPassword;
    private EditText confirmYourNewPassword;
    private Button submitButton;
    private firebaseHelper firebaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        yourNewPassword = findViewById(R.id.your_new_password);
        confirmYourNewPassword = findViewById(R.id.confirm_your_new_password);
        submitButton = findViewById(R.id.submitButton);
        firebaseHelper = new firebaseHelper();
        firebaseHelper.connectFirebase();
        email = getIntent().getStringExtra("email");

        submitButton.setOnClickListener(view-> resetPassword());
    }

    /**
     * Resets password after validating.
     * */
    private void resetPassword(){
        String password = yourNewPassword.getText().toString().trim();
        String confirmedPassword = confirmYourNewPassword.getText().toString().trim();
        // check if password is entered in correctly
        if(password.isEmpty() || confirmedPassword.isEmpty()){
            yourNewPassword.setError("please fill in all the blank fields");
            confirmYourNewPassword.setError("please fill in all the blank fields");
            return;
        }
        //check if the passwords match
        if(!password.equals(confirmedPassword)){
            confirmYourNewPassword.setError("passwords don't match");
            return;
        }
        //look for the email
        Query emailQuery = firebaseHelper.dbref.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //update the password if the email is found in the db
                    for(DataSnapshot userSnapshot: snapshot.getChildren()){
                        userSnapshot.getRef().child("password").setValue(password);
                        userSnapshot.getRef().child("confirmPassword").setValue(confirmedPassword);
                    }
                    Toast.makeText(NewPasswordActivity.this, "Password has been successfully reset", Toast.LENGTH_SHORT).show();
                    //redirect to login page
                    Intent intent = new Intent(NewPasswordActivity.this, LogInActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(NewPasswordActivity.this, "ERROR: User not found", Toast.LENGTH_SHORT).show();
                }}
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(NewPasswordActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );
    }
}
