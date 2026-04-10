package com.demo.farmfresh25;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Model.CartModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    ImageView image;
    TextView name, price;
    Button addToCart;

    Button btnPlus, btnMinus;
    TextView txtQuantity;

    int quantity = 1;
    FirebaseFirestore db; // ✅ Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details1);

        image = findViewById(R.id.detailImage);
        name = findViewById(R.id.detailName);
        price = findViewById(R.id.detailPrice);
        addToCart = findViewById(R.id.addToCart);

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        txtQuantity = findViewById(R.id.txtQuantity);


        btnPlus.setOnClickListener(v -> {
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });

        // ✅ Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // ✅ Get ID from intent
        String productId = getIntent().getStringExtra("productId");

        if (productId == null) {
            Toast.makeText(this, "Product ID missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Load product
        loadProduct(productId);
    }

    private void loadProduct(String productId) {

        db.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String productName = documentSnapshot.getString("name");
                        String productPrice = documentSnapshot.getString("price");
                        String productImage = documentSnapshot.getString("image");

                        name.setText(productName);
                        price.setText(productPrice);

                        Glide.with(ProductDetails.this)
                                .load(productImage)
                                .into(image);


                        addToCart.setOnClickListener(v -> {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("name", productName);
                            cartItem.put("price", productPrice);
                            cartItem.put("image", productImage);
                            cartItem.put("quantity", 1);

                            db.collection("cart")
                                    .add(cartItem)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(ProductDetails.this,
                                                "Added to Cart", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ProductDetails.this,
                                                "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        });

                    } else {
                        Toast.makeText(ProductDetails.this,
                                "Product not found", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetails.this,
                            "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}