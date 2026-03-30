package com.demo.farmfresh25;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetails extends AppCompatActivity {

    ImageView image;
    TextView name, price;
    Button addToCart;

    DatabaseReference databaseReference;

    // ✅ ADD THESE (important for cart)
    String productName, productPrice, productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details1);

        image = findViewById(R.id.detailImage);
        name = findViewById(R.id.detailName);
        price = findViewById(R.id.detailPrice);
        addToCart = findViewById(R.id.addToCart);

        String productId = getIntent().getStringExtra("productId");

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("products")
                .child(productId);

        loadProduct();
    }

    private void loadProduct() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ✅ SAVE DATA
                productName = snapshot.child("name").getValue(String.class);
                productPrice = snapshot.child("price").getValue(String.class);
                productImage = snapshot.child("image").getValue(String.class);

                name.setText(productName);
                price.setText(productPrice);

                Glide.with(ProductDetails.this)
                        .load(productImage)
                        .into(image);

                // ✅ ADD TO CART BUTTON WORKING
                addToCart.setOnClickListener(v -> {

                    CartManager.cartList.add(
                            new CartModel(productName, productPrice, productImage)
                    );

                    Toast.makeText(ProductDetails.this,
                            "Added to Cart", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}