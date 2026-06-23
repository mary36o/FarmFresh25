package com.demo.farmfresh25.ui.checkout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class CheckoutFragment extends Fragment {

    EditText edtName, edtPhone, edtAddress;
    TextView txtTotal;
    Button btnPlaceOrder;

    FirebaseFirestore db;
    double totalAmount = 0;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize views
        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);
        txtTotal = view.findViewById(R.id.txtTotal);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load cart total
        loadTotal();

        // Place order button click
        btnPlaceOrder.setOnClickListener(v -> placeOrder());

        return view;
    }

    private void loadTotal() {
        db.collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    totalAmount = 0;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        String price = doc.getString("price");
                        Long quantityLong = doc.getLong("quantity");

                        int quantity = (quantityLong != null)
                                ? quantityLong.intValue()
                                : 1;

                        try {
                            double priceValue = Double.parseDouble(price);
                            totalAmount += priceValue * quantity;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String tAmount = "Total: GHS " + totalAmount;
                    txtTotal.setText(tAmount);
                });
    }

    private void placeOrder() {

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Enter your name");
            edtName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Enter your phone number");
            edtPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Enter your address");
            edtAddress.requestFocus();
            return;
        }

        // Create order object
        Map<String, Object> order = new HashMap<>();
        order.put("customerName", name);
        order.put("phone", phone);
        order.put("address", address);
        order.put("totalAmount", totalAmount);
        order.put("timestamp", System.currentTimeMillis());

        // Save order to Firestore
        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {

                    // Clear cart
                    db.collection("cart")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {

                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    db.collection("cart")
                                            .document(doc.getId())
                                            .delete();
                                }

                                Toast.makeText(requireContext(),
                                        "Order placed successfully!",
                                        Toast.LENGTH_LONG).show();

                                // Go back to previous fragment
                                requireActivity()
                                        .getSupportFragmentManager()
                                        .popBackStack();
                            });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }
}