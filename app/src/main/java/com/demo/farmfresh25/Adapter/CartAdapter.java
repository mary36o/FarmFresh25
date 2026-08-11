package com.demo.farmfresh25.Adapter;

import android.content.Context;
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

    Context context;

    // Interface for cart item changes
    public interface OnCartItemChangedListener {
        void onCartItemChanged();
    }

    public CartAdapter(List<CartModel> cartList,Context context) {
        this.cartList = cartList;
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public CartAdapter(List<CartModel> cartList, OnCartItemChangedListener listener,Context context) {
        this.cartList = cartList;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
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
        if (cartList == null || cartList.isEmpty()) {
            return;
        }

        CartModel item = cartList.get(position);
        if (item == null) {
            return;
        }

        // Set data with null checks
        if (holder.productName != null) {
            holder.productName.setText(item.getName() != null ? item.getName() : "Unknown Product");
        }

        if (holder.productPrice != null) {
            holder.productPrice.setText(String.format("GHS %s", item.getPrice() != null ? item.getPrice() : "0.00"));
        }

        if (holder.productQuantity != null) {
            holder.productQuantity.setText(String.valueOf(item.getQuantity()));
        }

        // Calculate total for this item
        if (holder.itemTotal != null) {
            try {
                String priceStr = item.getPrice();
                if (priceStr != null && !priceStr.isEmpty()) {
                    double price = Double.parseDouble(priceStr);
                    double total = price * item.getQuantity();
                    holder.itemTotal.setText(String.format("GHS %.2f", total));
                } else {
                    holder.itemTotal.setText("GHS 0.00");
                }
            } catch (Exception e) {
                holder.itemTotal.setText("GHS 0.00");
            }
        }

        // Load image using Glide
        if (holder.productImage != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage() != null ? item.getImage() : "")
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.productImage);
        }

        // Plus button - Check if not null first
        if (holder.btnPlus != null) {
            holder.btnPlus.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() + 1;
                updateQuantity(item.getId(), newQuantity, position, holder.itemView.getContext());
            });
        }

        // Minus button - Check if not null first
        if (holder.btnMinus != null) {
            holder.btnMinus.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    int newQuantity = item.getQuantity() - 1;
                    updateQuantity(item.getId(), newQuantity, position, holder.itemView.getContext());
                }
            });
        }

        // Delete button - Check if not null first
        if (holder.btnDelete != null) {

                        holder.btnDelete.setOnClickListener(v -> {
                deleteItem(item.getId(), position, holder.itemView.getContext());
            });
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateQuantity(String documentId, int newQuantity, int position, Context context) {
        if (documentId == null || documentId.isEmpty()) {
            return;
        }

        db.collection("cart")
                .document(documentId)
                .update("quantity", newQuantity)
                .addOnSuccessListener(aVoid -> {
                    if (cartList != null && position < cartList.size()) {
                        cartList.get(position).setQuantity(newQuantity);
                        notifyItemChanged(position);
                        if (listener != null) {
                            listener.onCartItemChanged();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (context != null) {
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteItem(String documentId, int position, Context context) {
        if (documentId == null || documentId.isEmpty()) {
            return;
        }

        db.collection("cart")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (cartList != null && position < cartList.size()) {
                        cartList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartList.size());
                        if (listener != null) {
                            listener.onCartItemChanged();
                        }
                        if (context != null) {
                            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (context != null) {
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage,btnDelete;
        TextView productName, productPrice, productQuantity, itemTotal;
        Button btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                productImage = itemView.findViewById(R.id.cartImage);
                productName = itemView.findViewById(R.id.cartName);
                productPrice = itemView.findViewById(R.id.cartPrice);
                productQuantity = itemView.findViewById(R.id.cartQuantity);
                itemTotal = itemView.findViewById(R.id.itemTotal);
                btnPlus = itemView.findViewById(R.id.btnPlus);
                btnMinus = itemView.findViewById(R.id.btnMinus);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}