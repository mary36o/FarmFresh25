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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etName, etDescription, etPrice, etCategory, etQuantity;
    private ImageView ivImage;
    private Button btnSelectImage, btnUpdateItem, btnDeleteItem;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;
    private String productId;
    private String sellerId;
    private String currentImageUrl;
    private Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("product_images");

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get product ID from intent
        productId = getIntent().getStringExtra("productId");
        if (productId == null || productId.isEmpty()) {
            Toast.makeText(this, "Product ID required", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        loadProductData();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etQuantity = findViewById(R.id.etQuantity);
        ivImage = findViewById(R.id.ivImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUpdateItem = findViewById(R.id.btnUpdateItem);
        btnDeleteItem = findViewById(R.id.btnDeleteItem);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnSelectImage.setOnClickListener(v -> openFileChooser());
        btnUpdateItem.setOnClickListener(v -> updateItem());
        btnDeleteItem.setOnClickListener(v -> deleteItem());
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

    private void loadProductData() {
        progressBar.setVisibility(View.VISIBLE);
        btnUpdateItem.setEnabled(false);

        db.collection("items").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdateItem.setEnabled(true);

                    if (documentSnapshot.exists()) {
                        currentItem = documentSnapshot.toObject(Item.class);
                        if (currentItem != null) {
                            currentItem.setId(productId);

                            etName.setText(currentItem.getName());
                            etDescription.setText(currentItem.getDescription());
                            etPrice.setText(String.valueOf(currentItem.getPrice()));
                            etCategory.setText(currentItem.getCategory());
                            etQuantity.setText(String.valueOf(currentItem.getQuantity()));

                            currentImageUrl = currentItem.getImageUrl();
                            if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                                Glide.with(this)
                                        .load(currentImageUrl)
                                        .placeholder(R.drawable.ic_placeholder)
                                        .error(R.drawable.ic_error)
                                        .into(ivImage);
                            } else {
                                ivImage.setImageResource(R.drawable.ic_placeholder);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdateItem.setEnabled(true);
                    Toast.makeText(this, "Error loading product: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateItem() {
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
            btnUpdateItem.setEnabled(false);

            if (imageUri != null) {
                uploadImageAndUpdateItem(name, description, price, category, quantity);
            } else {
                updateItemInFirestore(name, description, price, category, quantity, currentImageUrl);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageAndUpdateItem(String name, String description, double price,
                                          String category, int quantity) {
        String fileName = UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateItemInFirestore(name, description, price, category, quantity, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdateItem.setEnabled(true);
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateItemInFirestore(String name, String description, double price,
                                       String category, int quantity, String imageUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("description", description);
        updates.put("price", price);
        updates.put("category", category);
        updates.put("quantity", quantity);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            updates.put("imageUrl", imageUrl);
        }

        updates.put("timestamp", System.currentTimeMillis());

        db.collection("items").document(productId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdateItem.setEnabled(true);
                    Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditProductActivity.this, ItemListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdateItem.setEnabled(true);
                    Toast.makeText(this, "Failed to update product: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteItem() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete \"" +
                        (currentItem != null ? currentItem.getName() : "this product") + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deleteItemFromFirestore())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteItemFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);
        btnUpdateItem.setEnabled(false);

        db.collection("items").document(productId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditProductActivity.this, ItemListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdateItem.setEnabled(true);
                    Toast.makeText(this, "Error deleting product: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}