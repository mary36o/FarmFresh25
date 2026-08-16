package com.demo.farmfresh25;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.demo.farmfresh25.Authentification.Login;
import com.demo.farmfresh25.DashboardActivity;
import com.demo.farmfresh25.Profile.ProfileFragment;
//import com.demo.farmfresh25.ShoppingCart.ShoppingCart;
import com.demo.farmfresh25.Seller.LoginActivity4;
import com.demo.farmfresh25.Settings.SettingsActivity;
import com.demo.farmfresh25.ShoppingCart.ShoppingCartActivity;
import com.demo.farmfresh25.databinding.ActivityHomeBinding;

import com.demo.farmfresh25.ui.checkout.CheckoutFragment ;
import com.demo.farmfresh25.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {


    ActivityHomeBinding binding;
    Toolbar toolbar;

    NavigationView navigationView;
    DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.appBarHome.toolbar;
        setSupportActionBar(toolbar);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem switchItem = menu.findItem(R.id.app_bar_switch);
        if (switchItem != null) {
            Switch darkModeSwitch = switchItem.getActionView().findViewById(R.id.switchDarkMode);
            if (darkModeSwitch != null) {
                darkModeSwitch.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    AppCompatDelegate.setDefaultNightMode(isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.logout == item.getItemId()) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();

        } else if (R.id.action_settings == item.getItemId()) {

            startActivity(new Intent(this, SettingsActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (R.id.nav_home == item.getItemId()) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();


        } else if (R.id.nav_dashboard == item.getItemId()) {
            startActivity(new Intent(Home.this, DashboardActivity.class));

        } else if (R.id.nav_checkout == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CheckoutFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_checkout);
        }

//            else if (item.getItemId() == R.id.nav_seller) {
//            startActivity(new Intent(Home.this, LoginActivity4.class));
//            finish();
//            return true;
//        }




        else if (R.id.logout == item.getItemId()) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();


//        }else if (R.id.nav_productitem == item.getItemId()) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ProductItem ()).commit();
//            navigationView.setCheckedItem(R.id.nav_productitem);



            setContentView(binding.getRoot());

            toolbar = binding.appBarHome.toolbar;
            setSupportActionBar(toolbar);
            drawer = binding.drawerLayout;
            navigationView = binding.navView;


//        }else if (R.id.nav_shoppingcart == item.getItemId()) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ShoppingCart()).commit();
//            navigationView.setCheckedItem(R.id.nav_shoppingcart);

        }else if (R.id.nav_profile == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
//
//        else if (R.id.nav_item== item.getItemId()) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ItemFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_item);
//
//
//        }


//        else if (R.id.nav_productitem == item.getItemId()) {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(this, LoginEsp32.class);
//            startActivity(intent);


//        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}