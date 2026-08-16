package com.demo.farmfresh25.Seller;
public class SalesData {
    private String date;
    private double amount;
    private int orderCount;

    public SalesData() {
        // Empty constructor for Firestore
    }

    public SalesData(String date, double amount, int orderCount) {
        this.date = date;
        this.amount = amount;
        this.orderCount = orderCount;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
}