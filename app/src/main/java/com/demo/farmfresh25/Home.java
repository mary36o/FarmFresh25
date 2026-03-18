package com.demo.farmfresh25;

import static androidx.fragment.app.FragmentManagerKt.commit;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.demo.farmfresh25.Authentification.Login;
import com.demo.farmfresh25.DashBoard.DashBoard;
import com.demo.farmfresh25.ProductItem.ProductItem;
import com.demo.farmfresh25.ShoppingCart.ShoppingCart;
import com.demo.farmfresh25.databinding.ActivityHomeBinding;

import com.demo.farmfresh25.ui.gallery.GalleryFragment;
import com.demo.farmfresh25.ui.home.HomeFragment;
import com.demo.farmfresh25.ui.slideshow.SlideshowFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.logout == item.getItemId()) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();

        } else if (R.id.nav_gallery == item.getItemId()) {

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (R.id.nav_home == item.getItemId()) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();


        } else if (R.id.nav_gallery == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new GalleryFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_gallery);
//            Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();



        }else if (R.id.nav_dashboard == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new DashBoard()).commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);
//            Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();

        }else if (R.id.nav_productitem == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ProductItem ()).commit();
            navigationView.setCheckedItem(R.id.nav_productitem);
//            Toast.makeText(this, "Product Items", Toast.LENGTH_SHORT).show();


        }else if (R.id.nav_shoppingcart == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ShoppingCart()).commit();
            navigationView.setCheckedItem(R.id.nav_shoppingcart);
//            Toast.makeText(this, "Shopping Cart", Toast.LENGTH_SHORT).show();



        }
//        else if (R.id.nav_productitem == item.getItemId()) {
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(this, LoginEsp32.class);
//            startActivity(intent);


//        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
