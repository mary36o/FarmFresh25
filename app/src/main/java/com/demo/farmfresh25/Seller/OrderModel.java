package com.demo.farmfresh25.Seller;

public class OrderModel {
    private String orderId;
    private String productName;
    private String buyerName;
    private int quantity;
    private double totalPrice;
    private long timestamp;
    private String status;

    public OrderModel() {
        // Default constructor required for Firestore
    }

    public OrderModel(String orderId, String productName, String buyerName,
                      int quantity, double totalPrice, long timestamp, String status) {
        this.orderId = orderId;
        this.productName = productName;
        this.buyerName = buyerName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getProductName() { return productName; }
    public String getBuyerName() { return buyerName; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public long getTimestamp() { return timestamp; }
    public String getStatus() { return status; }

    // Setters
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setStatus(String status) { this.status = status; }
}