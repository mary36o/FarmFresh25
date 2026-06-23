//package com.demo.farmfresh25.ShoppingCart;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.demo.farmfresh25.Adapter.CartAdapter;
//import com.demo.farmfresh25.Model.CartModel;
//import com.demo.farmfresh25.R;
//import com.demo.farmfresh25.ui.checkout.CheckoutFragment;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//
//public class ShoppingCart extends Fragment implements CartAdapter.OnCartItemChangedListener {
//
//    FirebaseFirestore db;
//    ArrayList<CartModel> cartList;
//    CartAdapter adapter;
//
//    RecyclerView recyclerView;
//    TextView totalPrice, subtotalPrice, deliveryFee, discountAmount;
//    Button checkoutBtn, btnApplyCoupon;
//    EditText edtCoupon;
//    LinearLayout emptyCartLayout, discountLayout;
//    ImageView btnDeleteAll, btnBack;
//
//    double subtotal = 0;
//    double deliveryFeeAmount = 10.00;
//    double discount = 0;
//
//    public ShoppingCart() {
//        // Required empty constructor
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.activity_shopping_cart, container, false);
//
//        // Initialize views
//        initializeViews(view);
//
//        // RecyclerView setup
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//
//        // Initialize Firestore
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize list and adapter
//        cartList = new ArrayList<>();
//        adapter = new CartAdapter(cartList, this);
//        recyclerView.setAdapter(adapter);
//
//        // Load cart items
//        loadCart();
//
//        // Setup click listeners
//        setupClickListeners(view);
//
//        return view;
//    }
//
//    private void initializeViews(View view) {
//        recyclerView = view.findViewById(R.id.recyclerCart);
//        totalPrice = view.findViewById(R.id.totalPrice);
//        subtotalPrice = view.findViewById(R.id.subtotalPrice);
//        deliveryFee = view.findViewById(R.id.deliveryFee);
//        discountAmount = view.findViewById(R.id.discountAmount);
//        checkoutBtn = view.findViewById(R.id.checkoutBtn);
//        btnApplyCoupon = view.findViewById(R.id.btnApplyCoupon);
//        edtCoupon = view.findViewById(R.id.edtCoupon);
//        emptyCartLayout = view.findViewById(R.id.emptyCartLayout);
//        discountLayout = view.findViewById(R.id.discountLayout);
//        btnDeleteAll = view.findViewById(R.id.btnDeleteAll);
//        btnBack = view.findViewById(R.id.btnBack);
//    }
//
//    private void setupClickListeners(View view) {
//        // Back button - IMPORTANT: This will navigate back
//        if (btnBack != null) {
//            btnBack.setOnClickListener(v -> {
//                // Go back to previous fragment
//                requireActivity().getSupportFragmentManager().popBackStack();
//            });
//        }
//
//        // Apply coupon button
//        btnApplyCoupon.setOnClickListener(v -> applyCoupon());
//
//        // Checkout button
//        checkoutBtn.setOnClickListener(v -> proceedToCheckout());
//
//        // Delete all button
//        btnDeleteAll.setOnClickListener(v -> deleteAllCartItems());
//
//        // Continue shopping button (from empty cart)
//        Button btnContinueShopping = view.findViewById(R.id.btnContinueShopping);
//        if (btnContinueShopping != null) {
//            btnContinueShopping.setOnClickListener(v -> {
//                // Go back to previous fragment
//                requireActivity().getSupportFragmentManager().popBackStack();
//            });
//        }
//    }
//
//    private void applyCoupon() {
//        String couponCode = edtCoupon.getText().toString().trim();
//
//        if (TextUtils.isEmpty(couponCode)) {
//            Toast.makeText(requireContext(), "Please enter coupon code", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Example coupon logic
//        if (couponCode.equalsIgnoreCase("SAVE10")) {
//            discount = subtotal * 0.10;
//            discountAmount.setText(String.format("- GHS %.2f", discount));
//            discountLayout.setVisibility(View.VISIBLE);
//            updateTotal();
//            Toast.makeText(requireContext(), "Coupon applied! 10% discount", Toast.LENGTH_SHORT).show();
//        } else if (couponCode.equalsIgnoreCase("SAVE20")) {
//            discount = subtotal * 0.20;
//            discountAmount.setText(String.format("- GHS %.2f", discount));
//            discountLayout.setVisibility(View.VISIBLE);
//            updateTotal();
//            Toast.makeText(requireContext(), "Coupon applied! 20% discount", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(requireContext(), "Invalid coupon code", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onCartItemChanged() {
//        calculateTotals();
//    }
//
//    private void calculateTotals() {
//        subtotal = 0;
//        for (CartModel item : cartList) {
//            try {
//                double price = Double.parseDouble(item.getPrice());
//                subtotal += price * item.getQuantity();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        subtotalPrice.setText(String.format("GHS %.2f", subtotal));
//        deliveryFee.setText(String.format("GHS %.2f", deliveryFeeAmount));
//        updateTotal();
//    }
//
//    private void updateTotal() {
//        double total = subtotal + deliveryFeeAmount - discount;
//        totalPrice.setText(String.format("GHS %.2f", total));
//    }
//
//    private void proceedToCheckout() {
//        if (cartList.isEmpty()) {
//            Toast.makeText(requireContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Bundle bundle = new Bundle();
//        bundle.putDouble("totalAmount", subtotal + deliveryFeeAmount - discount);
//
//        CheckoutFragment checkoutFragment = new CheckoutFragment();
//        checkoutFragment.setArguments(bundle);
//
//        requireActivity()
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .replace(android.R.id.content, checkoutFragment)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    private void deleteAllCartItems() {
//        db.collection("cart")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        db.collection("cart")
//                                .document(doc.getId())
//                                .delete();
//                    }
//                    Toast.makeText(requireContext(), "Cart cleared", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e ->
//                        Toast.makeText(requireContext(), "Failed to clear cart", Toast.LENGTH_SHORT).show());
//    }
//
//    private void loadCart() {
//        db.collection("cart")
//                .addSnapshotListener((value, error) -> {
//                    if (error != null) {
//                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    cartList.clear();
//                    subtotal = 0;
//
//                    if (value != null && !value.isEmpty()) {
//                        for (QueryDocumentSnapshot doc : value) {
//                            String name = doc.getString("name");
//                            String price = doc.getString("price");
//                            String image = doc.getString("image");
//                            Long quantityLong = doc.getLong("quantity");
//                            int quantity = (quantityLong != null) ? quantityLong.intValue() : 1;
//
//                            CartModel cartItem = new CartModel(
//                                    doc.getId(),
//                                    name,
//                                    price,
//                                    image,
//                                    quantity
//                            );
//
//                            cartList.add(cartItem);
//
//                            try {
//                                double priceValue = Double.parseDouble(price);
//                                subtotal += priceValue * quantity;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        // Show cart content, hide empty layout
//                        recyclerView.setVisibility(View.VISIBLE);
//                        emptyCartLayout.setVisibility(View.GONE);
//
//                        // Update totals
//                        subtotalPrice.setText(String.format("GHS %.2f", subtotal));
//                        deliveryFee.setText(String.format("GHS %.2f", deliveryFeeAmount));
//                        updateTotal();
//
//                    } else {
//                        // Show empty cart layout
//                        recyclerView.setVisibility(View.GONE);
//                        emptyCartLayout.setVisibility(View.VISIBLE);
//                        totalPrice.setText("GHS 0.00");
//                        subtotalPrice.setText("GHS 0.00");
//                    }
//
//                    adapter.notifyDataSetChanged();
//                });
//    }
//}