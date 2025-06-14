package com.example.group14_projectiteration1_quickcash.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group14_projectiteration1_quickcash.Firebase.firebaseHelper;
import com.example.group14_projectiteration1_quickcash.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Handles users Login process.
 * */
public class LogInActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailInput, passwordInput;
    private firebaseHelper firebaseHelper;

    private TextView signupPageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        initializeViews();
        firebaseHelper = new firebaseHelper();
        firebaseHelper.connectFirebase();

        loginButton.setOnClickListener(view -> attemptLogin());

        goToSignUpThroughLink(signupPageLink);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Initialize all Login page UI elements.
     * */
    private void initializeViews() {
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        signupPageLink = findViewById(R.id.signupPageLink);
    }

    /**
     * If user attempts Login.
     * */
    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!isValidEmail(email)) {
            emailInput.setError("Enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Enter your password");
            return;
        }

        checkCredentials(email, password);
    }

    /**
     * Validates user email.
     * @param email User's email
     * @return true if is valid otherwise false.
     * */
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Verify credentials given by the user.
     * @param email User's email
     * @param password User's password
     * */
    private void checkCredentials(String email, String password) {
        Query emailQuery = firebaseHelper.dbref.orderByChild("email").equalTo(email);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String storedPassword = userSnapshot.child("password").getValue(String.class);

                        if (password.equals(storedPassword)) {
                            onLoginSuccess(email);
                            return;
                        }
                    }
                    showToast("Invalid password");
                } else {
                    showToast("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showToast("Database error: " + error.getMessage());
            }
        });
    }

    /**
     * If login process is successful.
     * */
    private void onLoginSuccess(String email) {
        showToast("Login successful");
        Intent intent = new Intent(LogInActivity.this, DashboardActivity.class);
        intent.putExtra("userEmail", email);
        startActivity(intent);
        finish();
    }

    /**
     * Just to show toast.
     * */
    private void showToast(String message) {
        Toast.makeText(LogInActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Redirects user to Signup Page.*/
    private void redirectToSignup() {
        Intent intent = new Intent(LogInActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * If user clicks on signup page link on login page.
     * */
    private void goToSignUpThroughLink(TextView signupPageLink){
        signupPageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSignup();
            }
        });
    }
}
