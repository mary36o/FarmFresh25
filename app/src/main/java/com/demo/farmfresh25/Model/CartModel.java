package com.demo.farmfresh25.Model;

public class CartModel {

    private String id;
    private String name;
    private String price;
    private String image;
    private int quantity;

    public CartModel() {
        // Required empty constructor for Firestore
    }

    public CartModel(String id, String name, String price, String image, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    // Constructor without id (optional)
    public CartModel(String name, String price, String image, int quantity) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}