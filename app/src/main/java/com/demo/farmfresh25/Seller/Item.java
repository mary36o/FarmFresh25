package com.demo.farmfresh25.Seller;

public class Item {
    private String id;
    private String sellerId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private int quantity;
    private long timestamp;

    // Empty constructor required for Firestore
    public Item() {
    }

    // Constructor with all fields
    public Item(String id, String sellerId, String name, String description,
                double price, String category, String imageUrl, int quantity) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public String getId() { return id; }
    public String getSellerId() { return sellerId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}