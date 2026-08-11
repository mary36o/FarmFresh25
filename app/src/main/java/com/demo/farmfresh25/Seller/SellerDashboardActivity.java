package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SellerDashboardActivity extends AppCompatActivity {

    private TextView tvSellerName, tvSellerEmail, tvTotalProducts, tvTotalOrders, tvPendingOrders;
    private CardView cardAddProduct, cardMyProducts, cardOrders, cardProfile;
    private Button btnLogout;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        loadSellerData();
        loadStatistics();
    }

    private void initViews() {
        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);

        cardAddProduct = findViewById(R.id.cardAddProduct);
        cardMyProducts = findViewById(R.id.cardMyProducts);
        cardOrders = findViewById(R.id.cardOrders);
        cardProfile = findViewById(R.id.cardProfile);

        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        // Add Product - Goes to CreateItemActivity
        cardAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, CreateItemActivity.class);
            startActivity(intent);
        });

        // My Products - Goes to ItemListActivity (NOT CreateItemActivity)
        cardMyProducts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, ItemListActivity.class);
            startActivity(intent);
        });

        // Orders - Goes to Order2Activity
        cardOrders.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, Order2Activity.class);
            startActivity(intent);
        });

        // Profile
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, SellerProfileActivity.class);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SellerDashboardActivity.this, LoginActivity4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadSellerData() {
        db.collection("users").document(sellerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");

                        if (name != null) {
                            tvSellerName.setText("Welcome, " + name + "!");
                        }
                        if (email != null) {
                            tvSellerEmail.setText(email);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadStatistics() {
        // Load total products
        db.collection("items")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    tvTotalProducts.setText(String.valueOf(count));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading products count", Toast.LENGTH_SHORT).show();
                });

        // Load total orders and pending orders
        db.collection("orders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int total = queryDocumentSnapshots.size();
                    int pending = 0;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String status = doc.getString("status");
                        if (status != null && status.equals("Pending")) {
                            pending++;
                        }
                    }

                    tvTotalOrders.setText(String.valueOf(total));
                    tvPendingOrders.setText(String.valueOf(pending));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading orders count", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when returning to dashboard
        loadStatistics();
    }
}