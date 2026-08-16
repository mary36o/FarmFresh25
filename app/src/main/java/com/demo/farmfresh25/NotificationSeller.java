package com.demo.farmfresh25;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Seller.NotificationModel;
import com.demo.farmfresh25.Seller.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notifications");
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        rvNotifications = findViewById(R.id.rvNotifications);
        progressBar = findViewById(R.id.progressBar);
        llEmptyState = findViewById(R.id.llEmptyState);
        tvEmptyText = findViewById(R.id.tvEmptyText);
    }
 
    private void setupRecyclerView() {
        adapter = new NotificationAdapter(this, notificationList,
                notification -> markNotificationAsRead(notification));
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);
    }

    private void loadNotifications() {
        progressBar.setVisibility(View.VISIBLE);
        rvNotifications.setVisibility(View.GONE);
        llEmptyState.setVisibility(View.GONE);

        db.collection("notifications")
                .whereEqualTo("sellerId", sellerId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    notificationList.clear();

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            NotificationModel notification = doc.toObject(NotificationModel.class);
                            if (notification != null) {
                                notification.setNotificationId(doc.getId());
                                notificationList.add(notification);
                            }
                        }

                        Collections.sort(notificationList, (a, b) -> {
                            try {
                                long timeA = Long.parseLong(a.getTimestamp());
                                long timeB = Long.parseLong(b.getTimestamp());
                                return Long.compare(timeB, timeA);
                            } catch (NumberFormatException e) {
                                return 0;
                            }
                        });

                        rvNotifications.setVisibility(View.VISIBLE);
                        llEmptyState.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        rvNotifications.setVisibility(View.GONE);
                        llEmptyState.setVisibility(View.VISIBLE);
                        tvEmptyText.setText("No notifications yet");
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    rvNotifications.setVisibility(View.GONE);
                    llEmptyState.setVisibility(View.VISIBLE);
                    tvEmptyText.setText("Failed to load notifications");
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
}