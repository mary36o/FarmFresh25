package com.demo.farmfresh25.ui.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceholderItem createPlaceholderItem(int position) {
        return new PlaceholderItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String content;
        public final String details;

        public PlaceholderItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

//
//
//<!--<?xml version="1.0" encoding="utf-8"?>-->
//<!--<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"-->
//<!--    xmlns:app="http://schemas.android.com/apk/res-auto"-->
//<!--    android:layout_width="match_parent"-->
//<!--    android:layout_height="match_parent"-->
//<!--    android:background="@color/background_color"-->
//<!--    android:fillViewport="true">-->
//
//<!--    <LinearLayout-->
//<!--        android:layout_width="match_parent"-->
//<!--        android:layout_height="wrap_content"-->
//<!--        android:orientation="vertical">-->
//
//<!--        &lt;!&ndash; Product Image Section &ndash;&gt;-->
//<!--        <FrameLayout-->
//<!--            android:layout_width="match_parent"-->
//<!--            android:layout_height="300dp"-->
//<!--            android:background="@color/card_background">-->
//
//<!--            <ImageView-->
//<!--                android:id="@+id/detailImage"-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="match_parent"-->
//<!--                android:scaleType="centerCrop"-->
//<!--                android:src="@drawable/image111" />-->
//
//<!--            &lt;!&ndash; Back Button &ndash;&gt;-->
//<!--            <ImageView-->
//<!--                android:id="@+id/btnBack"-->
//<!--                android:layout_width="40dp"-->
//<!--                android:layout_height="40dp"-->
//<!--                android:layout_margin="16dp"-->
//<!--                android:background="@drawable/outline_arrow_back_24"-->
//<!--                android:padding="8dp"-->
//<!--                android:src="@drawable/outline_arrow_back_24"-->
//<!--                app:tint="@color/white" />-->
//
//<!--            &lt;!&ndash; Favorite Button &ndash;&gt;-->
//<!--            <ImageView-->
//<!--                android:id="@+id/btnFavorite"-->
//<!--                android:layout_width="40dp"-->
//<!--                android:layout_height="40dp"-->
//<!--                android:layout_gravity="end"-->
//<!--                android:layout_margin="16dp"-->
//<!--                android:background="@drawable/favorite_24dp_ea3323_fill0_wght400_grad0_opsz24"-->
//<!--                android:padding="8dp"-->
//<!--                android:src="@drawable/outline_arrow_back_24"-->
//<!--                app:tint="@color/white" />-->
//
//<!--        </FrameLayout>-->
//
//<!--        &lt;!&ndash; Product Details Section &ndash;&gt;-->
//<!--        <LinearLayout-->
//<!--            android:layout_width="match_parent"-->
//<!--            android:layout_height="wrap_content"-->
//<!--            android:orientation="vertical"-->
//<!--            android:padding="20dp">-->
//
//<!--            &lt;!&ndash; Product Name &ndash;&gt;-->
//<!--            <TextView-->
//<!--                android:id="@+id/detailName"-->
//<!--                android:layout_width="wrap_content"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:text="Product Name"-->
//<!--                android:textColor="@color/text_primary"-->
//<!--                android:textSize="24sp"-->
//<!--                android:textStyle="bold"-->
//<!--                android:layout_marginBottom="8dp" />-->
//
//<!--            &lt;!&ndash; Rating Section &ndash;&gt;-->
//<!--            <LinearLayout-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:orientation="horizontal"-->
//<!--                android:layout_marginBottom="12dp">-->
//
//<!--                <LinearLayout-->
//<!--                    android:layout_width="wrap_content"-->
//<!--                    android:layout_height="wrap_content"-->
//<!--                    android:orientation="horizontal">-->
//
//<!--                    <ImageView-->
//<!--                        android:layout_width="16dp"-->
//<!--                        android:layout_height="16dp"-->
//<!--                        android:src="@drawable/star_rate_24dp_ea3323_fill0_wght400_grad0_opsz24"-->
//<!--                        app:tint="@color/colorError" />-->
//
//<!--                    <ImageView-->
//<!--                        android:layout_width="16dp"-->
//<!--                        android:layout_height="16dp"-->
//<!--                        android:src="@drawable/star_half_24dp_000000_fill0_wght400_grad0_opsz24"-->
//<!--                        app:tint="@color/colorError" />-->
//
//<!--                    <ImageView-->
//<!--                        android:layout_width="16dp"-->
//<!--                        android:layout_height="16dp"-->
//<!--                        android:src="@drawable/star_half_24dp_000000_fill0_wght400_grad0_opsz24"-->
//<!--                        app:tint="@color/colorError" />-->
//
//<!--                    <ImageView-->
//<!--                        android:layout_width="16dp"-->
//<!--                        android:layout_height="16dp"-->
//<!--                        android:src="@drawable/star_rate_24dp_ea3323_fill0_wght400_grad0_opsz24"-->
//<!--                        app:tint="@color/colorError" />-->
//
//<!--                    <ImageView-->
//<!--                        android:layout_width="16dp"-->
//<!--                        android:layout_height="16dp"-->
//<!--                        android:src="@drawable/star_rate_24dp_ea3323_fill0_wght400_grad0_opsz24"-->
//<!--                        app:tint="@color/colorError" />-->
//
//<!--                </LinearLayout>-->
//
//<!--                <TextView-->
//<!--                    android:layout_width="wrap_content"-->
//<!--                    android:layout_height="wrap_content"-->
//<!--                    android:text=" 4.5 (120 reviews)"-->
//<!--                    android:textColor="@color/secondary_text"-->
//<!--                    android:textSize="12sp"-->
//<!--                    android:layout_marginStart="8dp" />-->
//<!--            </LinearLayout>-->
//
//<!--            &lt;!&ndash; Price Section &ndash;&gt;-->
//<!--            <LinearLayout-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:orientation="horizontal"-->
//<!--                android:gravity="center_vertical"-->
//<!--                android:layout_marginBottom="16dp">-->
//
//<!--                <TextView-->
//<!--                    android:id="@+id/detailPrice"-->
//<!--                    android:layout_width="wrap_content"-->
//<!--                    android:layout_height="wrap_content"-->
//<!--                    android:text="GHS 0.00"-->
//<!--                    android:textColor="@color/green_price"-->
//<!--                    android:textSize="28sp"-->
//<!--                    android:textStyle="bold" />-->
//
//<!--&lt;!&ndash;                <TextView&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:layout_width="wrap_content"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:layout_height="wrap_content"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:text="GHS 0.00"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:textColor="@color/secondary_text"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:textSize="16sp"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:textStyle="strike_through"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:layout_marginStart="12dp" />&ndash;&gt;-->
//
//<!--&lt;!&ndash;                <TextView&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:layout_width="wrap_content"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:layout_height="wrap_content"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:layout_marginStart="8dp"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:paddingHorizontal="8dp"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:paddingVertical="2dp"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:text="20% OFF"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:textColor="@color/white"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:textSize="12sp"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:textStyle="bold"&ndash;&gt;-->
//<!--&lt;!&ndash;                    android:background="@drawable/sale_badge" />&ndash;&gt;-->
//<!--            </LinearLayout>-->
//
//<!--            &lt;!&ndash; Divider &ndash;&gt;-->
//<!--            <View-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="1dp"-->
//<!--                android:background="@color/divider_color"-->
//<!--                android:layout_marginVertical="16dp" />-->
//
//<!--            &lt;!&ndash; Quantity Selector Section &ndash;&gt;-->
//<!--            <TextView-->
//<!--                android:layout_width="wrap_content"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:text="Quantity"-->
//<!--                android:textColor="@color/text_primary"-->
//<!--                android:textSize="16sp"-->
//<!--                android:textStyle="bold"-->
//<!--                android:layout_marginBottom="12dp" />-->
//
//<!--            <LinearLayout-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:orientation="horizontal"-->
//<!--                android:gravity="center_vertical"-->
//<!--                android:layout_marginBottom="24dp">-->
//
//<!--                <TextView-->
//<!--                    android:layout_width="0dp"-->
//<!--                    android:layout_height="wrap_content"-->
//<!--                    android:layout_weight="1"-->
//<!--                    android:text="Select quantity:"-->
//<!--                    android:textColor="@color/secondary_text"-->
//<!--                    android:textSize="14sp" />-->
//
//<!--                <LinearLayout-->
//<!--                    android:layout_width="wrap_content"-->
//<!--                    android:layout_height="wrap_content"-->
//<!--                    android:background="@drawable/maize"-->
//<!--                    android:orientation="horizontal">-->
//
//<!--                    <Button-->
//<!--                        android:id="@+id/btnMinus"-->
//<!--                        android:layout_width="40dp"-->
//<!--                        android:layout_height="40dp"-->
//<!--                        android:background="@null"-->
//<!--                        android:stateListAnimator="@null"-->
//<!--                        android:text="-"-->
//<!--                        android:textColor="@color/colorOnError"-->
//<!--                        android:textSize="20sp" />-->
//
//<!--                    <TextView-->
//<!--                        android:id="@+id/txtQuantity"-->
//<!--                        android:layout_width="50dp"-->
//<!--                        android:layout_height="40dp"-->
//<!--                        android:gravity="center"-->
//<!--                        android:text="1"-->
//<!--                        android:textColor="@color/text_primary"-->
//<!--                        android:textSize="16sp"-->
//<!--                        android:textStyle="bold" />-->
//
//<!--                    <Button-->
//<!--                        android:id="@+id/btnPlus"-->
//<!--                        android:layout_width="40dp"-->
//<!--                        android:layout_height="40dp"-->
//<!--                        android:background="@null"-->
//<!--                        android:stateListAnimator="@null"-->
//<!--                        android:text="+"-->
//<!--                        android:textColor="@color/colorOnError"-->
//<!--                        android:textSize="20sp" />-->
//
//<!--                </LinearLayout>-->
//
//<!--            </LinearLayout>-->
//
//<!--            &lt;!&ndash; Product Description &ndash;&gt;-->
//<!--            <TextView-->
//<!--                android:layout_width="wrap_content"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:text="Description"-->
//<!--                android:textColor="@color/text_primary"-->
//<!--                android:textSize="16sp"-->
//<!--                android:textStyle="bold"-->
//<!--                android:layout_marginBottom="8dp" />-->
//
//<!--            <TextView-->
//<!--                android:layout_width="wrap_content"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:text="Fresh organic vegetables directly from local farms. Grown without pesticides and harvested at peak ripeness. Perfect for healthy meals and guaranteed freshness."-->
//<!--                android:textColor="@color/secondary_text"-->
//<!--                android:textSize="14sp"-->
//<!--                android:lineSpacingExtra="4dp"-->
//<!--                android:layout_marginBottom="24dp" />-->
//
//<!--            &lt;!&ndash; Delivery Info Card &ndash;&gt;-->
//<!--            <com.google.android.material.card.MaterialCardView-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="wrap_content"-->
//<!--                android:layout_marginBottom="24dp"-->
//<!--                app:cardCornerRadius="12dp"-->
//<!--                app:cardElevation="2dp"-->
//<!--                app:cardBackgroundColor="@color/card_background">-->
//
//<!--                <LinearLayout-->
//<!--                    android:layout_width="match_parent"-->
//<!--                    android:layout_height="wrap_content"-->
//<!--                    android:orientation="vertical"-->
//<!--                    android:padding="16dp">-->
//
//<!--                    <LinearLayout-->
//<!--                        android:layout_width="match_parent"-->
//<!--                        android:layout_height="wrap_content"-->
//<!--                        android:orientation="horizontal"-->
//<!--                        android:layout_marginBottom="12dp">-->
//
//<!--                        <ImageView-->
//<!--                            android:layout_width="20dp"-->
//<!--                            android:layout_height="20dp"-->
//<!--                            android:src="@drawable/delivery_truck_bolt_24dp_000000_fill0_wght400_grad0_opsz24"-->
//<!--                            app:tint="@color/primary_green" />-->
//
//<!--                        <TextView-->
//<!--                            android:layout_width="wrap_content"-->
//<!--                            android:layout_height="wrap_content"-->
//<!--                            android:text="Free Delivery"-->
//<!--                            android:textColor="@color/text_primary"-->
//<!--                            android:textSize="14sp"-->
//<!--                            android:textStyle="bold"-->
//<!--                            android:layout_marginStart="8dp" />-->
//<!--                    </LinearLayout>-->
//
//<!--                    <LinearLayout-->
//<!--                        android:layout_width="match_parent"-->
//<!--                        android:layout_height="wrap_content"-->
//<!--                        android:orientation="horizontal">-->
//
//<!--                        <ImageView-->
//<!--                            android:layout_width="20dp"-->
//<!--                            android:layout_height="20dp"-->
//<!--                            android:src="@drawable/assignment_return_24dp_000000_fill0_wght400_grad0_opsz24"-->
//<!--                            app:tint="@color/primary_green" />-->
//
//<!--                        <TextView-->
//<!--                            android:layout_width="wrap_content"-->
//<!--                            android:layout_height="wrap_content"-->
//<!--                            android:text="30 Days Return Policy"-->
//<!--                            android:textColor="@color/text_primary"-->
//<!--                            android:textSize="14sp"-->
//<!--                            android:textStyle="bold"-->
//<!--                            android:layout_marginStart="8dp" />-->
//<!--                    </LinearLayout>-->
//
//<!--                </LinearLayout>-->
//
//<!--            </com.google.android.material.card.MaterialCardView>-->
//
//<!--            &lt;!&ndash; Add to Cart Button &ndash;&gt;-->
//<!--            <com.google.android.material.button.MaterialButton-->
//<!--                android:id="@+id/addToCart"-->
//<!--                android:layout_width="match_parent"-->
//<!--                android:layout_height="56dp"-->
//<!--                android:text="ADD TO CART"-->
//<!--                android:textAllCaps="true"-->
//<!--                android:textSize="16sp"-->
//<!--                android:textColor="@color/white"-->
//<!--                app:cornerRadius="12dp"-->
//<!--                app:backgroundTint="@color/primary_green"-->
//<!--                android:stateListAnimator="@null" />-->
//
//<!--        </LinearLayout>-->
//
//<!--    </LinearLayout>-->
//
//<!--</ScrollView>-->
