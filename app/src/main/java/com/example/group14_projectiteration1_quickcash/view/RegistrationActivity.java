package com.example.group14_projectiteration1_quickcash.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.User;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

/**
 * Handles registration(signup) process.
 */
public class RegistrationActivity extends AppCompatActivity {

    private firebaseHelper firebase_helper;

    // Registration page UI elements
    private Spinner roleSpinner;
    private EditText firstName, lastName, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize elements
        roleSpinner = findViewById(R.id.spinner);
        TextView loginPageLink = findViewById(R.id.loginPageLink);
        Button registerButton = findViewById(R.id.button);

        askForNotificationPermission();

        firebase_helper = new firebaseHelper();
        setupRoleSpinner();
        goToLoginThroughLink(loginPageLink);

        registerButton.setOnClickListener(v -> {
            firebase_helper.connectFirebase();  // Initializing firebase connection
            writeRegistration();                // Registering user
        });
    }

    /**
     * Validates user input, creates a User object, and saves it to Firebase.
     * Displays toast messages for successful or failed registrations.
     */
    private void writeRegistration() {
        initializeUIElements();

        // Input validation
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("First name is required");
            showToast("Registration Failed!, First name is required");
            return;
        }

        if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError("Last name is required");
            showToast("Registration Failed!, Last name is required");
            return;
        }

        String emailText = email.getText().toString();
        if (TextUtils.isEmpty(emailText)) {
            email.setError("Email is required");
            showToast("Registration Failed!, Email is required");
            return;
        }

        if (!emailText.contains("@")) {
            email.setError("Enter a valid email");
            showToast("Registration Failed!, Invalid email");
            return;
        }

        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(passwordText)) {
            password.setError("Password is required");
            showToast("Registration Failed!, Password is required");
            return;
        }

        if (!passwordText.equals(confirmPasswordText)) {
            confirmPassword.setError("Passwords do not match");
            showToast("Registration Failed!, Passwords do not match");
            return;
        }

        String selectedRole = roleSpinner.getSelectedItem().toString();
        if ("Select your role".equals(selectedRole)) {
            showToast("Registration Failed!, Please select a role");
            return;
        }

        // Creating a user object with the input data
        User user = new User(
                null, // ID is assigned by Firebase
                firstName.getText().toString(),
                lastName.getText().toString(),
                emailText,
                passwordText,
                confirmPasswordText,
                selectedRole
        );

        // Save user to Firebase
        firebase_helper.dbref.push().setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Registration successful!");
                redirectingToLogin();
            } else {
                showToast("Registration Failed!");
            }
        });
    }

    /**
     * Initializes the registration page UI elements.
     */
    private void initializeUIElements() {
        firstName = findViewById(R.id.editTextText);
        lastName = findViewById(R.id.editTextText2);
        email = findViewById(R.id.editTextText3);
        password = findViewById(R.id.editTextText4);
        confirmPassword = findViewById(R.id.editTextText5);
    }

    /**
     * Populates the role spinner with the roles: "Employee", and "Employer".
     */
    private void setupRoleSpinner() {
        List<String> roles = Arrays.asList("Select your role", "Employee", "Employer");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                roles
        );
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        roleSpinner.setAdapter(spinnerAdapter);
    }

    /**
     * Redirects the user to the login page after successful registration.
     */
    private void redirectingToLogin() {
        Intent intent = new Intent(RegistrationActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    /**
     * Shows a toast message.
     */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Redirects the user to the login page if user clicks on go to login page link
     */
    private void goToLoginThroughLink(TextView loginPageLink) {
        loginPageLink.setOnClickListener(v -> redirectingToLogin());
    }

    /**
     * Asks user for push notification permission on Android 13+.
     */
    private void askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1
                );
            }
        }
    }
}