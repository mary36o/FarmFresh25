package com.demo.farmfresh25.Seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class CreateSellerActivity extends AppCompatActivity {

    private EditText etSellerName, etEmail, etPhone, etShopName, etShopAddress;
    private ImageView ivSellerImage;
    private Button btnSelectImage, btnSaveSeller;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seller);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("seller_profiles");

        initViews();
        setupListeners();
    }

    private void initViews() {
        etSellerName = findViewById(R.id.etName);
        etShopName = findViewById(R.id.etStoreName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etShopAddress = findViewById(R.id.etAddress);
        ivSellerImage = findViewById(R.id.ivProfile);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveSeller = findViewById(R.id.btnCreateSeller);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        });

        btnSaveSeller.setOnClickListener(v -> saveSeller());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.profile_placeholder)
                    .into(ivSellerImage);
        }
    }

    private void saveSeller() {
        String sellerName = etSellerName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String shopName = etShopName.getText().toString().trim();
        String shopAddress = etShopAddress.getText().toString().trim();

        if (sellerName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSaveSeller.setEnabled(false);

        if (imageUri != null) {
            uploadImageAndSaveSeller(sellerName, email, phone, shopName, shopAddress);
        } else {
            saveSellerToFirestore(sellerName, email, phone, shopName, shopAddress, "");
        }
    }

    private void uploadImageAndSaveSeller(String sellerName, String email, String phone,
                                          String shopName, String shopAddress) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        fileRef.getDownloadUrl().addOnSuccessListener(uri ->
                                saveSellerToFirestore(sellerName, email, phone, shopName, shopAddress, uri.toString())))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveSeller.setEnabled(true);
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveSellerToFirestore(String sellerName, String email, String phone,
                                       String shopName, String shopAddress, String imageUrl) {
        String sellerId = UUID.randomUUID().toString();
        Seller seller = new Seller(
                sellerId,
                sellerName,
                email,
                phone,
                imageUrl,
                shopName,
                shopAddress,
                System.currentTimeMillis()
        );

        firestore.collection("sellers")
                .document(sellerId)
                .set(seller)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveSeller.setEnabled(true);
                    Toast.makeText(this, "Seller Created Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateSellerActivity.this, SellerListActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSaveSeller.setEnabled(true);
                    Toast.makeText(this, "Failed to create seller: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
