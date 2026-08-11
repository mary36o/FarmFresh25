package com.demo.farmfresh25.Seller;

public class Order {
    private String orderId;
    private String sellerId;
    private String buyerId;
    private String buyerName;
    private String productId;
    private String productName;
    private double price;
    private int quantity;
    private double totalPrice;
    private String orderDate;
    private String deliveryAddress;
    private String status; // Pending, Accepted, Preparing, Shipped, Delivered, Cancelled
    private String paymentMethod;
    private String paymentStatus;
    private long timestamp;

    public Order() {
        // Empty constructor for Firestore
    }

    public Order(String orderId, String sellerId, String buyerId, String buyerName,
                 String productId, String productName, double price, int quantity,
                 double totalPrice, String deliveryAddress) {
        this.orderId = orderId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.status = "Pending";
        this.paymentStatus = "Pending";
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getBuyerId() { return buyerId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}