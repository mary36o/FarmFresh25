package com.demo.farmfresh25;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.farmfresh25.Seller.LoginActivity4;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivitySeller extends AppCompatActivity {

    private EditText edtEmail;
    private MaterialButton btnResetPassword;
    private ProgressBar progressBar;
    private TextView tvBackToLogin;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        initializeViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Forgot Password");
        }

        // Reset Password button click
        if (btnResetPassword != null) {
            btnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPassword();
                }
            });
        }

        // Back to Login click
        if (tvBackToLogin != null) {
            tvBackToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ForgotPasswordActivitySeller.this, LoginActivity4.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        edtEmail = findViewById(R.id.edtEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        progressBar = findViewById(R.id.progressBar);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    private void resetPassword() {
        String email = edtEmail.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter your email address");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid email address");
            edtEmail.requestFocus();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnResetPassword.setEnabled(false);

        // Send reset password email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnResetPassword.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivitySeller.this,
                                "Password reset email sent! Please check your inbox.",
                                Toast.LENGTH_LONG).show();

                        new android.os.Handler().postDelayed(() -> {
                            Intent intent = new Intent(ForgotPasswordActivitySeller.this, LoginActivity4.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }, 3000);
                    } else {
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Failed to send reset email.";
                        Toast.makeText(ForgotPasswordActivitySeller.this,
                                "Error: " + errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}