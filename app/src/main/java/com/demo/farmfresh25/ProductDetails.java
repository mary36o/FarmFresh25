package com.demo.farmfresh25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
//import com.demo.farmfresh25.ShoppingCart.ShoppingCart;
import com.demo.farmfresh25.ShoppingCart.ShoppingCartActivity;
import com.demo.farmfresh25.ui.checkout.CheckoutFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    // Views
    ImageView image, btnBack, btnFavorite;
    TextView name, price, originalPrice, discountBadge, ratingText, description;
    Button addToCart, buyNow;
    Button btnPlus, btnMinus;
    TextView txtQuantity;

    // Data
    public  static int quantity = 1;
    FirebaseFirestore db;
    boolean isFavorite = false;
    String productName, productPrice, productImage, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details1);

        // Initialize Firebase first
        initializeFirebase();

        // Initialize views
        initializeViews();
        setupClickListeners();

        // Get product ID
        productId = getIntent().getStringExtra("productId");
        if (productId == null) {
            Toast.makeText(this, "Product ID missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load product
        loadProduct(productId);
    }

    private void initializeFirebase() {
        try {
            // Ensure Firebase is initialized
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
            }
            db = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, "Firebase initialization error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        image = findViewById(R.id.detailImage);
        name = findViewById(R.id.detailName);
        price = findViewById(R.id.detailPrice);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        txtQuantity = findViewById(R.id.txtQuantity);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        originalPrice = findViewById(R.id.originalPrice);
        discountBadge = findViewById(R.id.discountBadge);
        ratingText = findViewById(R.id.ratingText);
        description = findViewById(R.id.description);
    }

    private void setupClickListeners() {
        btnPlus.setOnClickListener(v -> {
            quantity = quantity + 1;
            txtQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity = quantity - 1;
                txtQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            if (isFavorite) {
                btnFavorite.setImageResource(R.drawable.gradient_header);
                btnFavorite.setColorFilter(ContextCompat.getColor(this, R.color.error_color));
                Toast.makeText(ProductDetails.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                btnFavorite.setImageResource(R.drawable.favorite_24dp_ea3323_fill0_wght400_grad0_opsz24);
                btnFavorite.setColorFilter(ContextCompat.getColor(this, R.color.white));
                Toast.makeText(ProductDetails.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
        });

        buyNow.setOnClickListener(v -> {
            if (productName != null) {
                addToCartAndCheckout(productName, productPrice, productImage);
            } else {
                Toast.makeText(this, "Product not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        String currentPrice = productPrice;
        if (!currentPrice.isEmpty() && !currentPrice.equals("GHS 0.00")) {
            String priceStr = currentPrice;
            try {
                double singlePrice = Double.parseDouble(priceStr);
                double totalPrice = singlePrice * quantity;
                price.setText(String.format("GHS %.2f", totalPrice));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProduct(String productId) {
        if (db == null) {
            Toast.makeText(this, "Firestore not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("sub_product")
                .document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        productName = documentSnapshot.getString("name");
                        productPrice = documentSnapshot.getString("price");
                        productImage = documentSnapshot.getString("image");
                        String productDescription = documentSnapshot.getString("description");
                        Double productRating = documentSnapshot.getDouble("rating");

                        name.setText(productName);
                        price.setText(String.format("GHS %s", productPrice));

                        if (productDescription != null && !productDescription.isEmpty()) {
                            description.setText(productDescription);
                        }

                        Glide.with(ProductDetails.this)
                                .load(productImage)
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .into(image);

                        addToCart.setOnClickListener(v -> addToCart(productName, productPrice, productImage));
                    } else {
                        Toast.makeText(ProductDetails.this, "Product not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetails.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void addToCart(String productName, String productPrice, String productImage) {
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("name", productName);
        cartItem.put("price", productPrice);
        cartItem.put("image", productImage);
        cartItem.put("quantity", quantity);
        cartItem.put("timestamp", System.currentTimeMillis());

        db.collection("cart")
                .add(cartItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ProductDetails.this,
                            String.format("Added %d item(s) to Cart", quantity),
                            Toast.LENGTH_SHORT).show();

                    // Open ShoppingCartActivity (not Fragment)
                    Intent intent = new Intent(ProductDetails.this, ShoppingCartActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetails.this,
                            "Failed to add to cart: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void addToCartAndCheckout(String productName, String productPrice, String productImage) {
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("name", productName);
        cartItem.put("price", productPrice);
        cartItem.put("image", productImage);
        cartItem.put("quantity", quantity);
        cartItem.put("timestamp", System.currentTimeMillis());

        db.collection("cart")
                .add(cartItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ProductDetails.this, "Proceeding to checkout", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    double sub = Double.parseDouble(productPrice) * quantity;
                    double delivery = 10.00;
                    bundle.putDouble("subtotal", sub);
                    bundle.putDouble("discount", 0);
                    bundle.putDouble("deliveryFee", delivery);
                    bundle.putDouble("totalAmount", sub + delivery);

                    CheckoutFragment checkoutFragment = new CheckoutFragment();
                    checkoutFragment.setArguments(bundle);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(android.R.id.content, checkoutFragment)
                            .addToBackStack(null)
                            .commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetails.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}