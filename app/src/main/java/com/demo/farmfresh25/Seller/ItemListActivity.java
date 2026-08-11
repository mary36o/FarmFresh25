package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView rvItems;
    private ProgressBar progressBar;
    private LinearLayout llEmptyState;
    private TextView tvEmptyText;
    private Button btnAddProduct, btnAddFirstProduct;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ItemAdapter adapter;
    private List<Item> itemList;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        itemList = new ArrayList<>();
        initViews();
        setupRecyclerView();
        setupListeners();
        loadItems();
    }

    private void initViews() {
        rvItems = findViewById(R.id.rvItems);
        progressBar = findViewById(R.id.progressBar);
        llEmptyState = findViewById(R.id.llEmptyState);
        tvEmptyText = findViewById(R.id.tvEmptyText);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddFirstProduct = findViewById(R.id.btnAddFirstProduct);
    }

    private void setupRecyclerView() {
        adapter = new ItemAdapter(this, itemList);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ItemListActivity.this, CreateItemActivity.class);
            startActivity(intent);
        });

        btnAddFirstProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ItemListActivity.this, CreateItemActivity.class);
            startActivity(intent);
        });
    }

    private void loadItems() {
        progressBar.setVisibility(View.VISIBLE);
        rvItems.setVisibility(View.GONE);
        llEmptyState.setVisibility(View.GONE);

        db.collection("items")
                .whereEqualTo("sellerId", sellerId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);

                    if (error != null) {
                        Toast.makeText(this, "Error loading items: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    itemList.clear();

                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            Item item = doc.toObject(Item.class);
                            if (item != null) {
                                item.setId(doc.getId());
                                itemList.add(item);
                            }
                        }
                        rvItems.setVisibility(View.VISIBLE);
                        llEmptyState.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        rvItems.setVisibility(View.GONE);
                        llEmptyState.setVisibility(View.VISIBLE);
                        tvEmptyText.setText("You haven't added any products yet");
                    }
                });
    }
}