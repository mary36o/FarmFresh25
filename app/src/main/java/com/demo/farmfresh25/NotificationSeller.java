package com.demo.farmfresh25;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Seller.NotificationModel;
import com.demo.farmfresh25.Seller.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationSeller extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private ProgressBar progressBar;
    private LinearLayout llEmptyState;
    private TextView tvEmptyText;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_seller);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            sellerId = auth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        notificationList = new ArrayList<>();
        initViews();
        setupRecyclerView();
        loadNotifications();
    }

    private void initViews() {
        rvNotifications = findViewById(R.id.rvNotifications);
        progressBar = findViewById(R.id.progressBar);
        llEmptyState = findViewById(R.id.llEmptyState);
        tvEmptyText = findViewById(R.id.tvEmptyText);
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(this, notificationList,
                notification -> {
                    markNotificationAsRead(notification);
                });
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);
    }

    private void loadNotifications() {
        progressBar.setVisibility(View.VISIBLE);
        rvNotifications.setVisibility(View.GONE);
        llEmptyState.setVisibility(View.GONE);

        db.collection("notifications")
                .whereEqualTo("sellerId", sellerId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    progressBar.setVisibility(View.GONE);

                    if (error != null) {
                        Toast.makeText(this, "Error loading notifications: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    notificationList.clear();

                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            NotificationModel notification = doc.toObject(NotificationModel.class);
                            if (notification != null) {
                                notification.setNotificationId(doc.getId());
                                notificationList.add(notification);
                            }
                        }
                        rvNotifications.setVisibility(View.VISIBLE);
                        llEmptyState.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        rvNotifications.setVisibility(View.GONE);
                        llEmptyState.setVisibility(View.VISIBLE);
                        tvEmptyText.setText("No notifications yet");
                    }
                });
    }

    private void markNotificationAsRead(NotificationModel notification) {
        db.collection("notifications")
                .document(notification.getNotificationId())
                .update("isRead", true)
                .addOnSuccessListener(aVoid -> {
                    notification.setRead(true);
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }
}