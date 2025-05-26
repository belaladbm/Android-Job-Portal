package com.example.group14_projectiteration1_quickcash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group14_projectiteration1_quickcash.R;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Firebase database instance and reference
    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference dbref;

    // Firebase URL
    private static final String FIREBASE_URL = "https://group-14-project-quickca-c43bd-default-rtdb.firebaseio.com";

    // Registration page UI elements.
    private Spinner roleSpinner;

    private EditText firstName;

    private EditText lastName;

    private EditText email;

    private EditText password;

    private EditText confirmPassword;

    private Button signUp;

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

        RoleSpinner();

        Button b = findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectFirebase();  // Initializing firebase connection
                writeRegistration(); // registering user.
            }
        });


    }

    /**
     * Validates user input, creates a User object, and saves it to Firebase.
     * Displays toast messages for successful or failed registrations.
     */
    private void writeRegistration(){

        firstName = findViewById(R.id.editTextText);
        lastName = findViewById(R.id.editTextText2);
        email = findViewById(R.id.editTextText3);
        password = findViewById(R.id.editTextText4);
        confirmPassword = findViewById(R.id.editTextText5);

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
        dbref.push().setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                redirectingToLogin();
            } else {
                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    /**
     * Connects to Firebase Database and initializes the database reference.
     */
    private void connectFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        dbref = firebaseDatabase.getReference("User details");
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
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
    }
}