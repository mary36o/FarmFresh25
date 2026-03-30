package com.demo.farmfresh25.Model;

public class ProductModel {


        String name, price, image, description, category;



        public ProductModel() {
        }

    public ProductModel(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public ProductModel(String name, String price, String image) {
            this.name = name;
            this.price = price;
            this.image = image;
        }

    public ProductModel(String name, String price, String image, String description) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }


    public String getCategory() { return category; }
    public String getName() { return name; }
        public String getPrice() { return price; }
        public String getImage() { return image; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}