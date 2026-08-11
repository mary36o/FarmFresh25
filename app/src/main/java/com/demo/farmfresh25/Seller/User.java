package com.demo.farmfresh25.Seller;

// models/User.java
public class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String role; // "buyer" or "seller"
    private String shopName;
    private String shopDescription;
    private String phoneNumber;

    // Constructors
    public User() {}

    public User(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getShopDescription() { return shopDescription; }
    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isSeller() {
        return "seller".equalsIgnoreCase(role);
    }
}