package com.demo.farmfresh25.Model;

public class Category {
    private String id, name, image, color;

    public Category(String id, String name, String image, String color) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.color = color;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public String getColor() { return color; }
}