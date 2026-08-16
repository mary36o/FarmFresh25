package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SellerSetupActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfile;
    private EditText etName, etStoreName, etEmail, etPhone, etAddress;
    private Button btnSelectImage, btnCreateSeller;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private Uri imageUri;
    private String imageUrl = "";
    private boolean isUploading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_setup);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        initViews();
        loadExistingData();

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnCreateSeller.setOnClickListener(v -> saveProfile());
    }

    private void initViews() {
        ivProfile = findViewById(R.id.ivProfile);
        etName = findViewById(R.id.etName);
        etStoreName = findViewById(R.id.etStoreName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnCreateSeller = findViewById(R.id.btnCreateSeller);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadExistingData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        db.collection("sellers").document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        String storeName = doc.getString("storeName");
                        String email = doc.getString("email");
                        String phone = doc.getString("phone");
                        String address = doc.getString("address");
                        imageUrl = doc.getString("imageUrl") != null ? doc.getString("imageUrl") : "";

                        if (name != null) etName.setText(name);
                        if (storeName != null) etStoreName.setText(storeName);
                        if (email != null) etEmail.setText(email);
                        if (phone != null) etPhone.setText(phone);
                        if (address != null) etAddress.setText(address);
                        if (!TextUtils.isEmpty(imageUrl)) {
                            Glide.with(this).load(imageUrl).into(ivProfile);
                        }
                    } else {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            etEmail.setText(currentUser.getEmail());
                        }
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivProfile.setImageURI(imageUri);
        }
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String storeName = etStoreName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter your name");
            etName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(storeName)) {
            etStoreName.setError("Enter store name");
            etStoreName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter phone number");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Enter address");
            etAddress.requestFocus();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        progressBar.setVisibility(View.VISIBLE);
        btnCreateSeller.setEnabled(false);

        if (imageUri != null) {
            uploadImageAndSave(user.getUid(), name, storeName, email, phone, address);
        } else {
            saveSellerData(user.getUid(), name, storeName, email, phone, address, imageUrl);
        }
    }

    private void uploadImageAndSave(String userId, String name, String storeName,
                                    String email, String phone, String address) {
        StorageReference fileRef = storage.getReference()
                .child("profile_images/" + userId + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String downloadedUrl = uri.toString();
                            saveSellerData(userId, name, storeName, email, phone, address, downloadedUrl);
                        }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnCreateSeller.setEnabled(true);
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveSellerData(String userId, String name, String storeName,
                                String email, String phone, String address, String imgUrl) {
        Map<String, Object> sellerData = new HashMap<>();
        sellerData.put("id", userId);
        sellerData.put("userId", userId);
        sellerData.put("name", name);
        sellerData.put("fullName", name);
        sellerData.put("email", email);
        sellerData.put("storeName", storeName);
        sellerData.put("businessName", storeName);
        sellerData.put("address", address);
        sellerData.put("businessAddress", address);
        sellerData.put("phone", phone);
        sellerData.put("imageUrl", imgUrl);
        sellerData.put("role", "seller");
        sellerData.put("isVerified", false);
        sellerData.put("profileComplete", true);
        sellerData.put("createdAt", System.currentTimeMillis());

        db.collection("sellers").document(userId)
                .set(sellerData)
                .addOnSuccessListener(aVoid -> {
                    db.collection("users").document(userId)
                            .update("name", name, "email", email)
                            .addOnSuccessListener(aVoid2 -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Profile created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerSetupActivity.this, SellerDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Profile saved. Redirecting...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerSetupActivity.this, SellerDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnCreateSeller.setEnabled(true);
                    Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
