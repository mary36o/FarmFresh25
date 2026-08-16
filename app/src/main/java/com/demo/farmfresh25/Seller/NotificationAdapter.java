package com.demo.farmfresh25.Seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Seller.NotificationModel;
import com.demo.farmfresh25.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<NotificationModel> notificationList;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationModel notification);
    }

    public NotificationAdapter(Context context, List<NotificationModel> notificationList,
                               OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);

        holder.tvTitle.setText(notification.getTitle());
        holder.tvMessage.setText(notification.getMessage());
        holder.tvType.setText(notification.getType().toUpperCase());

        // Set time
        try {
            long time = Long.parseLong(notification.getTimestamp());
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(time));
            holder.tvTime.setText(date);
        } catch (Exception e) {
            holder.tvTime.setText("Just now");
        }

        // Set read/unread indicator
        if (notification.isRead()) {
            holder.viewUnread.setVisibility(View.GONE);
        } else {
            holder.viewUnread.setVisibility(View.VISIBLE);
        }

        // Set type icon/color
        switch (notification.getType()) {
            case "order":
                holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "product":
                holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                break;
            case "review":
                holder.tvType.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            default:
                holder.tvType.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvType, tvTime;
        View viewUnread;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvType = itemView.findViewById(R.id.tvNotificationType);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
            viewUnread = itemView.findViewById(R.id.viewUnread);
        }
    }
}