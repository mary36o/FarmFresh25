package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateItemActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etName, etDescription, etPrice, etCategory, etQuantity;
    private ImageView ivImage;
    private Button btnSelectImage, btnCreateItem;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Uri imageUri;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get seller ID
        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        // Make sure these IDs match your XML
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etQuantity = findViewById(R.id.etQuantity);
        ivImage = findViewById(R.id.ivImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnCreateItem = findViewById(R.id.btnCreateItem);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        // Check if buttons are null before setting listeners
        if (btnSelectImage != null) {
            btnSelectImage.setOnClickListener(v -> openFileChooser());
        } else {
            Toast.makeText(this, "Error: btnSelectImage is null", Toast.LENGTH_SHORT).show();
        }

        if (btnCreateItem != null) {
            btnCreateItem.setOnClickListener(v -> createItem());
        } else {
            Toast.makeText(this, "Error: btnCreateItem is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(ivImage);
        }
    }

    private void createItem() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() ||
                category.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            progressBar.setVisibility(View.VISIBLE);
            btnCreateItem.setEnabled(false);

            createItemInFirestore(name, description, price, category, quantity);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnCreateItem.setEnabled(true);
        }
    }

    private void createItemInFirestore(String name, String description, double price,
                                       String category, int quantity) {
        String id = db.collection("items").document().getId();

        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("sellerId", sellerId);
        item.put("name", name);
        item.put("description", description);
        item.put("price", price);
        item.put("category", category);
        item.put("quantity", quantity);
        item.put("imageUrl", "");
        item.put("timestamp", System.currentTimeMillis());

        db.collection("items").document(id)
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnCreateItem.setEnabled(true);
                    Toast.makeText(this, "Item created successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateItemActivity.this, ItemListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnCreateItem.setEnabled(true);
                    Toast.makeText(this, "Failed to create item: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}