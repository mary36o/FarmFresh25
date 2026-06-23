package com.demo.farmfresh25.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Model.CartModel;
import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartModel> cartList;
    private FirebaseFirestore db;
    private OnCartItemChangedListener listener;

    // Interface for cart item changes
    public interface OnCartItemChangedListener {
        void onCartItemChanged();
    }

    public CartAdapter(List<CartModel> cartList) {
        this.cartList = cartList;
        this.db = FirebaseFirestore.getInstance();
    }

    // Constructor with listener
    public CartAdapter(List<CartModel> cartList, OnCartItemChangedListener listener) {
        this.cartList = cartList;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel item = cartList.get(position);

        holder.productName.setText(item.getName());
        holder.productPrice.setText(String.format("GHS %s", item.getPrice()));
        holder.productQuantity.setText(String.valueOf(item.getQuantity()));

        // Calculate total for this item
        try {
            double price = Double.parseDouble(item.getPrice());
            double total = price * item.getQuantity();
            holder.itemTotal.setText(String.format("GHS %.2f", total));
        } catch (Exception e) {
            holder.itemTotal.setText(String.format("GHS %s", item.getPrice()));
        }

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
                .into(holder.productImage);

        // Plus button
        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            updateQuantity(item.getId(), newQuantity, position, holder.itemView.getContext());
        });

        // Minus button
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                updateQuantity(item.getId(), newQuantity, position, holder.itemView.getContext());
            } else {
                // Remove item if quantity is 1 and user clicks minus
                deleteItem(item.getId(), position, holder.itemView.getContext());
            }
        });

        // Delete button
        holder.btnDelete.setOnClickListener(v -> {
            deleteItem(item.getId(), position, holder.itemView.getContext());
        });
    }

    private void updateQuantity(String documentId, int newQuantity, int position, android.content.Context context) {
        db.collection("cart")
                .document(documentId)
                .update("quantity", newQuantity)
                .addOnSuccessListener(aVoid -> {
                    cartList.get(position).setQuantity(newQuantity);
                    notifyItemChanged(position);
                    if (listener != null) {
                        listener.onCartItemChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context,
                            "Failed to update quantity", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteItem(String documentId, int position, android.content.Context context) {
        db.collection("cart")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartList.size());
                    if (listener != null) {
                        listener.onCartItemChanged();
                    }
                    Toast.makeText(context,
                            "Item removed from cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context,
                            "Failed to remove item", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productQuantity, itemTotal;
        Button btnPlus, btnMinus, btnDelete;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartImage);
            productName = itemView.findViewById(R.id.cartName);
            productPrice = itemView.findViewById(R.id.cartPrice);
            productQuantity = itemView.findViewById(R.id.cartQuantity);
            itemTotal = itemView.findViewById(R.id.itemTotal);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}