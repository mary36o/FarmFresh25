package com.demo.farmfresh25.crud;
import android.text.TextUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

    public class ProductAdminModel {
        private String id;
        private String name;
        private String description;
        private String price;
        private String quantity;
        private String image;
        private String category;
        private String subCategory;
        private String brand;
        private String sku; // Stock Keeping Unit
        private double discountPercentage;
        private String taxRate;
        private boolean inStock;
        private boolean isFeatured;
        private String weight;

        private String productCode;
        private String unit; // kg, g, lb, piece, dozen
        private String minOrderQuantity;
        private String maxOrderQuantity;
        private Date createdAt;
        private Date updatedAt;
        private String sellerName;
        private String sellerId;
        private String barcode;
        private Map<String, String> specifications;
        private int stockAlertThreshold;
        private String expiryDate;
        private String manufacturingDate;
        private String storageInstructions;
        private String returnPolicy;
        private int rating;
        private int reviewCount;
        private String tags; // Comma separated tags
        private boolean isOrganic;
        private boolean isVegan;
        private boolean isGlutenFree;

        // Empty constructor required for Firestore
        public ProductAdminModel(String productCode) {
            this.productCode = productCode;
            this.createdAt = new Date();
            this.updatedAt = new Date();
            this.inStock = true;
            this.isFeatured = false;
            this.rating = 0;
            this.reviewCount = 0;
            this.discountPercentage = 0;
            this.stockAlertThreshold = 10;
            this.specifications = new HashMap<>();
        }

        // Full constructor
        public ProductAdminModel(String productId, String name, String description, String price,
                                 String quantity, String imageUrl, String category, String subCategory,
                                 String brand, String sku, double discountPercentage, String taxRate,
                                 boolean inStock, boolean isFeatured, String weight, String productCode, String unit,
                                 String minOrderQuantity, String maxOrderQuantity, String sellerName,
                                 String sellerId, String barcode, int stockAlertThreshold,
                                 String expiryDate, String manufacturingDate, String storageInstructions,
                                 String returnPolicy, String tags, boolean isOrganic, boolean isVegan,
                                 boolean isGlutenFree) {
            this.id = productId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.image = imageUrl;
            this.category = category;
            this.subCategory = subCategory;
            this.brand = brand;
            this.sku = sku;
            this.discountPercentage = discountPercentage;
            this.taxRate = taxRate;
            this.inStock = inStock;
            this.isFeatured = isFeatured;
            this.weight = weight;
            this.productCode = productCode;
            this.unit = unit;
            this.minOrderQuantity = minOrderQuantity;
            this.maxOrderQuantity = maxOrderQuantity;
            this.sellerName = sellerName;
            this.sellerId = sellerId;
            this.barcode = barcode;
            this.stockAlertThreshold = stockAlertThreshold;
            this.expiryDate = expiryDate;
            this.manufacturingDate = manufacturingDate;
            this.sku = sku;
            this.storageInstructions = storageInstructions;
            this.returnPolicy = returnPolicy;
            this.createdAt = new Date();
            this.updatedAt = new Date();
            this.tags = tags;
            this.isOrganic = isOrganic;
            this.isVegan = isVegan;
            this.isGlutenFree = isGlutenFree;
            this.rating = 0;
            this.reviewCount = 0;
            this.specifications = new HashMap<>();
        }


        public ProductAdminModel(String id, String name, String price, String image, String description, double v, String productCode) {
            this.productCode = productCode;
        }







        public ProductAdminModel(String image, String name, String productCode) {
            this.image = image;
            this.name = name;
            this.productCode = productCode;
        }


        public ProductAdminModel(String name, String price, String image, String description, String productCode) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.description = description;
            this.productCode = productCode;
            this.category = category;
        }
        public ProductAdminModel(String name, String price, String image, String productCode) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.productCode = productCode;
        }

        public ProductAdminModel(String id, String name, String price, String image, String category, String productCode) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.category = category;
            this.id = id;
            this.productCode = productCode;
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String productId) { this.id = productId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }

        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }

        public String getImage() { return image; }
        public void setImage(String imageUrl) { this.image = imageUrl; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getSubCategory() { return subCategory; }
        public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public double getDiscountPercentage() { return discountPercentage; }
        public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

        public String getTaxRate() { return taxRate; }
        public void setTaxRate(String taxRate) { this.taxRate = taxRate; }

        public boolean isInStock() { return inStock; }
        public void setInStock(boolean inStock) { this.inStock = inStock; }

        public boolean isFeatured() { return isFeatured; }
        public void setFeatured(boolean featured) { isFeatured = featured; }

        public String getWeight() { return weight; }
        public void setWeight(String weight) { this.weight = weight; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        public String getMinOrderQuantity() { return minOrderQuantity; }
        public void setMinOrderQuantity(String minOrderQuantity) { this.minOrderQuantity = minOrderQuantity; }

        public String getMaxOrderQuantity() { return maxOrderQuantity; }
        public void setMaxOrderQuantity(String maxOrderQuantity) { this.maxOrderQuantity = maxOrderQuantity; }

        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

        public Date getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

        public String getSellerName() { return sellerName; }
        public void setSellerName(String sellerName) { this.sellerName = sellerName; }

        public String getSellerId() { return sellerId; }
        public void setSellerId(String sellerId) { this.sellerId = sellerId; }

        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }

        public Map<String, String> getSpecifications() { return specifications; }
        public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }

        public int getStockAlertThreshold() { return stockAlertThreshold; }
        public void setStockAlertThreshold(int stockAlertThreshold) { this.stockAlertThreshold = stockAlertThreshold; }

        public String getExpiryDate() { return expiryDate; }
        public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

        public String getManufacturingDate() { return manufacturingDate; }
        public void setManufacturingDate(String manufacturingDate) { this.manufacturingDate = manufacturingDate; }

        public String getStorageInstructions() { return storageInstructions; }
        public void setStorageInstructions(String storageInstructions) { this.storageInstructions = storageInstructions; }

        public String getReturnPolicy() { return returnPolicy; }
        public void setReturnPolicy(String returnPolicy) { this.returnPolicy = returnPolicy; }

        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }

        public int getReviewCount() { return reviewCount; }
        public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }

        public boolean isOrganic() { return isOrganic; }
        public void setOrganic(boolean organic) { isOrganic = organic; }

        public boolean isVegan() { return isVegan; }
        public void setVegan(boolean vegan) { isVegan = vegan; }

        public boolean isGlutenFree() { return isGlutenFree; }
        public void setGlutenFree(boolean glutenFree) { isGlutenFree = glutenFree; }

        // Helper methods
        public double getDiscountedPrice() {
            double originalPrice = Double.parseDouble(price);
            return originalPrice - (originalPrice * discountPercentage / 100);
        }

        public String getFormattedPrice() {
            return String.format("$%.2f", Double.parseDouble(price));
        }

        public String getFormattedDiscountedPrice() {
            if (discountPercentage > 0) {
                return String.format("$%.2f", getDiscountedPrice());
            }
            return getFormattedPrice();
        }

        public boolean isLowStock() {
            try {
                int currentStock = Integer.parseInt(quantity);
                return currentStock <= stockAlertThreshold;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public boolean isValid() {
            return !TextUtils.isEmpty(name) &&
                    !TextUtils.isEmpty(price) &&
                    !TextUtils.isEmpty(quantity) &&
                    !TextUtils.isEmpty(image) &&
                    !TextUtils.isEmpty(category);
        }
    }





