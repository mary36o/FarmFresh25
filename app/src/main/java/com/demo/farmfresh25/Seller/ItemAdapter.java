package com.demo.farmfresh25.Seller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.Seller.EditProductActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private List<Item> itemList;
    private FirebaseFirestore db;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        // Set item details
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText("GH₵ " + String.format("%.2f", item.getPrice()));
        holder.tvCategory.setText(item.getCategory());
        holder.tvQuantity.setText("Stock: " + item.getQuantity());

        // Set description (truncate if too long)
        String description = item.getDescription();
        if (description != null && description.length() > 50) {
            description = description.substring(0, 47) + "...";
        }
        holder.tvDescription.setText(description != null ? description : "");

        // Load image if available
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_placeholder);
        }

        // Set status
        if (item.getQuantity() == 0) {
            holder.tvStatus.setText("Out of Stock");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if (item.getQuantity() < 10) {
            holder.tvStatus.setText("Low Stock");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            holder.tvStatus.setText("Available");
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        // Edit button - Navigate to EditProductActivity
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("productId", item.getId());
            context.startActivity(intent);
        });

        // Delete button with confirmation
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete \"" + item.getName() + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> deleteItem(item, position))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void deleteItem(Item item, int position) {
        db.collection("items")
                .document(item.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    itemList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error deleting: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void updateList(List<Item> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvCategory, tvQuantity, tvDescription, tvStatus;
        Button btnEdit, btnDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivItemImage);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
            tvCategory = itemView.findViewById(R.id.tvItemCategory);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}