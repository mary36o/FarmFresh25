package com.demo.farmfresh25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.demo.farmfresh25.Authentification.Login;
import com.demo.farmfresh25.DashBoard.DashboardFragment;
import com.demo.farmfresh25.ProductItem.ProductItem;
import com.demo.farmfresh25.Profile.ProfileFragment;
import com.demo.farmfresh25.databinding.ActivityHomeBinding;
import com.demo.farmfresh25.ui.checkout.CheckoutFragment;
import com.demo.farmfresh25.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHomeBinding binding;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        toolbar = binding.appBarHome.toolbar;
        setSupportActionBar(toolbar);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            showLogoutConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);

        } else if (id == R.id.nav_checkout) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new CheckoutFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_checkout);

        } else if (id == R.id.nav_dashboard) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new DashboardFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);

        } else if (id == R.id.nav_productitem) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new ProductItem())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_productitem);

        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new
                            ProfileFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_profile);

//        } else if (id == R.id.nav_item) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainerView, new ItemFragment())
//                    .commit();
//            navigationView.setCheckedItem(R.id.nav_item);

        } else if (id == R.id.nav_logout) {
            // Handle logout from navigation drawer
            showLogoutConfirmationDialog();
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Show logout confirmation dialog
     */
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Perform logout operation
     */
    private void performLogout() {
        // Sign out from Firebase
        if (mAuth != null) {
            mAuth.signOut();
        }

        // Clear any saved preferences (if you have any)
        // SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // preferences.edit().clear().apply();

        // Show logout message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to Login Activity
        Intent intent = new Intent(Home.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}