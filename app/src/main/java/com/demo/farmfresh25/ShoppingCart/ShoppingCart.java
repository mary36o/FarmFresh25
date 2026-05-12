package com.demo.farmfresh25.ShoppingCart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Adapter.CartAdapter;
import com.demo.farmfresh25.Model.CartModel;
import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShoppingCart extends Fragment {

    FirebaseFirestore db;
    ArrayList<CartModel> cartList;
    CartAdapter adapter;

    RecyclerView recyclerView;
    TextView totalPrice;
    Button checkoutBtn;

    public ShoppingCart() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerCart);
        totalPrice = view.findViewById(R.id.totalPrice);
        checkoutBtn = view.findViewById(R.id.checkoutBtn);

        // RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize list and adapter
        cartList = new ArrayList<>();
        adapter = new CartAdapter(cartList);
        recyclerView.setAdapter(adapter);

        // Load cart items
        loadCart();

        // Checkout button
        checkoutBtn.setOnClickListener(v -> {
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
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
        });

        return view;
    }

    private void loadCart() {

        db.collection("cart")
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Toast.makeText(requireContext(),
                                error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    cartList.clear();
                    double total = 0;

                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {

                            String name = doc.getString("name");
                            String price = doc.getString("price");
                            String image = doc.getString("image");

                            Long quantityLong = doc.getLong("quantity");
                            int quantity = (quantityLong != null)
                                    ? quantityLong.intValue()
                                    : 1;

                            // Add item to cart list
                            cartList.add(new CartModel(
                                    doc.getId(),
                                    name,
                                    price,
                                    image,
                                    quantity
                            ));

                            // Calculate total
                            try {
                                double priceValue = Double.parseDouble(price);
                                total += priceValue * quantity;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // Update total price
                    totalPrice.setText("Total: GHS " + total);

                    // Refresh RecyclerView
                    adapter.notifyDataSetChanged();

                    // Empty cart message
                    if (cartList.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Cart is empty",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}