package com.demo.farmfresh25.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Seller.NotificationModel;
import com.demo.farmfresh25.NotificationSeller;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.Seller.NotificationAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SellerDashboardActivity extends AppCompatActivity {

    // Stats
    private TextView tvSellerName, tvSellerEmail, tvTotalProducts, tvTotalOrders, tvPendingOrders;
    private TextView tvTotalRevenue, tvTotalBuyers, tvUnreadCount;

    // Cards
    private CardView cardAddProduct, cardMyProducts, cardOrders, cardProfile;
    private CardView cardNotifications, cardAnalytics;

    // Buttons
    private Button btnLogout, btnViewAllNotifications;

    // Charts
    private LineChart lineChartSales;
    private BarChart barChartBuyers;

    // Notifications
    private RecyclerView rvNotifications;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationList;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        notificationList = new ArrayList<>();
        initViews();
        setupListeners();
        setupCharts();
        loadSellerData();
        loadStatistics();
        loadNotifications();
        loadSalesData();
        loadBuyerData();
    }

    private void initViews() {
        // Stats
        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalBuyers = findViewById(R.id.tvTotalBuyers);
        tvUnreadCount = findViewById(R.id.tvUnreadCount);

        // Cards
        cardAddProduct = findViewById(R.id.cardAddProduct);
        cardMyProducts = findViewById(R.id.cardMyProducts);
        cardOrders = findViewById(R.id.cardOrders);
        cardProfile = findViewById(R.id.cardProfile);
        cardNotifications = findViewById(R.id.cardNotifications);
        cardAnalytics = findViewById(R.id.cardAnalytics);

        // Buttons
        btnLogout = findViewById(R.id.btnLogout);
        btnViewAllNotifications = findViewById(R.id.btnViewAllNotifications);

        // Charts
        lineChartSales = findViewById(R.id.lineChartSales);
        barChartBuyers = findViewById(R.id.barChartBuyers);

        // Notifications RecyclerView
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this, notificationList,
                notification -> {
                    Toast.makeText(this, "Notification: " + notification.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    markNotificationAsRead(notification);
                });
        rvNotifications.setAdapter(notificationAdapter);
    }

    private void setupListeners() {
        // Add Product
        cardAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, CreateItemActivity.class);
            startActivity(intent);
        });

        // My Products
        cardMyProducts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, ItemListActivity.class);
            startActivity(intent);
        });

        // Orders
        cardOrders.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, Order2Activity.class);
            startActivity(intent);
        });

        // Profile
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, SellerProfileActivity.class);
            startActivity(intent);
        });

        // Notifications - Toggle visibility
        cardNotifications.setOnClickListener(v -> {
            if (rvNotifications.getVisibility() == View.VISIBLE) {
                rvNotifications.setVisibility(View.GONE);
                cardNotifications.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            } else {
                rvNotifications.setVisibility(View.VISIBLE);
                loadNotifications();
                cardNotifications.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }
        });

        // Analytics - Toggle chart visibility
        cardAnalytics.setOnClickListener(v -> {
            if (lineChartSales.getVisibility() == View.VISIBLE) {
                lineChartSales.setVisibility(View.GONE);
                barChartBuyers.setVisibility(View.GONE);
                cardAnalytics.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            } else {
                lineChartSales.setVisibility(View.VISIBLE);
                barChartBuyers.setVisibility(View.VISIBLE);
                cardAnalytics.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }
        });

        // View All Notifications
        btnViewAllNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, NotificationSeller.class);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SellerDashboardActivity.this, LoginActivity4.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupCharts() {
        // Sales Line Chart
        lineChartSales.getDescription().setEnabled(false);
        lineChartSales.setTouchEnabled(true);
        lineChartSales.setDragEnabled(true);
        lineChartSales.setScaleEnabled(true);
        lineChartSales.setPinchZoom(true);
        lineChartSales.setVisibility(View.GONE);

        XAxis xAxis = lineChartSales.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Buyers Bar Chart
        barChartBuyers.getDescription().setEnabled(false);
        barChartBuyers.setTouchEnabled(true);
        barChartBuyers.setDragEnabled(true);
        barChartBuyers.setScaleEnabled(true);
        barChartBuyers.setVisibility(View.GONE);

        XAxis xAxisBar = barChartBuyers.getXAxis();
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGranularity(1f);
    }

    private void loadSellerData() {
        db.collection("users").document(sellerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");

                        if (name != null) {
                            tvSellerName.setText("Welcome, " + name + "!");
                        }
                        if (email != null) {
                            tvSellerEmail.setText(email);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadStatistics() {
        // Load total products
        db.collection("items")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    tvTotalProducts.setText(String.valueOf(count));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading products count", Toast.LENGTH_SHORT).show();
                });

        // Load orders with revenue and buyer count
        db.collection("orders")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int total = queryDocumentSnapshots.size();
                    int pending = 0;
                    double revenue = 0;
                    List<String> buyerIds = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String status = doc.getString("status");
                        Double totalPrice = doc.getDouble("totalPrice");
                        String buyerId = doc.getString("buyerId");

                        if (status != null && status.equals("Pending")) {
                            pending++;
                        }

                        if (totalPrice != null && (status != null && status.equals("Delivered"))) {
                            revenue += totalPrice;
                        }

                        if (buyerId != null && !buyerIds.contains(buyerId)) {
                            buyerIds.add(buyerId);
                        }
                    }

                    tvTotalOrders.setText(String.valueOf(total));
                    tvPendingOrders.setText(String.valueOf(pending));
                    tvTotalRevenue.setText("GH₵ " + String.format("%.2f", revenue));
                    tvTotalBuyers.setText(String.valueOf(buyerIds.size()));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading orders count", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadNotifications() {
        db.collection("notifications")
                .whereEqualTo("sellerId", sellerId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) {
                        return;
                    }

                    notificationList.clear();
                    int unreadCount = 0;

                    for (QueryDocumentSnapshot doc : value) {
                        NotificationModel notification = doc.toObject(NotificationModel.class);
                        if (notification != null) {
                            notification.setNotificationId(doc.getId());
                            notificationList.add(notification);
                            if (!notification.isRead()) {
                                unreadCount++;
                            }
                        }
                    }

                    tvUnreadCount.setText(String.valueOf(unreadCount));
                    if (unreadCount > 0) {
                        tvUnreadCount.setVisibility(View.VISIBLE);
                    } else {
                        tvUnreadCount.setVisibility(View.GONE);
                    }

                    notificationAdapter.notifyDataSetChanged();
                });
    }

    private void markNotificationAsRead(NotificationModel notification) {
        db.collection("notifications")
                .document(notification.getNotificationId())
                .update("isRead", true)
                .addOnSuccessListener(aVoid -> {
                    notification.setRead(true);
                    notificationAdapter.notifyDataSetChanged();

                    int unread = 0;
                    for (NotificationModel n : notificationList) {
                        if (!n.isRead()) unread++;
                    }
                    tvUnreadCount.setText(String.valueOf(unread));
                    if (unread == 0) {
                        tvUnreadCount.setVisibility(View.GONE);
                    }
                });
    }

    private void loadSalesData() {
        Map<String, Double> dailySales = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

        for (int i = 6; i >= 0; i--) {
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DAY_OF_YEAR, -i);
            String date = dateFormat.format(day.getTime());
            dailySales.put(date, 0.0);
        }

        db.collection("orders")
                .whereEqualTo("sellerId", sellerId)
                .whereEqualTo("status", "Delivered")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Double totalPrice = doc.getDouble("totalPrice");
                        Long timestamp = doc.getLong("timestamp");

                        if (totalPrice != null && timestamp != null) {
                            Date date = new Date(timestamp);
                            String dateStr = dateFormat.format(date);

                            if (dailySales.containsKey(dateStr)) {
                                dailySales.put(dateStr, dailySales.get(dateStr) + totalPrice);
                            }
                        }
                    }
                    updateSalesChart(dailySales);
                });
    }

    private void updateSalesChart(Map<String, Double> dailySales) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Double> entry : dailySales.entrySet()) {
            entries.add(new Entry(index, entry.getValue().floatValue()));
            labels.add(entry.getKey());
            index++;
        }

        if (entries.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                entries.add(new Entry(i, 0));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Daily Sales (GH₵)");
        dataSet.setColor(getResources().getColor(android.R.color.holo_green_dark));
        dataSet.setCircleColor(getResources().getColor(android.R.color.holo_green_dark));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) ->
                "GH₵ " + String.format("%.0f", value));

        LineData lineData = new LineData(dataSet);
        lineChartSales.setData(lineData);
        lineChartSales.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChartSales.invalidate();
    }

    private void loadBuyerData() {
        Map<String, Integer> dailyBuyers = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

        for (int i = 6; i >= 0; i--) {
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DAY_OF_YEAR, -i);
            String date = dateFormat.format(day.getTime());
            dailyBuyers.put(date, 0);
        }

        db.collection("orders")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> processedBuyers = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String buyerId = doc.getString("buyerId");
                        Long timestamp = doc.getLong("timestamp");

                        if (buyerId != null && timestamp != null) {
                            Date date = new Date(timestamp);
                            String dateStr = dateFormat.format(date);

                            String key = dateStr + "_" + buyerId;
                            if (!processedBuyers.contains(key)) {
                                processedBuyers.add(key);
                                if (dailyBuyers.containsKey(dateStr)) {
                                    dailyBuyers.put(dateStr, dailyBuyers.get(dateStr) + 1);
                                }
                            }
                        }
                    }
                    updateBuyerChart(dailyBuyers);
                });
    }

    private void updateBuyerChart(Map<String, Integer> dailyBuyers) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entry : dailyBuyers.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue().floatValue()));
            labels.add(entry.getKey());
            index++;
        }

        if (entries.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                entries.add(new BarEntry(i, 0));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "New Buyers");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barChartBuyers.setData(barData);
        barChartBuyers.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChartBuyers.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
        loadNotifications();
    }
}