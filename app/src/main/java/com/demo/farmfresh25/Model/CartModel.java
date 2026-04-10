package com.demo.farmfresh25.Model;

public class CartModel {

//    String name, price, image,  int quantity;
    String name, price, image;
    int quantity;

    public CartModel() {
    }

    public CartModel(String name, String price, String image ,int quantity) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public int getQuantity() { return quantity; }
}


