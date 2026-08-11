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

import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        // Set order details
        String orderId = order.getOrderId();
        holder.tvOrderId.setText(orderId != null ? "Order #" + orderId : "Order");

        String status = order.getStatus();
        holder.tvStatus.setText(status != null ? status : "");

        String customerName = order.getCustomerName();
        holder.tvProductName.setText(customerName != null ? customerName : "Customer");

        String phone = order.getPhone();
        String email = order.getEmail();
        if (phone != null && !phone.isEmpty()) {
            holder.tvBuyerName.setText("Phone: " + phone);
        } else if (email != null && !email.isEmpty()) {
            holder.tvBuyerName.setText("Email: " + email);
        } else {
            holder.tvBuyerName.setText("");
        }

        String paymentMethod = order.getPaymentMethod();
        holder.tvQuantity.setText("Payment: " + (paymentMethod != null ? paymentMethod : "-"));

        holder.tvTotalPrice.setText(String.format("Total: GH₵ %.2f", order.getTotalAmount()));

        // Set date
        if (order.getTimestamp() > 0) {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(order.getTimestamp()));
            holder.tvOrderDate.setText(date);
        } else {
            holder.tvOrderDate.setText("");
        }

        // Show action buttons only for pending orders
        if ("Pending".equals(status)) {
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
        TextView tvOrderId, tvStatus, tvProductName, tvBuyerName, tvQuantity, tvTotalPrice, tvOrderDate;
        Button btnAccept, btnReject;
        LinearLayout llActions;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBuyerName = itemView.findViewById(R.id.tvBuyerName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            llActions = itemView.findViewById(R.id.llActions);
        }
    }
}
