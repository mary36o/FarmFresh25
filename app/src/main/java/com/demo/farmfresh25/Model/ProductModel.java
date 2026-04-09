package com.demo.farmfresh25.Model;

public class ProductModel {


        String name;
    String price;

    String category;
    String id;

    String image;
    String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





        public ProductModel() {
        }

    public ProductModel(String image, String name) {
        this.image = image;
        this.name = name;
    }


    public ProductModel(String name, String price, String image, String description) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.category = category;
    }
    public ProductModel(String name, String price, String image) {
            this.name = name;
            this.price = price;
            this.image = image;
        }

    public ProductModel(String id, String name, String price, String image,String category) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
        this.id = id;
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