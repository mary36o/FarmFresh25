package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.farmfresh25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity2 extends AppCompatActivity {

    // Views
    private EditText fullNameEditText, emailEditText,
            passwordEditText, confirmPasswordEditText,
            businessNameEditText, businessAddressEditText;
    private MaterialButton registerButton;
    private TextView loginLink;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        initializeViews();

        // Register button click
        registerButton.setOnClickListener(v -> registerSeller());

        // Login link click - Navigate to LoginActivity4
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity2.this, LoginActivity4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        businessNameEditText = findViewById(R.id.businessNameEditText);
        businessAddressEditText = findViewById(R.id.businessAddressEditText);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        loginLink = findViewById(R.id.loginLink);
    }

    private void registerSeller() {
        // Get input values
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String businessName = businessNameEditText.getText().toString().trim();
        String businessAddress = businessAddressEditText.getText().toString().trim();

        // Validate Personal Information
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Enter your full name");
            fullNameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Enter email address");
            emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Enter password");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm your password");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // Validate Business Information
        if (TextUtils.isEmpty(businessName)) {
            businessNameEditText.setError("Enter your business name");
            businessNameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(businessAddress)) {
            businessAddressEditText.setError("Enter your business address");
            businessAddressEditText.requestFocus();
            return;
        }

        // Show Progress
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        // Create user with Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                saveSellerData(user.getUid(), fullName, email,
                                        businessName, businessAddress);
                            }
                        } else {
                            String errorMessage = task.getException() != null ?
                                    task.getException().getMessage() : "Registration failed";
                            Toast.makeText(RegisterActivity2.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveSellerData(String userId, String fullName, String email,
                                String businessName, String businessAddress) {

        Map<String, Object> sellerData = new HashMap<>();
        sellerData.put("id", userId);
        sellerData.put("userId", userId);
        sellerData.put("name", fullName);
        sellerData.put("fullName", fullName);
        sellerData.put("email", email);
        sellerData.put("storeName", businessName);
        sellerData.put("businessName", businessName);
        sellerData.put("address", businessAddress);
        sellerData.put("businessAddress", businessAddress);
        sellerData.put("phone", "");
        sellerData.put("imageUrl", "");
        sellerData.put("role", "seller");
        sellerData.put("isVerified", false);
        sellerData.put("createdAt", System.currentTimeMillis());

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", fullName);
        userData.put("email", email);
        userData.put("role", "seller");

        db.collection("sellers")
                .document(userId)
                .set(sellerData)
                .addOnSuccessListener(aVoid ->
                        db.collection("users")
                                .document(userId)
                                .set(userData))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity2.this,
                            "Registration successful! Please login.", Toast.LENGTH_LONG).show();

                    // Navigate to LoginActivity4
                    Intent intent = new Intent(RegisterActivity2.this, LoginActivity4.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity2.this,
                            "Failed to save seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}