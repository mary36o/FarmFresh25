package com.demo.farmfresh25.Seller;

public class OrderModel {
    private String orderId;
    private String customerName;
    private String phone;
    private String email;
    private String address;
    private String paymentMethod;
    private double totalAmount;
    private double deliveryFee;
    private long timestamp;
    private String status;

    public OrderModel() {
        // Default constructor required for Firestore
    }

    public OrderModel(String orderId, String customerName, String phone, String email,
                      String address, String paymentMethod, double totalAmount,
                      double deliveryFee, long timestamp, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.deliveryFee = deliveryFee;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getTotalAmount() { return totalAmount; }
    public double getDeliveryFee() { return deliveryFee; }
    public long getTimestamp() { return timestamp; }
    public String getStatus() { return status; }

    // Setters
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setStatus(String status) { this.status = status; }
}
