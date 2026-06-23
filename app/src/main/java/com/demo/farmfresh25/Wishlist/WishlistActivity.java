package com.demo.farmfresh25.Wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<WishlistItem> wishlistItems;
    private WishlistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        initializeViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Wishlist");
        }

        // Setup RecyclerView
        wishlistItems = new ArrayList<>();
        adapter = new WishlistAdapter(wishlistItems);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Load wishlist
        loadWishlist();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        emptyText = findViewById(R.id.emptyText);
    }

    private void loadWishlist() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Please login to view wishlist", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("wishlist")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    wishlistItems.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        emptyText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        return;
                    }

                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        WishlistItem item = new WishlistItem(
                                doc.getId(),
                                doc.getString("productId"),
                                doc.getString("name"),
                                doc.getString("price"),
                                doc.getString("image"),
                                doc.getString("description")
                        );
                        wishlistItems.add(item);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading wishlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Wishlist Item Model
    public static class WishlistItem {
        private String id, productId, name, price, image, description;

        public WishlistItem(String id, String productId, String name, String price, String image, String description) {
            this.id = id;
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.image = image;
            this.description = description;
        }

        public String getId() { return id; }
        public String getProductId() { return productId; }
        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getImage() { return image; }
        public String getDescription() { return description; }
    }

    // Wishlist Adapter
    public static class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

        private ArrayList<WishlistItem> items;

        public WishlistAdapter(ArrayList<WishlistItem> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_wishlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WishlistItem item = items.get(position);

            holder.productName.setText(item.getName());
            holder.productPrice.setText(String.format("GHS %s", item.getPrice()));

            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.productImage);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView productImage;
            TextView productName, productPrice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.productImage);
                productName = itemView.findViewById(R.id.productName);
                productPrice = itemView.findViewById(R.id.productPrice);
            }
        }
    }
}