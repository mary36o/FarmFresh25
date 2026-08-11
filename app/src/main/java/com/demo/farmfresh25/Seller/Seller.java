package com.demo.farmfresh25.Seller;


import java.io.Serializable;

public class Seller implements Serializable {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String storeName;
    private String imageUrl;
    private long createdAt;

    public Seller(String id, String name, String email, String phone,
                  String address, String storeName, String imageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.storeName = storeName;
        this.imageUrl = imageUrl;
        this.createdAt = System.currentTimeMillis();
    }

    public Seller( ) {
    }

    public Seller(String sellerId, String sellerName, String email, String phone, String imageUrl, String shopName, String shopAddress, long l) {
    }


    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
