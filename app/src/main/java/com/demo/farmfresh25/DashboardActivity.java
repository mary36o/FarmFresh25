package com.demo.farmfresh25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcomeLabel, tvWelcomeName, tvWelcomeEmail;
    private TextView tvTotalOrders, tvTotalSpent;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();

        tvWelcomeLabel = findViewById(R.id.tvWelcomeLabel);
        tvWelcomeName = findViewById(R.id.tvWelcomeName);
        tvWelcomeEmail = findViewById(R.id.tvWelcomeEmail);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);

        loadWelcomeMessage();
        loadOrderStats();

        CardView cardBrowseProducts = findViewById(R.id.cardBrowseProducts);
        CardView cardOrderHistory = findViewById(R.id.cardOrderHistory);
        CardView cardProfile = findViewById(R.id.cardProfile);

        cardBrowseProducts.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, Home.class);
            startActivity(intent);
        });

        cardOrderHistory.setOnClickListener(v -> {
            Toast.makeText(this, "Order history coming soon", Toast.LENGTH_SHORT).show();
        });

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, Home.class);
            startActivity(intent);
        });
    }

    private void loadWelcomeMessage() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String name = null;
                    if (documentSnapshot.exists()) {
                        name = documentSnapshot.getString("name");
                    }

                    if (name == null || name.isEmpty()) {
                        name = user.getDisplayName();
                    }
                    if (name == null || name.isEmpty()) {
                        name = user.getEmail();
                    }
                    if (name == null || name.isEmpty()) {
                        name = "User";
                    }

                    tvWelcomeLabel.setText("Welcome back,");
                    tvWelcomeName.setText(name + "!");
                    tvWelcomeEmail.setText(user.getEmail());
                })
                .addOnFailureListener(e -> {
                    String name = user.getDisplayName();
                    if (name == null || name.isEmpty()) {
                        name = user.getEmail();
                    }
                    if (name == null || name.isEmpty()) {
                        name = "User";
                    }

                    tvWelcomeLabel.setText("Welcome back,");
                    tvWelcomeName.setText(name + "!");
                    tvWelcomeEmail.setText(user.getEmail());
                });
    }

    private void loadOrderStats() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        db.collection("orders")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalOrders = queryDocumentSnapshots.size();
                    double totalSpent = 0;

                    for (var doc : queryDocumentSnapshots) {
                        Double price = doc.getDouble("totalPrice");
                        if (price != null) {
                            totalSpent += price;
                        }
                    }

                    tvTotalOrders.setText(String.valueOf(totalOrders));
                    tvTotalSpent.setText("GH\u20B5 " + String.format("%.2f", totalSpent));
                })
                .addOnFailureListener(e -> {
                    tvTotalOrders.setText("0");
                    tvTotalSpent.setText("GH\u20B5 0.00");
                });
    }
}
