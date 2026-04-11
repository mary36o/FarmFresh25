package com.demo.farmfresh25.ShoppingCart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    public ShoppingCart() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        totalPrice = view.findViewById(R.id.totalPrice);

        db = FirebaseFirestore.getInstance();
        cartList = new ArrayList<>();

        adapter = new CartAdapter(cartList);
        recyclerView.setAdapter(adapter);

        loadCart();

        return view;

    }

    private void loadCart() {

        db.collection("cart")
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Toast.makeText(requireContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    cartList.clear();

                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {

                            String name = doc.getString("name");
                            String price = doc.getString("price");
                            String image = doc.getString("image");

                            Long quantityLong = doc.getLong("quantity");
                            int quantity = (quantityLong != null) ? quantityLong.intValue() : 1;

                            cartList.add(new CartModel(name, price, image, quantity));
                        }

                        try {
                            double priceValue = Double.parseDouble(price);
                            total += priceValue * quantity;
                        } catch (Exception e) {
                            e.printStackTrace();
                            // ✅ SHOW TOTAL
                            totalPrice.setText("Total: GHS " + total);
                    }

                    adapter.notifyDataSetChanged();

                    if (cartList.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Cart is empty", Toast.LENGTH_SHORT).show();
                    }


                });


    }
}
