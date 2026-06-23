package com.demo.farmfresh25.Settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Switch switchNotifications, switchDarkMode;
    private CardView cardLanguage, cardPrivacy, cardAbout;
    private Button btnClearCache, btnDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        initializeViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        switchNotifications = findViewById(R.id.switchNotifications);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        cardLanguage = findViewById(R.id.cardLanguage);
        cardPrivacy = findViewById(R.id.cardPrivacy);
        cardAbout = findViewById(R.id.cardAbout);
        btnClearCache = findViewById(R.id.btnClearCache);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
    }

    private void setupClickListeners() {
        // Notifications switch
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this,
                    isChecked ? "Notifications enabled" : "Notifications disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Dark mode switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this,
                    isChecked ? "Dark mode enabled" : "Dark mode disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Language
        cardLanguage.setOnClickListener(v -> {
            showLanguageDialog();
        });

        // Privacy Policy
        cardPrivacy.setOnClickListener(v -> {
            Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();
        });

        // About
        cardAbout.setOnClickListener(v -> {
            showAboutDialog();
        });

        // Clear Cache
        btnClearCache.setOnClickListener(v -> {
            Toast.makeText(this, "Cache cleared successfully!", Toast.LENGTH_SHORT).show();
        });

        // Delete Account
        btnDeleteAccount.setOnClickListener(v -> {
            showDeleteAccountDialog();
        });
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "French", "Spanish", "Arabic"};

        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    Toast.makeText(this, "Language: " + languages[which], Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About FarmFresh25")
                .setMessage("Version: 1.0.0\n\n" +
                        "FarmFresh25 is your trusted source for fresh organic produce.\n\n" +
                        "© 2024 FarmFresh25. All rights reserved.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone!")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseAuth.getInstance().getCurrentUser().delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}