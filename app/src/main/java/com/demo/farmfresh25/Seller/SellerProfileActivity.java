package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SellerProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfileImage;
    private TextView tvSellerName, tvSellerEmail, tvFarmName, tvLocation, tvJoinDate, tvTotalSales, tvRating;
    private EditText etFarmName, etLocation, etPhone, etFarmDescription;
    private Button btnSaveProfile, btnChangeImage, btnLogout;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String sellerId;
    private Uri imageUri;
    private String currentImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("seller_profiles");

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        loadProfileData();
    }

    private void initViews() {
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);
        tvFarmName = findViewById(R.id.tvFarmName);
        tvLocation = findViewById(R.id.tvLocation);
        tvJoinDate = findViewById(R.id.tvJoinDate);
        tvTotalSales = findViewById(R.id.tvTotalSales);
        tvRating = findViewById(R.id.tvRating);

        etFarmName = findViewById(R.id.etFarmName);
        etLocation = findViewById(R.id.etLocation);
        etPhone = findViewById(R.id.etPhone);
        etFarmDescription = findViewById(R.id.etFarmDescription);

        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnChangeImage.setOnClickListener(v -> openFileChooser());

        btnSaveProfile.setOnClickListener(v -> saveProfileData());

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SellerProfileActivity.this, LoginActivity4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
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
            Glide.with(this).load(imageUri).into(ivProfileImage);
        }
    }

    private void loadProfileData() {
        progressBar.setVisibility(View.VISIBLE);

        // Get user data from Firestore
        db.collection("users").document(sellerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);

                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String farmName = documentSnapshot.getString("farmName");
                        String location = documentSnapshot.getString("farmLocation");
                        String phone = documentSnapshot.getString("phone");
                        String description = documentSnapshot.getString("farmDescription");
                        String profileImage = documentSnapshot.getString("profileImage");
                        String joinDate = documentSnapshot.getString("joinDate");
                        Long totalSales = documentSnapshot.getLong("totalSales");
                        Double rating = documentSnapshot.getDouble("rating");

                        // Set display fields
                        tvSellerName.setText(name != null ? name : "Farmer");
                        tvSellerEmail.setText(email != null ? email : "");
                        tvFarmName.setText(farmName != null ? farmName : "Farm Name");
                        tvLocation.setText(location != null ? location : "Location not set");
                        tvJoinDate.setText(joinDate != null ? "Joined: " + joinDate : "");
                        tvTotalSales.setText(totalSales != null ? String.valueOf(totalSales) : "0");
                        tvRating.setText(rating != null ? String.format("%.1f ★", rating) : "0.0 ★");

                        // Set edit fields
                        etFarmName.setText(farmName != null ? farmName : "");
                        etLocation.setText(location != null ? location : "");
                        etPhone.setText(phone != null ? phone : "");
                        etFarmDescription.setText(description != null ? description : "");

                        // Load profile image
                        if (profileImage != null && !profileImage.isEmpty()) {
                            currentImageUrl = profileImage;
                            Glide.with(this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.ic_profile_placeholder)
                                    .error(R.drawable.ic_profile_placeholder)
                                    .into(ivProfileImage);
                        } else {
                            ivProfileImage.setImageResource(R.drawable.ic_profile_placeholder);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error loading profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfileData() {
        String farmName = etFarmName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String description = etFarmDescription.getText().toString().trim();

        if (farmName.isEmpty()) {
            Toast.makeText(this, "Farm name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSaveProfile.setEnabled(false);

        // If there's a new image, upload it first
        if (imageUri != null) {
            uploadProfileImage(farmName, location, phone, description);
        } else {
            updateProfileInFirestore(farmName, location, phone, description, currentImageUrl);
        }
    }

    private void uploadProfileImage(String farmName, String location, String phone, String description) {
        String fileName = sellerId + "_profile.jpg";
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateProfileInFirestore(farmName, location, phone, description, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileInFirestore(String farmName, String location, String phone,
                                          String description, String imageUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("farmName", farmName);
        updates.put("farmLocation", location);
        updates.put("phone", phone);
        updates.put("farmDescription", description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            updates.put("profileImage", imageUrl);
        }

        db.collection("users").document(sellerId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    // Refresh the profile display
                    loadProfileData();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(this, "Error updating profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}