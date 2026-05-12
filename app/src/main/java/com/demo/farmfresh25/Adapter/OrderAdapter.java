package com.demo.farmfresh25.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.R;

import java.util.List;

public class OrderAdapter {






    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, amount, status, date;
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            cardView = itemView.findViewById(R.id.orderCard);
        }
    }
}