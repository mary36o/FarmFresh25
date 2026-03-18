package com.demo.farmfresh25;

public class ProductModel {


        String name;
        String price;
        int image;

        public ProductModel(String name, String price, int image) {
            this.name = name;
            this.price = price;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public int getImage() {
            return image;
        }

}
