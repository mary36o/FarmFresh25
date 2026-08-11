package com.demo.farmfresh25.Authentification;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.farmfresh25.Addproduct;
import com.demo.farmfresh25.ForgotPassword;
import com.demo.farmfresh25.Home;
import com.demo.farmfresh25.R;
//import com.demo.farmfresh25.Seller.LoginActivity;
import com.demo.farmfresh25.Seller.RegisterActivity2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtRegister, txtForgot;
    MaterialButton btnSellerLogin, btnSellerRegister;  // Added Seller buttons
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgot = findViewById(R.id.txtForgot);

        // Initialize Seller buttons
        btnSellerLogin = findViewById(R.id.btnSellerLogin);
        btnSellerRegister = findViewById(R.id.btnSellerRegister);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Disable button to prevent multiple clicks
                btnLogin.setEnabled(false);

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Re-enable button
                                btnLogin.setEnabled(true);

                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Check if email is verified
                                    if (user != null && user.isEmailVerified()) {
                                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                    } else {
                                        // Email not verified
                                        Toast.makeText(Login.this,
                                                "Please verify your email before logging in.",
                                                Toast.LENGTH_LONG).show();
                                        // Optional: Resend verification email
                                        if (user != null && !user.isEmailVerified()) {
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Toast.makeText(Login.this,
                                                                    "Verification email resent. Please check your inbox.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                } else {
                                    // Sign in failed
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    String errorMessage = "Authentication failed.";

                                    // Provide more specific error messages
                                    if (task.getException() != null) {
                                        String error = task.getException().getMessage();
                                        if (error != null) {
                                            if (error.contains("There is no user record")) {
                                                errorMessage = "No account found with this email.";
                                            } else if (error.contains("The password is invalid")) {
                                                errorMessage = "Incorrect password.";
                                            } else if (error.contains("too many requests")) {
                                                errorMessage = "Too many attempts. Please try again later.";
                                            } else {
                                                errorMessage = error;
                                            }
                                        }
                                    }
                                    Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(this, Register.class)));

        // Forgot Password Click
        txtForgot.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPassword.class);
            startActivity(intent);
        });

        // Seller Login Button Click - Navigate to Seller Login
        btnSellerLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Login.class);
            startActivity(intent);
        });

        // Seller Register Button Click - Navigate to Seller Register
        btnSellerRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RegisterActivity2.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            updateUI(currentUser);
        } else if (currentUser != null && !currentUser.isEmailVerified()) {
            // User exists but email not verified
            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
    }

    public void ForgotPass(View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    public void button(View view) {
        Intent intent = new Intent(Login.this, Addproduct.class);
        startActivity(intent);
    }
}