package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class RegisterActivity extends AppCompatActivity {

    // Views
    private EditText fullNameEditText, emailEditText,
            passwordEditText, confirmPasswordEditText,
            businessNameEditText, businessAddressEditText;
    private MaterialButton registerButton;
    private TextView loginLink, termsText;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private boolean termsAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        initializeViews();

        // Setup terms and conditions
        setupTermsAndConditions();

        // Register button click
        registerButton.setOnClickListener(v -> registerSeller());

        // Login link click
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity4.class);
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
        termsText = findViewById(R.id.termsText);
    }

    private void setupTermsAndConditions() {
        String fullText = "By signing up, you agree to our Terms & Conditions and Privacy Policy";

        SpannableString spannableString = new SpannableString(fullText);

        // Make "Terms & Conditions" clickable
        String termsTextStr = "Terms & Conditions";
        int startTerms = fullText.indexOf(termsTextStr);
        int endTerms = startTerms + termsTextStr.length();

        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showTermsDialog();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.primary_green));
                ds.setUnderlineText(true);
            }
        };

        spannableString.setSpan(termsClickableSpan, startTerms, endTerms, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make "Privacy Policy" clickable
        String privacyTextStr = "Privacy Policy";
        int startPrivacy = fullText.indexOf(privacyTextStr);
        int endPrivacy = startPrivacy + privacyTextStr.length();

        ClickableSpan privacyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showPrivacyDialog();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.primary_green));
                ds.setUnderlineText(true);
            }
        };

        spannableString.setSpan(privacyClickableSpan, startPrivacy, endPrivacy, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsText.setText(spannableString);
        termsText.setMovementMethod(LinkMovementMethod.getInstance());

        // Click on the whole terms text to toggle acceptance
        termsText.setOnClickListener(v -> {
            termsAccepted = !termsAccepted;
            updateTermsVisual();
        });
    }

    private void updateTermsVisual() {
        if (termsAccepted) {
            termsText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle, 0, 0, 0);
            termsText.setTextColor(getColor(R.color.primary_green));
        } else {
            termsText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
            termsText.setTextColor(getColor(R.color.secondary_text));
        }
    }

    private void showTermsDialog() {
        String terms = "TERMS AND CONDITIONS\n\n" +
                "1. Acceptance of Terms\n" +
                "By registering as a seller on FarmFresh25, you agree to comply with these terms and conditions.\n\n" +
                "2. Seller Obligations\n" +
                "- You must provide accurate and complete information about your products.\n" +
                "- All products must be fresh, authentic, and of high quality.\n" +
                "- You are responsible for product pricing and inventory management.\n\n" +
                "3. Product Listings\n" +
                "- All product listings must include clear images and accurate descriptions.\n" +
                "- You must specify the quantity and price for each product.\n" +
                "- Organic products must be clearly labeled.\n\n" +
                "4. Order Processing\n" +
                "- You must process orders within 24 hours of confirmation.\n" +
                "- You are responsible for packaging and delivery quality.\n\n" +
                "5. Payment Terms\n" +
                "- Payments are processed through FarmFresh25's secure payment system.\n" +
                "- A commission fee of 10% applies to all sales.\n\n" +
                "6. Termination\n" +
                "FarmFresh25 reserves the right to terminate seller accounts that violate these terms.\n\n" +
                "7. Liability\n" +
                "FarmFresh25 is not liable for any damages arising from product quality issues.\n\n" +
                "By continuing, you agree to all terms and conditions.";

        new AlertDialog.Builder(this)
                .setTitle("Terms & Conditions")
                .setMessage(terms)
                .setPositiveButton("Accept", (dialog, which) -> {
                    termsAccepted = true;
                    updateTermsVisual();
                    Toast.makeText(this, "Terms accepted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Decline", (dialog, which) -> {
                    termsAccepted = false;
                    updateTermsVisual();
                })
                .setCancelable(false)
                .show();
    }

    private void showPrivacyDialog() {
        String privacy = "PRIVACY POLICY\n\n" +
                "1. Information We Collect\n" +
                "- Name, email address, phone number\n" +
                "- Business name and address\n" +
                "- Payment information\n" +
                "- Product listings and sales data\n\n" +
                "2. How We Use Your Information\n" +
                "- To create and manage your seller account\n" +
                "- To process orders and payments\n" +
                "- To communicate with you about orders\n" +
                "- To improve our services\n\n" +
                "3. Data Security\n" +
                "We implement industry-standard security measures to protect your data.\n\n" +
                "4. Data Sharing\n" +
                "We do not sell your personal information to third parties.\n\n" +
                "5. Your Rights\n" +
                "You have the right to access, modify, or delete your personal data.\n\n" +
                "6. Contact\n" +
                "For privacy concerns, contact us at privacy@farmfresh25.com";

        new AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage(privacy)
                .setPositiveButton("OK", null)
                .show();
    }

    private void registerSeller() {
        // Check if terms are accepted
        if (!termsAccepted) {
            Toast.makeText(this, "Please accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

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
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                saveSellerData(user.getUid(), fullName, email, password,
                                        businessName, businessAddress);
                            }
                        } else {
                            String errorMessage = task.getException() != null ?
                                    task.getException().getMessage() : "Registration failed";
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveSellerData(String userId, String fullName, String email, String password,
                                String businessName, String businessAddress) {

        Map<String, Object> sellerData = new HashMap<>();
        sellerData.put("userId", userId);
        sellerData.put("fullName", fullName);
        sellerData.put("email", email);
        sellerData.put("businessName", businessName);
        sellerData.put("businessAddress", businessAddress);
        sellerData.put("role", "seller");
        sellerData.put("termsAccepted", true);
        sellerData.put("isVerified", false);
        sellerData.put("createdAt", System.currentTimeMillis());

        db.collection("sellers")
                .document(userId)
                .set(sellerData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this,
                            "Registration successful! Please login.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity4.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this,
                            "Failed to save seller data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}