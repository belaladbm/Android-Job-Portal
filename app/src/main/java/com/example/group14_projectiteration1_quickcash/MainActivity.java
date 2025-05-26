package com.example.group14_projectiteration1_quickcash;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference dbref;

    private Spinner roleSpinner;

    private EditText firstName;

    private EditText lastName;

    private EditText email;

    private EditText password;

    private EditText confirmPassword;

    private Button signUp;

    private static final String FIREBASE_URL = "https://group-14-project-quickca-c43bd-default-rtdb.firebaseio.com";

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