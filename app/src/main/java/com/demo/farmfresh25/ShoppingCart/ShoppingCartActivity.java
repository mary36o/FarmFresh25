package com.demo.farmfresh25.ShoppingCart;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Adapter.CartAdapter;
import com.demo.farmfresh25.Model.CartModel;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.ui.checkout.CheckoutFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangedListener {

    // Views
    private RecyclerView recyclerView;
    private TextView totalPrice, subtotalPrice, deliveryFee, discountAmount, cartItemCount;
    private Button checkoutBtn, btnApplyCoupon;
    private EditText edtCoupon;
    private LinearLayout emptyCartLayout, discountLayout;
    private ImageView btnDeleteAll, btnBack;
    private Toolbar toolbar;

    // Data
    private FirebaseFirestore db;
    private ArrayList<CartModel> cartList;
    private CartAdapter adapter;

    private double subtotal = 0;
    private double deliveryFeeAmount = 10.00;
    private double discount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Initialize views
        initializeViews();

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Cart");
        }

        // Setup back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize lists and adapter
        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load cart items
        loadCart();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerCart);
        totalPrice = findViewById(R.id.totalPrice);
        subtotalPrice = findViewById(R.id.subtotalPrice);
        deliveryFee = findViewById(R.id.deliveryFee);
        discountAmount = findViewById(R.id.discountAmount);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        btnApplyCoupon = findViewById(R.id.btnApplyCoupon);
        edtCoupon = findViewById(R.id.edtCoupon);
        emptyCartLayout = findViewById(R.id.emptyCartLayout);
        discountLayout = findViewById(R.id.discountLayout);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        btnBack = findViewById(R.id.btnBack);
//        cartItemCount = findViewById(R.id.cartItemCount);
    }

    private void setupClickListeners() {
        // Apply coupon button
        btnApplyCoupon.setOnClickListener(v -> applyCoupon());

        // Checkout button
        checkoutBtn.setOnClickListener(v -> proceedToCheckout());

        // Delete all button
        btnDeleteAll.setOnClickListener(v -> deleteAllCartItems());

        // Continue shopping button (from empty cart)
        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);
        if (btnContinueShopping != null) {
            btnContinueShopping.setOnClickListener(v -> {
                finish(); // Go back to previous activity
            });
        }
    }

    private void applyCoupon() {
        String couponCode = edtCoupon.getText().toString().trim();

        if (TextUtils.isEmpty(couponCode)) {
            Toast.makeText(this, "Please enter coupon code", Toast.LENGTH_SHORT).show();
            return;
        }

        // Example coupon logic
        if (couponCode.equalsIgnoreCase("SAVE10")) {
            discount = subtotal * 0.10;
            discountAmount.setText(String.format("- GHS %.2f", discount));
            discountLayout.setVisibility(View.VISIBLE);
            updateTotal();
            Toast.makeText(this, "Coupon applied! 10% discount", Toast.LENGTH_SHORT).show();
        } else if (couponCode.equalsIgnoreCase("SAVE20")) {
            discount = subtotal * 0.20;
            discountAmount.setText(String.format("- GHS %.2f", discount));
            discountLayout.setVisibility(View.VISIBLE);
            updateTotal();
            Toast.makeText(this, "Coupon applied! 20% discount", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid coupon code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCartItemChanged() {
        calculateTotals();
    }

    private void calculateTotals() {
        subtotal = 0;
        for (CartModel item : cartList) {
            try {
                double price = Double.parseDouble(item.getPrice());
                subtotal += price * item.getQuantity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        subtotalPrice.setText(String.format("GHS %.2f", subtotal));
        deliveryFee.setText(String.format("GHS %.2f", deliveryFeeAmount));
        updateTotal();

        // Update cart item count
        if (cartItemCount != null) {
            cartItemCount.setText(cartList.size() + " items");
        }
    }

    private void updateTotal() {
        double total = subtotal + deliveryFeeAmount - discount;
        totalPrice.setText(String.format("GHS %.2f", total));
    }

    private void proceedToCheckout() {
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to Checkout Fragment
        CheckoutFragment checkoutFragment = new CheckoutFragment();

        // Pass total amount
        Bundle bundle = new Bundle();
        bundle.putDouble("totalAmount", subtotal + deliveryFeeAmount - discount);
        checkoutFragment.setArguments(bundle);

        // Replace current content with checkout fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, checkoutFragment)
                .addToBackStack(null)
                .commit();
    }

    private void deleteAllCartItems() {
        db.collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        db.collection("cart")
                                .document(doc.getId())
                                .delete();
                    }
                    Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to clear cart", Toast.LENGTH_SHORT).show());
    }

    private void loadCart() {
        db.collection("cart")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    cartList.clear();
                    subtotal = 0;

                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            String name = doc.getString("name");
                            String price = doc.getString("price");
                            String image = doc.getString("image");
                            Long quantityLong = doc.getLong("quantity");
                            int quantity = (quantityLong != null) ? quantityLong.intValue() : 1;

                            CartModel cartItem = new CartModel(
                                    doc.getId(),
                                    name,
                                    price,
                                    image,
                                    quantity
                            );

                            cartList.add(cartItem);

                            try {
                                double priceValue = Double.parseDouble(price);
                                subtotal += priceValue * quantity;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // Show cart content, hide empty layout
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyCartLayout.setVisibility(View.GONE);

                        // Update totals
                        subtotalPrice.setText(String.format("GHS %.2f", subtotal));
                        deliveryFee.setText(String.format("GHS %.2f", deliveryFeeAmount));
                        updateTotal();

                        // Update cart item count
                        if (cartItemCount != null) {
                            cartItemCount.setText(cartList.size() + " items");
                        }

                    } else {
                        // Show empty cart layout
                        recyclerView.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.VISIBLE);
                        totalPrice.setText("GHS 0.00");
                        subtotalPrice.setText("GHS 0.00");

                        if (cartItemCount != null) {
                            cartItemCount.setText("0 items");
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}