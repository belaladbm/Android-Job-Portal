package com.example.group14_projectiteration1_quickcash.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

//🔽 AT-3 requires these imports
 import android.widget.Spinner;
 import android.widget.ArrayAdapter;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group14_projectiteration1_quickcash.R;

public class SettingsActivity extends AppCompatActivity {

    Button logoutButton;


    private Button updateRoleButton;
    private Spinner roleSpinner;
    private DatabaseReference dbRef;
    private String currentUserEmail;
    private Button backToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logoutButton = findViewById(R.id.logoutButton);
        updateRoleButton = findViewById(R.id.updateRoleButton);
        roleSpinner = findViewById(R.id.roleSpinner);
        backToDashboard = findViewById(R.id.backButton);

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

        // Setup dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Setup button actions
        logoutButton.setOnClickListener(view -> showLogoutDialog());
        updateRoleButton.setOnClickListener(v -> updateRole());
    }



    // Role update logic
    private void updateRole() {
        String selectedRole = roleSpinner.getSelectedItem().toString();

        if (selectedRole.equals("Select your role")) {
            Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String emailInDB = userSnapshot.child("email").getValue(String.class);

                                userSnapshot.getRef().child("role").setValue(selectedRole)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(SettingsActivity.this,
                                                    "Role updated to " + selectedRole,
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
                                            intent.putExtra("userEmail", currentUserEmail);
                                            startActivity(intent);
                                        });
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

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, i) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        // Optional: FirebaseAuth.getInstance().signOut();
        Toast.makeText(SettingsActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
