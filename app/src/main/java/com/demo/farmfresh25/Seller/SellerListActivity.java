package com.demo.farmfresh25.Seller;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SellerListActivity extends AppCompatActivity {
    private RecyclerView rvSellers;
    private SearchView searchView;
    private SellerAdapter adapter;
    private List<Seller> sellerList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);

        db = FirebaseFirestore.getInstance();
        sellerList = new ArrayList<>();
        adapter = new SellerAdapter(this, sellerList);

        initViews();
        loadSellers();
        setupSearch();
    }

    private void initViews() {
        rvSellers = findViewById(R.id.rvSellers);
        searchView = findViewById(R.id.searchView);

        rvSellers.setLayoutManager(new LinearLayoutManager(this));
        rvSellers.setAdapter(adapter);
    }

    private void loadSellers() {
        db.collection("sellers")
                .orderBy("createdAt")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    sellerList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Seller seller = doc.toObject(Seller.class);
                        sellerList.add(seller);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load sellers: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSellers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSellers(newText);
                return true;
            }
        });
    }

    private void searchSellers(String query) {
        if (query.isEmpty()) {
            loadSellers();
            return;
        }

        db.collection("sellers")
                .whereEqualTo("name", query)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    sellerList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Seller seller = doc.toObject(Seller.class);
                        sellerList.add(seller);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Search failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
