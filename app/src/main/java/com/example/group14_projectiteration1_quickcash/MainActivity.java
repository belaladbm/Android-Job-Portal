package com.example.group14_projectiteration1_quickcash;

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

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference dbref;

    private static final String FIREBASE_URL = "https://group-14-project-quickca-c43bd-default-rtdb.firebaseio.com";

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

        roleSpinner = findViewById(R.id.spinner);

        RoleSpinner();

        Button b = findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectFirebase();
                writeRegistration();
            }
        });


    }

    private void writeRegistration(){

        firstName = findViewById(R.id.editTextText);
        lastName = findViewById(R.id.editTextText2);
        email = findViewById(R.id.editTextText3);
        password = findViewById(R.id.editTextText4);
        confirmPassword = findViewById(R.id.editTextText5);

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("First name is required");
            return;
        }
        if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError("Last name is required");
            return;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Email is required");
            return;
        }
        if (!email.getText().toString().contains("@")) {
            email.setError("Enter a valid email");
            return;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is required");
            return;
        }
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            return;
        }

        User user = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                confirmPassword.getText().toString(),
                roleSpinner.getSelectedItem().toString()
        );


        dbref.push().setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void connectFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        dbref = firebaseDatabase.getReference("User details");
    }

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
}