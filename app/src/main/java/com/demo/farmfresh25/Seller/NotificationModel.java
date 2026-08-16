package com.demo.farmfresh25.Seller;

public class NotificationModel {
    private String notificationId;
    private String title;
    private String message;
    private String type; // "order", "product", "review", "system"
    private String timestamp;
    private boolean isRead;
    private String orderId;
    private String buyerId;

    public NotificationModel() {
        // Empty constructor for Firestore
    }

    public NotificationModel(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = false;
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
}