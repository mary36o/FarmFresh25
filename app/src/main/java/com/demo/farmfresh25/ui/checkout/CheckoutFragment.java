package com.demo.farmfresh25.ui.checkout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CheckoutFragment extends Fragment {

    // Views
    private EditText edtName, edtPhone, edtAddress, edtEmail;
    private TextView txtSubtotal, txtDeliveryFee, txtTotal, txtOrderId, txtDiscount;
    private Button btnPlaceOrder, btnPayWithMom;
    private RadioGroup radioGroupPayment;
    private RadioButton radioCashOnDelivery, radioMobileMoney;
    private LinearLayout mobileMoneyLayout, discountLayout;
    private ImageView momoLogo;

    // Payment details
    private EditText edtMomoNumber, edtMomoProvider;

    // Data
    private FirebaseFirestore db;
    private double totalAmount = 0;
    private double subtotal = 0;
    private double deliveryFee = 10.00;
    private double discount = 0;
    private String selectedPaymentMethod = "Cash on Delivery";
    private String orderId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize views
        initializeViews(view);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get total amount from arguments
        if (getArguments() != null) {
            if (getArguments().containsKey("subtotal")) {
                subtotal = getArguments().getDouble("subtotal", 0);
                deliveryFee = getArguments().getDouble("deliveryFee", 10.00);
                discount = getArguments().getDouble("discount", 0);
                totalAmount = subtotal + deliveryFee - discount;
            } else {
                totalAmount = getArguments().getDouble("totalAmount", 0);
                subtotal = totalAmount - deliveryFee;
            }
        }

        // Load cart total if no argument
        if (totalAmount == 0) {
            loadTotal();
        } else {
            updatePriceDisplay();
        }

        // Generate order ID
        orderId = generateOrderId();

        // Setup click listeners
        setupClickListeners();

        // Initial payment state (Cash on Delivery is selected by default)
        radioCashOnDelivery.setChecked(true);
        mobileMoneyLayout.setVisibility(View.GONE);
        btnPayWithMom.setVisibility(View.GONE);
        btnPlaceOrder.setVisibility(View.VISIBLE);

        return view;
    }

    private void initializeViews(View view) {
        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtEmail = view.findViewById(R.id.edtEmail);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtDeliveryFee = view.findViewById(R.id.txtDeliveryFee);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtOrderId = view.findViewById(R.id.txtOrderId);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        btnPayWithMom = view.findViewById(R.id.btnPayWithMom);

        radioGroupPayment = view.findViewById(R.id.radioGroupPayment);
        radioCashOnDelivery = view.findViewById(R.id.radioCashOnDelivery);
        radioMobileMoney = view.findViewById(R.id.radioMobileMoney);
        mobileMoneyLayout = view.findViewById(R.id.mobileMoneyLayout);
        momoLogo = view.findViewById(R.id.momoLogo);

        edtMomoNumber = view.findViewById(R.id.edtMomoNumber);
        edtMomoProvider = view.findViewById(R.id.edtMomoProvider);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        discountLayout = view.findViewById(R.id.discountLayout);
    }

    private void setupClickListeners() {
        // Payment method selection
        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCashOnDelivery) {
                selectedPaymentMethod = "Cash on Delivery";
                mobileMoneyLayout.setVisibility(View.GONE);
                btnPayWithMom.setVisibility(View.GONE);
                btnPlaceOrder.setVisibility(View.VISIBLE);
                btnPlaceOrder.setText("PLACE ORDER");
            } else if (checkedId == R.id.radioMobileMoney) {
                selectedPaymentMethod = "Mobile Money";
                mobileMoneyLayout.setVisibility(View.VISIBLE);
                btnPayWithMom.setVisibility(View.VISIBLE);
                btnPlaceOrder.setVisibility(View.GONE);
            }
        });

        // Place Order button (Cash on Delivery)
        btnPlaceOrder.setOnClickListener(v -> {
            if (validateInputs()) {
                placeOrder();
            }
        });

        // Pay with Mobile Money button
        btnPayWithMom.setOnClickListener(v -> {
            if (validateInputs()) {
                processMobileMoneyPayment();
            }
        });
    }

    private boolean validateInputs() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Enter your full name");
            edtName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Enter your phone number");
            edtPhone.requestFocus();
            return false;
        }

        if (phone.length() < 10) {
            edtPhone.setError("Enter a valid phone number");
            edtPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Enter your delivery address");
            edtAddress.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter your email address");
            edtEmail.requestFocus();
            return false;
        }

        if (selectedPaymentMethod.equals("Mobile Money")) {
            String momoNumber = edtMomoNumber.getText().toString().trim();
            String momoProvider = edtMomoProvider.getText().toString().trim();

            if (TextUtils.isEmpty(momoNumber)) {
                edtMomoNumber.setError("Enter Mobile Money number");
                edtMomoNumber.requestFocus();
                return false;
            }

            if (momoNumber.length() < 10) {
                edtMomoNumber.setError("Enter a valid phone number");
                edtMomoNumber.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(momoProvider)) {
                edtMomoProvider.setError("Select a provider");
                edtMomoProvider.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void processMobileMoneyPayment() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String momoNumber = edtMomoNumber.getText().toString().trim();
        String momoProvider = edtMomoProvider.getText().toString().trim();

        // Show payment confirmation dialog
        showPaymentConfirmation(name, phone, address, email, momoNumber, momoProvider);
    }

    private void showPaymentConfirmation(String name, String phone, String address,
                                         String email, String momoNumber, String momoProvider) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_payment_confirmation, null);

        TextView tvAmount = dialogView.findViewById(R.id.tvAmount);
        TextView tvMomoNumber = dialogView.findViewById(R.id.tvMomoNumber);
        TextView tvProvider = dialogView.findViewById(R.id.tvProvider);
        TextView tvOrderId = dialogView.findViewById(R.id.tvOrderId);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        tvAmount.setText(String.format("GHS %.2f", totalAmount));
        tvMomoNumber.setText(momoNumber);
        tvProvider.setText(momoProvider);
        tvOrderId.setText(orderId);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Confirm Payment")
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            // Show payment processing
            showPaymentProcessing(name, phone, address, email, momoNumber, momoProvider);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showPaymentProcessing(String name, String phone, String address,
                                       String email, String momoNumber, String momoProvider) {
        // Show processing dialog
        AlertDialog processingDialog = new AlertDialog.Builder(getContext())
                .setTitle("Processing Payment")
                .setMessage("Please wait while we process your payment...\n\n" +
                        "You will receive a USSD push on " + momoNumber)
                .setCancelable(false)
                .create();
        processingDialog.show();

        // Simulate payment processing (In real app, integrate with payment gateway API)
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(() -> {
            processingDialog.dismiss();

            // Random success/failure for demo
            boolean paymentSuccess = Math.random() > 0.2;

            if (paymentSuccess) {
                showPaymentSuccess(name, phone, address, email, momoNumber, momoProvider, "Mobile Money");
            } else {
                showPaymentFailure();
            }
        }, 3000);
    }

    private void showPaymentSuccess(String name, String phone, String address,
                                    String email, String momoNumber, String momoProvider,
                                    String paymentMethod) {
        // Create order
        createOrder(name, phone, address, email, paymentMethod, momoNumber, momoProvider);

        View successView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_payment_success, null);

        TextView tvOrderId = successView.findViewById(R.id.tvOrderId);
        TextView tvAmount = successView.findViewById(R.id.tvAmount);
        TextView tvMethod = successView.findViewById(R.id.tvMethod);
        Button btnContinue = successView.findViewById(R.id.btnContinue);

        tvOrderId.setText(orderId);
        tvAmount.setText(String.format("GHS %.2f", totalAmount));
        tvMethod.setText(paymentMethod + " - " + momoProvider);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Payment Successful! 🎉")
                .setView(successView)
                .setCancelable(false)
                .create();

        btnContinue.setOnClickListener(v -> {
            dialog.dismiss();
            // Clear cart and navigate back
            clearCart();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        dialog.show();
    }

    private void showPaymentFailure() {
        new AlertDialog.Builder(getContext())
                .setTitle("Payment Failed")
                .setMessage("Your payment could not be processed. Please try again or use a different payment method.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    // Retry payment
                    String name = edtName.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    String address = edtAddress.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String momoNumber = edtMomoNumber.getText().toString().trim();
                    String momoProvider = edtMomoProvider.getText().toString().trim();
                    showPaymentProcessing(name, phone, address, email, momoNumber, momoProvider);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void placeOrder() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        createOrder(name, phone, address, email, "Cash on Delivery", "", "");
    }

    private void createOrder(String name, String phone, String address,
                             String email, String paymentMethod,
                             String momoNumber, String momoProvider) {
        // Create order object
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("customerName", name);
        order.put("phone", phone);
        order.put("address", address);
        order.put("email", email);
        order.put("paymentMethod", paymentMethod);
        order.put("totalAmount", totalAmount);
        order.put("subtotal", subtotal);
        order.put("discount", discount);
        order.put("deliveryFee", deliveryFee);
        order.put("timestamp", System.currentTimeMillis());
        order.put("status", "Pending");
        order.put("orderDate", new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(new Date()));

        // Add mobile money details if applicable
        if (paymentMethod.equals("Mobile Money")) {
            order.put("momoNumber", momoNumber);
            order.put("momoProvider", momoProvider);
        }

        // Save order to Firestore
        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    // Clear cart
                    clearCart();

                    if (paymentMethod.equals("Cash on Delivery")) {
                        Toast.makeText(getContext(),
                                "Order placed successfully!\nOrder ID: " + orderId,
                                Toast.LENGTH_LONG).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Failed to place order: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void clearCart() {
        db.collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        db.collection("cart")
                                .document(doc.getId())
                                .delete();
                    }
                });
    }

    private void loadTotal() {
        db.collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    totalAmount = 0;
                    subtotal = 0;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String price = doc.getString("price");
                        Long quantityLong = doc.getLong("quantity");
                        int quantity = (quantityLong != null) ? quantityLong.intValue() : 1;

                        try {
                            double priceValue = Double.parseDouble(price);
                            subtotal += priceValue * quantity;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    totalAmount = subtotal + deliveryFee;
                    updatePriceDisplay();
                });
    }

    private void updatePriceDisplay() {
        txtSubtotal.setText(String.format("GHS %.2f", subtotal));
        txtDeliveryFee.setText(String.format("GHS %.2f", deliveryFee));
        txtTotal.setText(String.format("GHS %.2f", totalAmount));
        txtOrderId.setText("Order #" + orderId);
        if (discount > 0 && discountLayout != null) {
            discountLayout.setVisibility(View.VISIBLE);
            txtDiscount.setText(String.format("- GHS %.2f", discount));
        } else if (discountLayout != null) {
            discountLayout.setVisibility(View.GONE);
        }
    }

    private String generateOrderId() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date());
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "FF-" + timestamp + "-" + random;
    }
}