package com.demo.farmfresh25.Seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    private Context context;
    private List<Seller> sellerList;
    private FirebaseFirestore db;

    public SellerAdapter(Context context, List<Seller> sellerList) {
        this.context = context;
        this.sellerList = sellerList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller, parent, false);
        return new SellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
        Seller seller = sellerList.get(position);

        holder.tvName.setText(seller.getName());
        holder.tvStoreName.setText(seller.getStoreName());
        holder.tvEmail.setText(seller.getEmail());
        holder.tvPhone.setText(seller.getPhone());
        holder.tvAddress.setText(seller.getAddress());

        if (seller.getImageUrl() != null && !seller.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(seller.getImageUrl())
                    .placeholder(R.drawable.profile_placeholder)
                    .error(R.drawable.profile_placeholder)
                    .into(holder.ivProfile);
        } else {
            holder.ivProfile.setImageResource(R.drawable.profile_placeholder);
        }

        // Update button
        holder.btnUpdate.setOnClickListener(v -> showUpdateDialog(seller, position));

        // Delete button
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmation(seller, position));
    }

    private void showUpdateDialog(Seller seller, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_seller_update, null);

        EditText etName = view.findViewById(R.id.etName);
        EditText etStoreName = view.findViewById(R.id.etStoreName);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etAddress = view.findViewById(R.id.etAddress);

        etName.setText(seller.getName());
        etStoreName.setText(seller.getStoreName());
        etEmail.setText(seller.getEmail());
        etPhone.setText(seller.getPhone());
        etAddress.setText(seller.getAddress());

        builder.setView(view)
                .setTitle("Update Seller")
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String storeName = etStoreName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();

                    if (name.isEmpty() || storeName.isEmpty()) {
                        Toast.makeText(context, "Name and Store Name required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    seller.setName(name);
                    seller.setStoreName(storeName);
                    seller.setEmail(email);
                    seller.setPhone(phone);
                    seller.setAddress(address);

                    updateSellerInFirestore(seller, position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateSellerInFirestore(Seller seller, int position) {
        db.collection("sellers").document(seller.getId())
                .set(seller)
                .addOnSuccessListener(aVoid -> {
                    sellerList.set(position, seller);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Seller updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmation(Seller seller, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Seller")
                .setMessage("Are you sure you want to delete " + seller.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("sellers").document(seller.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                if (position >= 0 && position < sellerList.size()) {
                                    sellerList.remove(position);
                                    notifyItemRemoved(position);
                                }
                                Toast.makeText(context, "Seller deleted", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return sellerList != null ? sellerList.size() : 0;
    }

    public void updateList(List<Seller> newList) {
        this.sellerList = newList;
        notifyDataSetChanged();
    }

    static class SellerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvName, tvStoreName, tvEmail, tvPhone, tvAddress;
        Button btnUpdate, btnDelete;

        SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivSellerImage);
            tvName = itemView.findViewById(R.id.tvSellerName);
            tvStoreName = itemView.findViewById(R.id.tvShopName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
