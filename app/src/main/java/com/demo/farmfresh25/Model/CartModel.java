package com.demo.farmfresh25.Model;

public class CartModel {

    String name, price, image;

    public CartModel() {
    }

    public CartModel(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
}