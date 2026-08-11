package com.demo.farmfresh25;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.R;
import com.demo.farmfresh25.OrdersActivity;
import com.demo.farmfresh25.Seller.OrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.admin.v1.Index;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private ProgressBar progressBar;
    private LinearLayout llEmptyState;
    private TextView tvEmptyText;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private OrderAdapter adapter;
    private List<Index.IndexField.Order> orderList;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        orderList = new ArrayList<Index.IndexField.Order>();
        initViews();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        rvOrders = findViewById(R.id.rvOrders);
        progressBar = findViewById(R.id.progressBar);
        llEmptyState = findViewById(R.id.llEmptyState);
        tvEmptyText = findViewById(R.id.tvEmptyText);
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter(this, orderList);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);
        rvOrders.setVisibility(View.GONE);
        llEmptyState.setVisibility(View.GONE);

        db.collection("orders")
                .whereEqualTo("sellerId", sellerId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);

                    if (error != null) {
                        Toast.makeText(this, "Error loading orders: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    orderList.clear();

                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            Index.IndexField.Order order = doc.toObject(Index.IndexField.Order.class);
                            if (order != null) {
                                order.setOrderId(doc.getId());
                                orderList.add(order);
                            }
                        }
                        rvOrders.setVisibility(View.VISIBLE);
                        llEmptyState.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        rvOrders.setVisibility(View.GONE);
                        llEmptyState.setVisibility(View.VISIBLE);
                        tvEmptyText.setText("No orders yet");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}