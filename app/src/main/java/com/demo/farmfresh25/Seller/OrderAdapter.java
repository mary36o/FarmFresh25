package com.demo.farmfresh25.Seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Order;
import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.admin.v1.Index;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderModel> orderList;
    private FirebaseFirestore db;

    public OrderAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.db = FirebaseFirestore.getInstance();
    }

//    public OrderAdapter(Order2Activity context, List<Index.IndexField.Order> orderList) {
//    }

    public OrderAdapter() {
    }

    public OrderAdapter(Order2Activity order2Activity, List<Order> orderList) {
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Set order details
        String orderId = order.getOrderId();
        if (orderId != null && orderId.length() > 8) {
            holder.tvOrderId.setText("Order #" + orderId.substring(0, 8));
        } else {
            holder.tvOrderId.setText("Order #" + orderId);
        }

        holder.tvProductName.setText(order.getProductName());
        holder.tvBuyerName.setText("Buyer: " + order.getBuyerName());
        holder.tvQuantity.setText("Qty: " + order.getQuantity());
        holder.tvTotalPrice.setText("Total: GH₵ " + String.format("%.2f", order.getTotalPrice()));

        // Set date
        if (order.getTimestamp() > 0) {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(order.getTimestamp()));
            holder.tvOrderDate.setText(date);
        }

        // Set status with color
        String status = order.getStatus();
        holder.tvStatus.setText(status);

        // Set status background color
        switch (status) {
            case "Pending":
                holder.tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "Accepted":
            case "Preparing":
                holder.tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                break;
            case "Shipped":
                holder.tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_purple));
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                break;
            case "Delivered":
                holder.tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "Cancelled":
                holder.tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                holder.tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                break;
        }

        // Show action buttons only for pending orders
        if (status.equals("Pending")) {
            holder.llActions.setVisibility(View.VISIBLE);
            holder.btnAccept.setOnClickListener(v -> updateOrderStatus(order, "Accepted"));
            holder.btnReject.setOnClickListener(v -> updateOrderStatus(order, "Cancelled"));
        } else {
            holder.llActions.setVisibility(View.GONE);
        }
    }

    private void updateOrderStatus(OrderModel order, String newStatus) {
        db.collection("orders")
                .document(order.getOrderId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Order " + newStatus.toLowerCase(),
                            Toast.LENGTH_SHORT).show();
                    order.setStatus(newStatus);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error updating order: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    // Method to update the list
    public void updateList(List<OrderModel> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvProductName, tvBuyerName, tvQuantity, tvTotalPrice, tvOrderDate, tvStatus;
        Button btnAccept, btnReject;
        LinearLayout llActions;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBuyerName = itemView.findViewById(R.id.tvBuyerName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            llActions = itemView.findViewById(R.id.llActions);
        }
    }
}