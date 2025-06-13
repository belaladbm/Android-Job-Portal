package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;
import com.example.group14_projectiteration1_quickcash.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Handles registration(signup) process.
 * */

public class RegistrationActivity extends AppCompatActivity {

    private firebaseHelper firebase_helper;

    // Registration page UI elements.
    private Spinner roleSpinner;

    private EditText firstName;

    private EditText lastName;

    private EditText email;

    private EditText password;

    private EditText confirmPassword;

    private TextView loginPageLink;

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

        // Initializing the spinner for role selection.
        roleSpinner = findViewById(R.id.spinner);

        loginPageLink = findViewById(R.id.loginPageLink);

        //making new object of firebaseHelper class
        firebase_helper = new firebaseHelper();

        RoleSpinner();

        goToLoginThroughLink(loginPageLink);

        Button b = findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_helper.connectFirebase();  // Initializing firebase connection
                writeRegistration(); // registering user.
            }
        });


    }

    /**
     * Validates user input, creates a User object, and saves it to Firebase.
     * Displays toast messages for successful or failed registrations.
     */
    private void writeRegistration(){

        initializeUIElements();

        // Input validation
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("First name is required");
            Toast.makeText(this, "Registration Failed!, First name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError("Last name is required");
            Toast.makeText(this, "Registration Failed!, Last name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Email is required");
            Toast.makeText(this, "Registration Failed!, email is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.getText().toString().contains("@")) {
            email.setError("Enter a valid email");
            Toast.makeText(this, "Registration Failed!, Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is required");
            Toast.makeText(this, "Registration Failed!, Password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            Toast.makeText(this, "Registration Failed!, Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedRole = roleSpinner.getSelectedItem().toString();
        if (selectedRole.equals("Select your role")) {
            Toast.makeText(this, "Registration Failed!, Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creating a user object with the input data
        User user = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                confirmPassword.getText().toString(),
                roleSpinner.getSelectedItem().toString()
        );

        // Saving the user to database.
        firebase_helper.dbref.push().setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                redirectingToLogin();
            } else {
                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    /**
     * Initializing the registration page UI elements.
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
    private void RoleSpinner() {
        List<String> roles = Arrays.asList(
                "Select your role",
                "Employee",
                "Employer"
        );

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
     * Redirects the user to the login page if user clicks on go to login page link*/
    private void goToLoginThroughLink(TextView loginPageLink){
        loginPageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectingToLogin();
            }
        });
    }
}