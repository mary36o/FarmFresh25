package com.demo.farmfresh25.Seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CreateSellerActivity extends AppCompatActivity {

    private EditText etSellerName, etEmail, etPhone, etShopName, etShopAddress;
    private ImageView ivSellerImage;
    private Button btnSelectImage, btnSaveSeller;
    private String imageUrl = "";
    private FirebaseFirestore firestore;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seller);

        firestore = FirebaseFirestore.getInstance();
        initViews();
        setupListeners();
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        etSellerName = findViewById(R.id.tvSellerName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etShopName = findViewById(R.id.tvShopName);
        etShopAddress = findViewById(R.id.etAddress);
        ivSellerImage = findViewById(R.id.ivSellerImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveSeller = findViewById(R.id.btnSaveSeller);
    }

    private void setupListeners() {
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        });

        btnSaveSeller.setOnClickListener(v -> {
            saveSellerToFirestore();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imageUrl = uri.toString();
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_seller_placeholder)
                    .into(ivSellerImage);
        }
    }

    private void saveSellerToFirestore() {
        String sellerName = etSellerName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String shopName = etShopName.getText().toString().trim();
        String shopAddress = etShopAddress.getText().toString().trim();

        if (sellerName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

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
                    Toast.makeText(this, "Seller Created Successfully!", Toast.LENGTH_SHORT).show();
                    //finish();
                    startActivity(new Intent(CreateSellerActivity.this, CreateItemActivity.class));

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create seller: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}