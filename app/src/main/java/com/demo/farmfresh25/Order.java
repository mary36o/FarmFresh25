package com.demo.farmfresh25;

import com.demo.farmfresh25.Seller.CartItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private List<CartItem> items;
    private double totalAmount;
    private String orderStatus;
    private String shippingAddress;
    private String paymentMethod;
    private long orderDate;
    private String deliveryDate;

    public Order() {}

    public Order(String orderId, String userId, List<CartItem> items,
                 double totalAmount, String orderStatus, String shippingAddress,
                 String paymentMethod, long orderDate, String deliveryDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    public Order(String orderId, String userId, ArrayList<CartItem> cartItems, double totalAmount, String pending, String shippingAddress, String cashOnDelivery, long orderDate, String deliveryDate) {
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }
    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
}