//package com.demo.farmfresh25.OrderHistory;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.demo.farmfresh25.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//
//public class OrderHistoryActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private TextView emptyText;
//    private Toolbar toolbar;
//
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private ArrayList<OrderModel> orderList;
//    private OrderAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_history);
//
//        // Initialize Firebase
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize views
//        initializeViews();
//
//        // Setup toolbar
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle("Order History");
//        }
//
//        // Setup RecyclerView
//        orderList = new ArrayList<>();
//        adapter = new OrderAdapter(orderList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        // Load orders
//        loadOrders();
//    }
//
//    private void initializeViews() {
//        toolbar = findViewById(R.id.toolbar);
//        recyclerView = findViewById(R.id.recyclerView);
//        emptyText = findViewById(R.id.emptyText);
//    }
//
//    private void loadOrders() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null) {
//            Toast.makeText(this, "Please login to view orders", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        db.collection("orders")
//                .whereEqualTo("userId", user.getUid())
//                .orderBy("timestamp")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    orderList.clear();
//
//                    if (queryDocumentSnapshots.isEmpty()) {
//                        emptyText.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                        return;
//                    }
//
//                    emptyText.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        OrderModel order = new OrderModel(
//                                doc.getId(),
//                                doc.getString("orderId"),
//                                doc.getString("items"),
//                                doc.getDouble("totalAmount"),
//                                doc.getString("status"),
//                                doc.getLong("timestamp")
//                        );
//                        orderList.add(order);
//                    }
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Error loading orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    // Order Model Class
//    public static class OrderModel {
//        private String id, orderId, items, status;
//        private double totalAmount;
//        private long timestamp;
//
//        public OrderModel(String id, String orderId, String items, double totalAmount, String status, long timestamp) {
//            this.id = id;
//            this.orderId = orderId;
//            this.items = items;
//            this.totalAmount = totalAmount;
//            this.status = status;
//            this.timestamp = timestamp;
//        }
//
//        public String getId() { return id; }
//        public String getOrderId() { return orderId; }
//        public String getItems() { return items; }
//        public double getTotalAmount() { return totalAmount; }
//        public String getStatus() { return status; }
//        public long getTimestamp() { return timestamp; }
//    }
//
//    // Order Adapter Class
//    public static class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
//
//        private ArrayList<OrderModel> orderList;
//
//        public OrderAdapter(ArrayList<OrderModel> orderList) {
//            this.orderList = orderList;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_order, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            OrderModel order = orderList.get(position);
//
//            holder.orderId.setText("Order #" + order.getOrderId());
//            holder.items.setText(order.getItems());
//            holder.totalAmount.setText(String.format("GHS %.2f", order.getTotalAmount()));
//            holder.status.setText(order.getStatus());
//
//            // Set status color
//            int color = R.color.secondary_text;
//            if ("Delivered".equals(order.getStatus())) {
//                color = R.color.success_color;
//            } else if ("Processing".equals(order.getStatus())) {
//                color = R.color.star_color;
//            } else if ("Cancelled".equals(order.getStatus())) {
//                color = R.color.error_color;
//            }
//            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), color));
//
//            // Format date
//            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
//            holder.orderDate.setText(sdf.format(new Date(order.getTimestamp())));
//        }
//
//        @Override
//        public int getItemCount() {
//            return orderList.size();
//        }
//
//        public static class ViewHolder extends RecyclerView.ViewHolder {
//            TextView orderId, items, totalAmount, status, orderDate;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                orderId = itemView.findViewById(R.id.orderId);
//                items = itemView.findViewById(R.id.items);
//                totalAmount = itemView.findViewById(R.id.totalAmount);
//                status = itemView.findViewById(R.id.status);
//                orderDate = itemView.findViewById(R.id.orderDate);
//            }
//        }
//    }
//}