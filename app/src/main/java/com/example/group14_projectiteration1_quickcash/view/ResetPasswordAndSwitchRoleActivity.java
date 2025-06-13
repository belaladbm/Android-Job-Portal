package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.Firebase.*;
import com.example.group14_projectiteration1_quickcash.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

/**
 * Handles both password reset and role switch process.*/

public class ResetPasswordAndSwitchRoleActivity extends AppCompatActivity {
    private EditText yourEmail;
    private Button resetButton;
    private firebaseHelper firebaseHelper;

    private TextView promptToUser;
    private String objective;
    private String userEmail;
    private String userName;
    private String currRole;
    private String roleToSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        yourEmail = findViewById(R.id.your_email);
        resetButton = findViewById(R.id.resetButton);
        promptToUser = findViewById(R.id.textView);

        objective = getIntent().getStringExtra("objective");
        userEmail = getIntent().getStringExtra("userEmail");

        if ("roleSwitch".equals(objective)) {
            userName = getIntent().getStringExtra("userName");
            currRole = getIntent().getStringExtra("currUserRole");
            roleToSwitch = getIntent().getStringExtra("switchToRole");

            // Update UI for role switching
            String promptText = userName + ", your role is being switched from " + currRole + " to " + roleToSwitch + ", please verify your email to do so";
            promptToUser.setText(promptText);
            resetButton.setText("Verify & Switch Role");
        }

        firebaseHelper = new firebaseHelper();
        firebaseHelper.connectFirebase();

        resetButton.setOnClickListener(view -> checkEmail());
    }

    /**
     * Validates user's email from firebase database.
     * */
    private void checkEmail() {
        String email = yourEmail.getText().toString().trim();

        // check email is entered in correctly
        if (email.isEmpty() || !email.contains("@")) {
            yourEmail.setError("please type in your correct registered email");
            return;
        }
        //check if typed in email exists in the database
        Query emailQuery = firebaseHelper.dbref.orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if ("roleSwitch".equals(objective)) {
                        if (!email.equals(userEmail)) {
                            Toast.makeText(ResetPasswordAndSwitchRoleActivity.this, "The email in incorrect!", Toast.LENGTH_LONG).show();
                        } else {
                            switchUserRole(snapshot);
                        }
                    } else {
                        Toast.makeText(ResetPasswordAndSwitchRoleActivity.this, "Request submitted.", Toast.LENGTH_SHORT).show();
                        // if the email exists in the db we redeirect the user to newpassword page
                        Intent intent = new Intent(ResetPasswordAndSwitchRoleActivity.this, NewPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ResetPasswordAndSwitchRoleActivity.this, "The email you entered doesn't exist in our records.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ResetPasswordAndSwitchRoleActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Switches user role.
     * */
    private void switchUserRole (DataSnapshot snap){
        for (DataSnapshot userSnapshot : snap.getChildren()) {
            userSnapshot.getRef().child("role").setValue(roleToSwitch).addOnSuccessListener(aVoid -> {
                Toast.makeText(ResetPasswordAndSwitchRoleActivity.this, "Role successfully changed to " + roleToSwitch, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetPasswordAndSwitchRoleActivity.this, DashboardActivity.class);intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {Toast.makeText(ResetPasswordAndSwitchRoleActivity.this, "Failed to update role!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
