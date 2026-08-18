package com.demo.farmfresh25.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.demo.farmfresh25.Authentification.Login;
import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FarmFreshSettings";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";
    private static final String KEY_LANGUAGE = "selected_language";

    private Toolbar toolbar;
    private Switch switchNotifications, switchDarkMode;
    private CardView cardLanguage, cardPrivacy, cardAbout;
    private Button btnClearCache, btnDeleteAccount;
    private TextView languageCurrentValue;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean darkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
                darkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();
        setupToolbar();
        loadSavedSettings();
        setupClickListeners();
        setupBackNavigation();
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
        languageCurrentValue = findViewById(R.id.languageCurrentValue);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
    }

    private void loadSavedSettings() {
        boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true);
        boolean darkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false);
        String savedLanguage = prefs.getString(KEY_LANGUAGE, "English");

        switchNotifications.setChecked(notificationsEnabled);
        switchDarkMode.setChecked(darkModeEnabled);
        if (languageCurrentValue != null) {
            languageCurrentValue.setText(savedLanguage);
        }
    }

    private void setupClickListeners() {
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Notifications enabled" : "Notifications disabled",
                    Toast.LENGTH_SHORT).show();
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        cardLanguage.setOnClickListener(v -> showLanguageDialog());

        cardPrivacy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://farmfresh25.com/privacy-policy"));
            try {
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(this, "No browser found", Toast.LENGTH_SHORT).show();
            }
        });

        cardAbout.setOnClickListener(v -> showAboutDialog());

        btnClearCache.setOnClickListener(v -> clearAppCache());

        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "French", "Spanish", "Arabic"};
        String currentLanguage = prefs.getString(KEY_LANGUAGE, "English");
        int selectedIndex = 0;
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(currentLanguage)) {
                selectedIndex = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setSingleChoiceItems(languages, selectedIndex, (dialog, which) -> {
                    prefs.edit().putString(KEY_LANGUAGE, languages[which]).apply();
                    if (languageCurrentValue != null) {
                        languageCurrentValue.setText(languages[which]);
                    }
                    Toast.makeText(this, "Language set to " + languages[which], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About FarmFresh25")
                .setMessage("Version: 1.1\n\n" +
                        "FarmFresh25 is your trusted source for fresh organic produce.\n\n" +
                        "\u00a9 2025 FarmFresh25. All rights reserved.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void clearAppCache() {
        try {
            File cacheDir = getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteDir(cacheDir);
            }
            Toast.makeText(this, "Cache cleared successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to clear cache", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir != null && dir.delete();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone!")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseAuth.getInstance().getCurrentUser().delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
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
        finish();
        return true;
    }
}
