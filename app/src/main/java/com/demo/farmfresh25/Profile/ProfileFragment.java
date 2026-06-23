package com.demo.farmfresh25.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Authentification.Login;
import com.demo.farmfresh25.EditProfile.EditProfileActivity;
import com.demo.farmfresh25.OrderHistory.OrderHistoryActivity;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.Settings.SettingsActivity;
import com.demo.farmfresh25.ShoppingCart.ShoppingCartActivity;
import com.demo.farmfresh25.Wishlist.WishlistActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    // Views
    private ImageView profileImage;
    private TextView userName, userEmail, userSince, orderCountText, wishlistCountText;
    private CardView editProfileCard, ordersCard, cartCard, wishlistCard, settingsCard, helpCard;
    private Button logoutButton;
    private LinearLayout profileHeader;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        initializeViews(view);

        // Setup click listeners with full functionality
        setupClickListeners(view);

        // Load user data
        loadUserData();

        return view;
    }

    private void initializeViews(View view) {
        profileImage = view.findViewById(R.id.profileImage);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userSince = view.findViewById(R.id.userSince);
        orderCountText = view.findViewById(R.id.orderCountText);
        wishlistCountText = view.findViewById(R.id.wishlistCountText);
        profileHeader = view.findViewById(R.id.profileHeader);

        editProfileCard = view.findViewById(R.id.editProfileCard);
        ordersCard = view.findViewById(R.id.ordersCard);
        cartCard = view.findViewById(R.id.cartCard);
        wishlistCard = view.findViewById(R.id.wishlistCard);
        settingsCard = view.findViewById(R.id.settingsCard);
        helpCard = view.findViewById(R.id.helpCard);
        logoutButton = view.findViewById(R.id.logoutButton);
    }

    private void setupClickListeners(View view) {
        // 1. Edit Profile - Navigate to Edit Profile Activity
        if (editProfileCard != null) {
            editProfileCard.setOnClickListener(v -> {
                if (currentUser != null) {
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    intent.putExtra("userId", currentUser.getUid());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 2. My Orders - Navigate to Order History
        if (ordersCard != null) {
            ordersCard.setOnClickListener(v -> {
                if (currentUser != null) {
                    Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Please login to view orders", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 3. Cart - Navigate to Shopping Cart
        if (cartCard != null) {
            cartCard.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                startActivity(intent);
            });
        }

        // 4. Wishlist - Navigate to Wishlist
        if (wishlistCard != null) {
            wishlistCard.setOnClickListener(v -> {
                if (currentUser != null) {
                    Intent intent = new Intent(getActivity(), WishlistActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Please login to view wishlist", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 5. Settings - Navigate to Settings
        if (settingsCard != null) {
            settingsCard.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            });
        }

        // 6. Help & Support - Open email or support page
        if (helpCard != null) {
            helpCard.setOnClickListener(v -> {
                showHelpOptions();
            });
        }

        // 7. Logout - Show confirmation dialog
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                showLogoutConfirmationDialog();
            });
        }

        // 8. Profile Image - Change profile picture
        if (profileImage != null) {
            profileImage.setOnClickListener(v -> {
                if (currentUser != null) {
                    showChangeProfilePictureDialog();
                } else {
                    Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 9. Stats Cards Click - Navigate to Orders and Wishlist
        View orderStatsCard = view.findViewById(R.id.orderStatsCard);
        if (orderStatsCard != null) {
            orderStatsCard.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(intent);
            });
        }

        View wishlistStatsCard = view.findViewById(R.id.wishlistStatsCard);
        if (wishlistStatsCard != null) {
            wishlistStatsCard.setOnClickListener(v -> {
                if (currentUser != null) {
                    Intent intent = new Intent(getActivity(), WishlistActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Please login", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showHelpOptions() {
        String[] options = {"Email Support", "Call Support", "FAQ", "Report an Issue"};

        new AlertDialog.Builder(getContext())
                .setTitle("Help & Support")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Email
                            composeEmail();
                            break;
                        case 1: // Call
                            makePhoneCall();
                            break;
                        case 2: // FAQ
                            showFAQ();
                            break;
                        case 3: // Report Issue
                            reportIssue();
                            break;
                    }
                })
                .show();
    }

    private void composeEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:support@farmfresh25.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FarmFresh25 Support Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Support Team,\n\n");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+233123456789"));
        startActivity(callIntent);
    }

    private void showFAQ() {
        Toast.makeText(getContext(), "FAQ section coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void reportIssue() {
        Toast.makeText(getContext(), "Report issue feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void showChangeProfilePictureDialog() {
        String[] options = {"Take Photo", "Choose from Gallery", "Remove Photo"};

        new AlertDialog.Builder(getContext())
                .setTitle("Change Profile Picture")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Take Photo
                            Toast.makeText(getContext(), "Camera feature coming soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // Gallery
                            Toast.makeText(getContext(), "Gallery feature coming soon", Toast.LENGTH_SHORT).show();
                            break;
                        case 2: // Remove
                            Toast.makeText(getContext(), "Photo removed", Toast.LENGTH_SHORT).show();
                            profileImage.setImageResource(R.drawable.profile_placeholder);
                            break;
                    }
                })
                .show();
    }

    private void loadUserData() {
        if (currentUser != null) {
            // Set user name
            String name = currentUser.getDisplayName();
            if (name != null && !name.isEmpty()) {
                userName.setText(name);
            } else {
                userName.setText("User");
            }

            // Set user email
            String email = currentUser.getEmail();
            if (email != null && !email.isEmpty()) {
                userEmail.setText(email);
            } else {
                userEmail.setText("No email provided");
            }

            // Load profile image
            Uri photoUrl = currentUser.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.profile_placeholder)
                        .error(R.drawable.profile_placeholder)
                        .circleCrop()
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.profile_placeholder);
            }

            // Load user data from Firestore
            loadUserFromFirestore();
        } else {
            // User not logged in
            userName.setText("Guest");
            userEmail.setText("Not logged in");
            userSince.setText("Please login");
            profileImage.setImageResource(R.drawable.profile_placeholder);
            orderCountText.setText("0");
            wishlistCountText.setText("0");
        }
    }

    private void loadUserFromFirestore() {
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get user data
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        Long createdAt = documentSnapshot.getLong("createdAt");
                        String photoUrl = documentSnapshot.getString("photoUrl");

                        if (name != null && !name.isEmpty()) {
                            userName.setText(name);
                        }

                        if (email != null && !email.isEmpty()) {
                            userEmail.setText(email);
                        }

                        // Load profile image from Firestore if available
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(photoUrl)
                                    .placeholder(R.drawable.profile_placeholder)
                                    .error(R.drawable.profile_placeholder)
                                    .circleCrop()
                                    .into(profileImage);
                        }

                        // Set user since date
                        if (createdAt != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
                            String dateStr = sdf.format(new Date(createdAt));
                            userSince.setText("Member since " + dateStr);
                        } else {
                            userSince.setText("Member since 2024");
                        }
                    } else {
                        // User document doesn't exist, create one
                        createUserDocument();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading profile", Toast.LENGTH_SHORT).show();
                });

        // Load order count
        loadOrderCount();
        loadWishlistCount();
    }

    private void createUserDocument() {
        if (currentUser == null) return;

        java.util.Map<String, Object> userData = new java.util.HashMap<>();
        userData.put("name", currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "User");
        userData.put("email", currentUser.getEmail());
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("photoUrl", currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "");

        db.collection("users")
                .document(currentUser.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile created", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    private void loadOrderCount() {
        if (currentUser == null) return;

        db.collection("orders")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int orderCount = queryDocumentSnapshots.size();
                    orderCountText.setText(String.valueOf(orderCount));
                })
                .addOnFailureListener(e -> {
                    orderCountText.setText("0");
                });
    }

    private void loadWishlistCount() {
        if (currentUser == null) return;

        db.collection("wishlist")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int wishlistCount = queryDocumentSnapshots.size();
                    wishlistCountText.setText(String.valueOf(wishlistCount));
                })
                .addOnFailureListener(e -> {
                    wishlistCountText.setText("0");
                });
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performLogout() {
        if (mAuth != null) {
            mAuth.signOut();
        }

        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to Login
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data when returning to profile
        loadUserData();
    }
}