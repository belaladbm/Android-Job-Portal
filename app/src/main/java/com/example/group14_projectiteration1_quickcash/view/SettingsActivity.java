package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

//🔽 AT-3 requires these imports
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.R;

/**
 * Provides user with settings options such as logout, reset password, role switch.
 */
public class SettingsActivity extends AppCompatActivity {

    Button logoutButton;
    Button resetPasswordButton;
    private DatabaseReference dbRef;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logoutButton = findViewById(R.id.logoutButton);
        Button switchRoleButton = findViewById(R.id.switchRoleButton);
        Button backToDashboard = findViewById(R.id.backButton);
        resetPasswordButton = findViewById(R.id.resetButton);

        // AT-16 — Find the new favorites button
        Button viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        // Get email passed from Dashboard
        currentUserEmail = getIntent().getStringExtra("userEmail");

        backToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
            intent.putExtra("userEmail", currentUserEmail);
            startActivity(intent);
        });

        // Safety check and log
        if (currentUserEmail == null || currentUserEmail.trim().isEmpty()) {
            Toast.makeText(this, " No email provided from Dashboard", Toast.LENGTH_LONG).show();
            Log.e("SettingsActivity", "Email from intent is null or empty");
            return;
        }

        dbRef = FirebaseDatabase.getInstance().getReference("User details");

        // Setup button actions
        logoutButton.setOnClickListener(view -> showLogoutDialog());
        switchRoleButton.setOnClickListener(v -> switchRole());
        //redirecting to reset password page after clicking on the appropriate button
        resetPasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ResetPasswordAndSwitchRoleActivity.class);
            startActivity(intent);
        });

        // Navigate to FavoriteJobActivity
        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, FavoritesJobActivity.class);
            intent.putExtra("userEmail", currentUserEmail);
            startActivity(intent);
        });
    }


    /***
     * Handles role switching process.
     * */
    private void switchRole() {

        dbRef.orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String emailInDB = userSnapshot.child("email").getValue(String.class);
                                String currRoleInDB = userSnapshot.child("role").getValue(String.class);
                                String userName = userSnapshot.child("firstName").getValue(String.class);
                                String switchToRole;
                                if (currRoleInDB.equals("Employee")) {
                                    switchToRole = "Employer";
                                } else {
                                    switchToRole = "Employee";
                                }

                                Intent intent = new Intent(SettingsActivity.this, ResetPasswordAndSwitchRoleActivity.class);
                                intent.putExtra("objective", "roleSwitch");
                                intent.putExtra("userEmail", emailInDB);
                                intent.putExtra("currUserRole", currRoleInDB);
                                intent.putExtra("userName", userName);
                                intent.putExtra("switchToRole", switchToRole);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(SettingsActivity.this,
                                    "User not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(SettingsActivity.this,
                                "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("SettingsActivity", "Firebase error: " + error.getMessage());
                    }
                });
    }

    /**
     * Displays logout popup.
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, i) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Executes logout procedure.
     */
    private void performLogout() {
        Toast.makeText(SettingsActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
