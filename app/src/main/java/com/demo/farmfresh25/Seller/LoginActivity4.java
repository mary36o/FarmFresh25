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

public class LoginActivity4 extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private MaterialButton loginButton, registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login4);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity4.this, SellerDashboardActivity.class));
            finish();
            return;
        }

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        // Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);

                // Authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity4.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                loginButton.setEnabled(true);

                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    Toast.makeText(LoginActivity4.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity4.this, SellerDashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String errorMessage = task.getException() != null ?
                                            task.getException().getMessage() : "Authentication failed.";
                                    Toast.makeText(LoginActivity4.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Register button click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity4.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });

        // Forgot Password click
        TextView forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        if (forgotPasswordLink != null) {
            forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity4.this, ForgotPasswordActivitySeller.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to previous activities
        moveTaskToBack(true);
    }
}