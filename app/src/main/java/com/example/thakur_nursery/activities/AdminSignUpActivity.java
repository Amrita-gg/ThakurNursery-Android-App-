package com.example.thakur_nursery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thakur_nursery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminSignUpActivity extends AppCompatActivity {

    private EditText id, full_name, email, password;
    private RadioGroup radioGroup;
    private RadioButton rbAdmin, rbUser;
    private Button btnRegister;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Check if a user is already signed in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // If user is signed in, redirect to MainActivity
            startActivity(new Intent(AdminSignUpActivity.this, MainActivity.class));
            finish();
        }

        // Initialize views
        id = findViewById(R.id.id);
        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        radioGroup = findViewById(R.id.radioGroup);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbUser = findViewById(R.id.rbUser);
        btnRegister = findViewById(R.id.signup_button);

        // Register button click listener
        btnRegister.setOnClickListener(v -> {
            String userId = id.getText().toString().trim();
            String userName = full_name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            // Validate user inputs
            if (TextUtils.isEmpty(userId)) {
                Toast.makeText(AdminSignUpActivity.this, "Enter your ID!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(AdminSignUpActivity.this, "Enter your name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(userEmail)) {
                Toast.makeText(AdminSignUpActivity.this, "Enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(AdminSignUpActivity.this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(userPassword)) {
                Toast.makeText(AdminSignUpActivity.this, "Enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userPassword.length() < 6) {
                Toast.makeText(AdminSignUpActivity.this, "Password is too short! Please enter at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine user role based on selected RadioButton
            int selectedRoleId = radioGroup.getCheckedRadioButtonId();
            String role = selectedRoleId == R.id.rbAdmin ? "Admin" : "User";

            // If the selected role is Admin, check if an Admin already exists
            if (role.equals("Admin")) {
                firestore.collection("users")
                        .whereEqualTo("role", "Admin")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    // If an Admin already exists, show a message and return
                                    Toast.makeText(AdminSignUpActivity.this, "An Admin already exists. Only one Admin is allowed.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If no Admin exists, proceed with registration
                                    registerUser(userId, userName, userEmail, userPassword, role);
                                }
                            } else {
                                Toast.makeText(AdminSignUpActivity.this, "Error checking admin: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // For normal users, proceed with registration
                registerUser(userId, userName, userEmail, userPassword, role);
            }
        });
    }

    private void registerUser(String userId, String userName, String userEmail, String userPassword, String role) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(AdminSignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                String userUid = user.getUid();

                                // Store user details and role in Firestore
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("id", userUid);
                                userMap.put("email", userEmail);
                                userMap.put("role", role);
                                userMap.put("fullName", userName);

                                firestore.collection("users").document(userUid).set(userMap)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(AdminSignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                            // Redirect to appropriate activity based on role
                                            if (role.equals("Admin")) {
                                                startActivity(new Intent(AdminSignUpActivity.this, AdminDashboardActivity.class));
                                            } else {
                                                startActivity(new Intent(AdminSignUpActivity.this, UserdashboardActivity.class));
                                            }
                                            finish();  // Close the sign-up activity
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(AdminSignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(AdminSignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signin(View view) {
        // Go to the login activity
        startActivity(new Intent(AdminSignUpActivity.this, LoginActivity.class));
    }
}
