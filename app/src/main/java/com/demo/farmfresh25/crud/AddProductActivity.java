package com.demo.farmfresh25.crud;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.farmfresh25.Model.ProductModel;
import com.demo.farmfresh25.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    // Basic Information
    private TextInputEditText editTextName, editProductCode,editTextDescription, editTextPrice, editTextQuantity;

    // Category & Brand - Now as AutoCompleteTextView
    private AutoCompleteTextView editTextCategory, editTextSubCategory, editTextBrand;

    // Pricing & Discount
    private TextInputEditText editTextDiscount, editTextTaxRate, editTextSKU, editTextBarcode;

    // Shipping & Weight
    private TextInputEditText editTextWeight, editTextUnit, editTextMinOrder, editTextMaxOrder;

    // Media
    private TextInputEditText editTextImageUrl;

    // Inventory & Dates
    private TextInputEditText editTextStockAlert, editTextManufacturingDate, editTextExpiryDate;

    // Seller Information
    private TextInputEditText editTextSellerName, editTextSellerId;

    // Additional Information
    private TextInputEditText editTextTags, editTextStorageInstructions, editTextReturnPolicy;

    // Features - Using MaterialCheckBox
    private SwitchMaterial chipInStock, chipFeatured, chipOrganic, chipVegan, chipGlutenFree;

    private MaterialButton btnSave, btnClear;
    private String productId;
    private boolean isEditMode = false;

    // Predefined data for dropdowns
    private static final String[] CATEGORIES = {
            "Fruits & Vegetables",
            "Dairy & Eggs",
            "Meat & Seafood",
            "tools",
            "dairy",
            "machines",
            "Snacks & Sweets",
            "Frozen Foods",
            "Health & Wellness",
            "Organic Products",
            "International Foods",
            "cereal",
            "fat/oil",
            "meat"
    };

    private static final String[] SUB_CATEGORIES = {
            "Fresh Fruits",
            "Fresh Vegetables",
            "Organic Fruits",
            "Organic Vegetables",
            "Milk & Cream",
            "Cheese",
            "Yogurt",
            "Butter & Margarine",
            "Eggs",
            "Chicken",
            "Beef",
            "Pork",
            "Fish",
            "Broadcast spreaders",
            "Seed drills",
            "Water pumps",
            "Hoses and sprinklers",
            " Drip lines",
            "Pasta & Noodles",
            "Canned Goods",
            "Oils & Vinegars",
            "Spices & Seasonings",
            "Coffee & Tea",
            "Juices",
            "Sodas",

            "Chips & Crackers",
            "Candy & Chocolate",
            "Ice Cream",
            "Frozen Vegetables",
            "Frozen Meals",
            "Vitamins",
            "Supplements",
            "Natural Remedies"
    };

    private static final String[] BRANDS = {
            "Tropicana",
            "Kellogg's",
            "Nestle",
            "PepsiCo",
            "Coca-Cola",
            "Danone",
            "Unilever",
            "Procter & Gamble",
            "Johnson & Johnson",
            "General Mills",
            "Kraft Heinz",
            "Mondelez",
            "Mars",
            "Ferrero",
            "Lindt",
            "Nabisco",
            "Quaker",
            "Campbell's",
            "Hormel",
            "Tyson Foods",
            "Smithfield",
            "Perdue",
            "Dole",
            "Del Monte",
            "Chiquita",
            "Heinz",
            "McCormick",
            "Hershey's",
            "Godiva",
            "Starbucks",
            "Folgers",
            "Lipton",
            "Arizona",
            "Gatorade",
            "Red Bull"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();
        initViews();
        setupDropdowns();

        // Check if in edit mode
        if (getIntent().hasExtra("product_id")) {
            isEditMode = true;
            productId = getIntent().getStringExtra("product_id");
            loadProductData();
            btnSave.setText("UPDATE PRODUCT");
        } else {
            btnSave.setText("SAVE PRODUCT");
        }

        btnSave.setOnClickListener(v -> saveProduct());
        btnClear.setOnClickListener(v -> clearForm());
    }

    private void initViews() {
        // Basic Information
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editProductCode = findViewById(R.id.editProductCode);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);

        // Category & Brand - AutoCompleteTextView
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextSubCategory = findViewById(R.id.editTextSubCategory);
        editTextBrand = findViewById(R.id.editTextBrand);

        // Pricing & Discount
        editTextDiscount = findViewById(R.id.editTextDiscount);
        editTextTaxRate = findViewById(R.id.editTextTaxRate);
        editTextSKU = findViewById(R.id.editTextSKU);
        editTextBarcode = findViewById(R.id.editTextBarcode);

        // Shipping & Weight
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextMinOrder = findViewById(R.id.editTextMinOrder);
        editTextMaxOrder = findViewById(R.id.editTextMaxOrder);

        // Media
        editTextImageUrl = findViewById(R.id.editTextImageUrl);

        // Inventory & Dates
        editTextStockAlert = findViewById(R.id.editTextStockAlert);
        editTextManufacturingDate = findViewById(R.id.editTextManufacturingDate);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);

        // Seller Information
        editTextSellerName = findViewById(R.id.editTextSellerName);
        editTextSellerId = findViewById(R.id.editTextSellerId);

        // Additional Information
        editTextTags = findViewById(R.id.editTextTags);
        editTextStorageInstructions = findViewById(R.id.editTextStorageInstructions);
        editTextReturnPolicy = findViewById(R.id.editTextReturnPolicy);

        // Features - MaterialCheckBox
        chipInStock = findViewById(R.id.chipInStock);
        chipFeatured = findViewById(R.id.chipFeatured);
        chipOrganic = findViewById(R.id.chipOrganic);
        chipVegan = findViewById(R.id.chipVegan);
        chipGlutenFree = findViewById(R.id.chipGlutenFree);

        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);

        // Set default values
        if (!isEditMode) {
            editTextUnit.setText("kg");
            chipInStock.setChecked(true);
            editTextStockAlert.setText("10");
        }
    }

    private void setupDropdowns() {
        // Setup Category Dropdown
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                CATEGORIES
        );
        editTextCategory.setAdapter(categoryAdapter);
        editTextCategory.setThreshold(1); // Start suggesting after 1 character
        editTextCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = CATEGORIES[position];
            // Optionally filter subcategories based on selected category
            filterSubCategories(selectedCategory);
        });

        // Setup SubCategory Dropdown
        ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                SUB_CATEGORIES
        );
        editTextSubCategory.setAdapter(subCategoryAdapter);
        editTextSubCategory.setThreshold(1);

        // Setup Brand Dropdown
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                BRANDS
        );
        editTextBrand.setAdapter(brandAdapter);
        editTextBrand.setThreshold(1);
    }

    private void filterSubCategories(String selectedCategory) {
        List<String> filteredSubCategories = new ArrayList<>();

        // Filter based on main category
        switch (selectedCategory) {
            case "Fruits & Vegetables":
                filteredSubCategories.addAll(Arrays.asList(
                        "Fresh Fruits", "Fresh Vegetables", "Organic Fruits", "Organic Vegetables"
                ));
                break;
            case "Dairy & Eggs":
                filteredSubCategories.addAll(Arrays.asList(
                        "Milk & Cream", "Cheese", "Yogurt", "Butter & Margarine", "Eggs"
                ));
                break;
            case "Meat & Seafood":
                filteredSubCategories.addAll(Arrays.asList(
                        "Chicken", "Beef", "Pork", "Fish", "Seafood"
                ));
                break;
            case "Bakery & Bread":
                filteredSubCategories.addAll(Arrays.asList(
                        "Bread", "Pastries", "Cakes"
                ));
                break;
            case "Pantry & Staples":
                filteredSubCategories.addAll(Arrays.asList(
                        "Rice & Grains", "Pasta & Noodles", "Canned Goods",
                        "Oils & Vinegars", "Spices & Seasonings"
                ));
                break;
            case "Beverages":
                filteredSubCategories.addAll(Arrays.asList(
                        "Coffee & Tea", "Juices", "Sodas", "Energy Drinks"
                ));
                break;
            case "Snacks & Sweets":
                filteredSubCategories.addAll(Arrays.asList(
                        "Chips & Crackers", "Candy & Chocolate"
                ));
                break;
            case "Frozen Foods":
                filteredSubCategories.addAll(Arrays.asList(
                        "Ice Cream", "Frozen Vegetables", "Frozen Meals"
                ));
                break;
            case "Health & Wellness":
                filteredSubCategories.addAll(Arrays.asList(
                        "Vitamins", "Supplements", "Natural Remedies"
                ));
                break;
            default:
                // If "All" or other category, show all subcategories
                filteredSubCategories.addAll(Arrays.asList(SUB_CATEGORIES));
                break;
        }

        // Update subcategory adapter
        ArrayAdapter<String> subAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                filteredSubCategories
        );
        editTextSubCategory.setAdapter(subAdapter);
        editTextSubCategory.setText(""); // Clear current selection
    }

    private void loadProductData() {
        db.collection("sub_product").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ProductModel product = documentSnapshot.toObject(ProductModel.class);
                        if (product != null) {
                            // Basic Information
                            editTextName.setText(product.getName());
                            editTextDescription.setText(product.getDescription());
                            editTextPrice.setText(product.getPrice());
                            editTextQuantity.setText(product.getQuantity());

                            // Category & Brand - Set text for AutoCompleteTextView
                            editTextCategory.setText(product.getCategory(), false);
                            editTextSubCategory.setText(product.getSubCategory(), false);
                            editTextBrand.setText(product.getBrand(), false);

                            // Pricing & Discount
                            editTextDiscount.setText(String.valueOf(product.getDiscountPercentage()));
                            editTextTaxRate.setText(product.getTaxRate());
                            editTextSKU.setText(product.getSku());
                            editTextBarcode.setText(product.getBarcode());

                            // Shipping & Weight
                            editTextWeight.setText(product.getWeight());
                            editTextUnit.setText(product.getUnit());
                            editTextMinOrder.setText(product.getMinOrderQuantity());
                            editTextMaxOrder.setText(product.getMaxOrderQuantity());

                            // Media
                            editTextImageUrl.setText(product.getImage());

                            // Inventory & Dates
                            editTextStockAlert.setText(String.valueOf(product.getStockAlertThreshold()));
                            editTextManufacturingDate.setText(product.getManufacturingDate());
                            editTextExpiryDate.setText(product.getExpiryDate());

                            // Seller Information
                            editTextSellerName.setText(product.getSellerName());
                            editTextSellerId.setText(product.getSellerId());

                            // Additional Information
                            editTextTags.setText(product.getTags());
                            editTextStorageInstructions.setText(product.getStorageInstructions());
                            editTextReturnPolicy.setText(product.getReturnPolicy());

                            // Features - MaterialCheckBox
                            chipInStock.setChecked(product.isInStock());
                            chipFeatured.setChecked(product.isFeatured());
                            chipOrganic.setChecked(product.isOrganic());
                            chipVegan.setChecked(product.isVegan());
                            chipGlutenFree.setChecked(product.isGlutenFree());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading product: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void saveProduct() {
        // Validate required fields
        if (!validateRequiredFields()) {
            return;
        }

        if (isEditMode) {
            updateProduct();
        } else {
            addNewProduct();
        }
    }

    private boolean validateRequiredFields() {
        if (TextUtils.isEmpty(editTextName.getText())) {
            editTextName.setError("Product name is required");
            editTextName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(editTextPrice.getText())) {
            editTextPrice.setError("Price is required");
            editTextPrice.requestFocus();
            return false;
        }

        // Validate price format
        try {
            double price = Double.parseDouble(editTextPrice.getText().toString());
            if (price <= 0) {
                editTextPrice.setError("Price must be greater than 0");
                editTextPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            editTextPrice.setError("Invalid price format");
            editTextPrice.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(editTextQuantity.getText())) {
            editTextQuantity.setError("Quantity is required");
            editTextQuantity.requestFocus();
            return false;
        }

        // Validate quantity
        try {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
            if (quantity < 0) {
                editTextQuantity.setError("Quantity cannot be negative");
                editTextQuantity.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            editTextQuantity.setError("Invalid quantity format");
            editTextQuantity.requestFocus();
            return false;
        }

        // Validate Category (required)
        if (TextUtils.isEmpty(editTextCategory.getText())) {
            editTextCategory.setError("Category is required");
            editTextCategory.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(editTextImageUrl.getText())) {
            editTextImageUrl.setError("Image URL is required");
            editTextImageUrl.requestFocus();
            return false;
        }

        // Validate URL format
        String url = editTextImageUrl.getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            editTextImageUrl.setError("Invalid URL format");
            editTextImageUrl.requestFocus();
            return false;
        }

        // Validate discount if provided
        if (!TextUtils.isEmpty(editTextDiscount.getText())) {
            try {
                double discount = Double.parseDouble(editTextDiscount.getText().toString());
                if (discount < 0 || discount > 100) {
                    editTextDiscount.setError("Discount must be between 0 and 100");
                    editTextDiscount.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                editTextDiscount.setError("Invalid discount format");
                editTextDiscount.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void addNewProduct() {
        String productId = db.collection("sub_product").document().getId();
        ProductModel product = createProductFromInput(productId);

        db.collection("sub_product")
                .document(productId)
                .set(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProduct() {
        ProductModel product = createProductFromInput(productId);
        product.setUpdatedAt(new Date());

        db.collection("sub_product")
                .document(productId)
                .set(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Update the createProductFromInput method:
    private ProductModel createProductFromInput(String productId) {
        return new ProductModel(
                  productId,
                editTextName.getText().toString().trim(),
                editTextDescription.getText().toString().trim(),
                editTextPrice.getText().toString().trim(),
                editTextQuantity.getText().toString().trim(),
                editTextImageUrl.getText().toString().trim(),
                editTextCategory.getText().toString().trim(),
                editTextSubCategory.getText().toString().trim(),
                editTextBrand.getText().toString().trim(),
                editTextSKU.getText().toString().trim(),
                TextUtils.isEmpty(editTextDiscount.getText()) ? 0 : Double.parseDouble(editTextDiscount.getText().toString()),
                editTextTaxRate.getText().toString().trim(),
                chipInStock.isChecked(),  // MaterialCheckBox uses isChecked()
                chipFeatured.isChecked(),
                editTextWeight.getText().toString().trim(),
                editTextUnit.getText().toString().trim(),
                editTextMinOrder.getText().toString().trim(),
                editTextMaxOrder.getText().toString().trim(),
                editTextSellerName.getText().toString().trim(),
                editTextSellerId.getText().toString().trim(),
                editTextBarcode.getText().toString().trim(),
                TextUtils.isEmpty(editTextStockAlert.getText()) ? 10 : Integer.parseInt(editTextStockAlert.getText().toString()),
                editTextExpiryDate.getText().toString().trim(),
                editTextManufacturingDate.getText().toString().trim(),
                editTextStorageInstructions.getText().toString().trim(),
                editTextReturnPolicy.getText().toString().trim(),
                editTextTags.getText().toString().trim(),
                chipOrganic.isChecked(),
                chipVegan.isChecked(),
                chipGlutenFree.isChecked(),
                editProductCode.getText().toString().trim()
        );
    }

    private void clearForm() {
        new AlertDialog.Builder(this)
                .setTitle("Clear Form")
                .setMessage("Are you sure you want to clear all fields?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear all text fields
                    editTextName.setText("");
                    editTextDescription.setText("");
                    editTextPrice.setText("");
                    editTextQuantity.setText("");

                    // Clear AutoCompleteTextView
                    editTextCategory.setText("", false);
                    editTextSubCategory.setText("", false);
                    editTextBrand.setText("", false);

                    editTextDiscount.setText("");
                    editTextTaxRate.setText("");
                    editTextSKU.setText("");
                    editTextBarcode.setText("");
                    editTextWeight.setText("");
                    editTextUnit.setText("kg");
                    editTextMinOrder.setText("");
                    editTextMaxOrder.setText("");
                    editTextImageUrl.setText("");
                    editTextStockAlert.setText("10");
                    editTextManufacturingDate.setText("");
                    editTextExpiryDate.setText("");
                    editTextSellerName.setText("");
                    editTextSellerId.setText("");
                    editTextTags.setText("");
                    editTextStorageInstructions.setText("");
                    editTextReturnPolicy.setText("");

                    // Reset checkboxes
                    chipInStock.setChecked(true);
                    chipFeatured.setChecked(false);
                    chipOrganic.setChecked(false);
                    chipVegan.setChecked(false);
                    chipGlutenFree.setChecked(false);

                    editTextName.requestFocus();
                    Toast.makeText(this, "Form cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}


//package com.demo.farmfresh25.crud;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.demo.farmfresh25.Model.ProductModel;
//import com.demo.farmfresh25.R;
//import com.google.android.material.button.MaterialButton;
//
//import com.google.android.material.checkbox.MaterialCheckBox;
//import com.google.android.material.chip.Chip;
//import com.google.android.material.switchmaterial.SwitchMaterial;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.Date;
//
//public class AddProductActivity extends AppCompatActivity {
//    private FirebaseFirestore db;
//
//    // Basic Information
//    private TextInputEditText editTextName,editProductCode, editTextDescription, editTextPrice, editTextQuantity;
//
//    // Category & Brand
//    private TextInputEditText editTextCategory, editTextSubCategory, editTextBrand;
//
//    // Pricing & Discount
//    private TextInputEditText editTextDiscount, editTextTaxRate, editTextSKU, editTextBarcode;
//
//    // Shipping & Weight
//    private TextInputEditText editTextWeight, editTextUnit, editTextMinOrder, editTextMaxOrder;
//
//    // Media
//    private TextInputEditText editTextImageUrl;
//
//    // Inventory & Dates
//    private TextInputEditText editTextStockAlert, editTextManufacturingDate, editTextExpiryDate;
//
//    // Seller Information
//    private TextInputEditText editTextSellerName, editTextSellerId;
//
//    // Additional Information
//    private TextInputEditText editTextTags, editTextStorageInstructions, editTextReturnPolicy;
//
//    // Features
//    private SwitchMaterial chipInStock, chipFeatured, chipOrganic, chipVegan, chipGlutenFree;
//
//    private MaterialButton btnSave, btnClear;
//    private String productId;
//    private boolean isEditMode = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_product);
////
//        db = FirebaseFirestore.getInstance();
//        initViews();
//
////         Check if in edit mode
//        if (getIntent().hasExtra("product_id")) {
//            isEditMode = true;
//            productId = getIntent().getStringExtra("product_id");
//            loadProductData();
//            btnSave.setText("UPDATE PRODUCT");
//        } else {
//            btnSave.setText("SAVE PRODUCT");
//        }
//
//        btnSave.setOnClickListener(v -> saveProduct());
//        btnClear.setOnClickListener(v -> clearForm());
//    }
//
//    private void initViews() {
//        // Basic Information
//        editTextName = findViewById(R.id.editTextName);
//        editProductCode = findViewById(R.id.editProductCode);
//        editTextDescription = findViewById(R.id.editTextDescription);
//        editTextPrice = findViewById(R.id.editTextPrice);
//        editTextQuantity = findViewById(R.id.editTextQuantity);
//
//        // Category & Brand
//        editTextCategory = findViewById(R.id.editTextCategory);
//        editTextSubCategory = findViewById(R.id.editTextSubCategory);
//        editTextBrand = findViewById(R.id.editTextBrand);
//
//        // Pricing & Discount
//        editTextDiscount = findViewById(R.id.editTextDiscount);
//        editTextTaxRate = findViewById(R.id.editTextTaxRate);
//        editTextSKU = findViewById(R.id.editTextSKU);
//        editTextBarcode = findViewById(R.id.editTextBarcode);
//
//        // Shipping & Weight
//        editTextWeight = findViewById(R.id.editTextWeight);
//        editTextUnit = findViewById(R.id.editTextUnit);
//        editTextMinOrder = findViewById(R.id.editTextMinOrder);
//        editTextMaxOrder = findViewById(R.id.editTextMaxOrder);
//
//        // Media
//        editTextImageUrl = findViewById(R.id.editTextImageUrl);
//
//        // Inventory & Dates
//        editTextStockAlert = findViewById(R.id.editTextStockAlert);
//        editTextManufacturingDate = findViewById(R.id.editTextManufacturingDate);
//        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
//
//        // Seller Information
//        editTextSellerName = findViewById(R.id.editTextSellerName);
//        editTextSellerId = findViewById(R.id.editTextSellerId);
//
//        // Additional Information
//        editTextTags = findViewById(R.id.editTextTags);
//        editTextStorageInstructions = findViewById(R.id.editTextStorageInstructions);
//        editTextReturnPolicy = findViewById(R.id.editTextReturnPolicy);
//
//        // Features
//        // Features - Updated to use MaterialCheckBox
//        chipInStock = findViewById(R.id.chipInStock);
//        chipFeatured = findViewById(R.id.chipFeatured);
//        chipOrganic = findViewById(R.id.chipOrganic);
//        chipVegan = findViewById(R.id.chipVegan);
//        chipGlutenFree = findViewById(R.id.chipGlutenFree);
//
//        btnSave = findViewById(R.id.btnSave);
//        btnClear = findViewById(R.id.btnClear);
//
//        // Set default values
//        if (!isEditMode) {
//            editTextUnit.setText("kg");
//            chipInStock.setChecked(true);
//            editTextStockAlert.setText("10");
//        }
//    }
//
//    private void loadProductData() {
//        db.collection("sub_product").document(productId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        ProductModel product = documentSnapshot.toObject(ProductModel.class);
//                        if (product != null) {
//                            // Basic Information
//                            editTextName.setText(product.getName());
//                            editProductCode.setText(product.getProductCode());
//                            editTextDescription.setText(product.getDescription());
//                            editTextPrice.setText(product.getPrice());
//                            editTextQuantity.setText(product.getQuantity());
//
//                            // Category & Brand
//                            editTextCategory.setText(product.getCategory());
//                            editTextSubCategory.setText(product.getSubCategory());
//                            editTextBrand.setText(product.getBrand());
//
//                            // Pricing & Discount
//                            editTextDiscount.setText(String.valueOf(product.getDiscountPercentage()));
//                            editTextTaxRate.setText(product.getTaxRate());
//                            editTextSKU.setText(product.getSku());
//                            editTextBarcode.setText(product.getBarcode());
//
//                            // Shipping & Weight
//                            editTextWeight.setText(product.getWeight());
//                            editTextUnit.setText(product.getUnit());
//                            editTextMinOrder.setText(product.getMinOrderQuantity());
//                            editTextMaxOrder.setText(product.getMaxOrderQuantity());
//
//                            // Media
//                            editTextImageUrl.setText(product.getImage());
//
//                            // Inventory & Dates
//                            editTextStockAlert.setText(String.valueOf(product.getStockAlertThreshold()));
//                            editTextManufacturingDate.setText(product.getManufacturingDate());
//                            editTextExpiryDate.setText(product.getExpiryDate());
//
//                            // Seller Information
//                            editTextSellerName.setText(product.getSellerName());
//                            editTextSellerId.setText(product.getSellerId());
//
//                            // Additional Information
//                            editTextTags.setText(product.getTags());
//                            editTextStorageInstructions.setText(product.getStorageInstructions());
//                            editTextReturnPolicy.setText(product.getReturnPolicy());
//
//                            // Features
//                            chipInStock.setChecked(product.isInStock());
//                            chipFeatured.setChecked(product.isFeatured());
//                            chipOrganic.setChecked(product.isOrganic());
//                            chipVegan.setChecked(product.isVegan());
//                            chipGlutenFree.setChecked(product.isGlutenFree());
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Error loading product: " + e.getMessage(),
//                            Toast.LENGTH_SHORT).show();
//                    finish();
//                });
//    }
//
//    private void saveProduct() {
//        // Validate required fields
//        if (!validateRequiredFields()) {
//            return;
//        }
//
//        if (isEditMode) {
//            updateProduct();
//        } else {
//            addNewProduct();
//        }
//    }
//
//    private boolean validateRequiredFields() {
//        if (TextUtils.isEmpty(editTextName.getText())) {
//            editTextName.setError("Product name is required");
//            editTextName.requestFocus();
//            return false;
//        }
//
//        if (TextUtils.isEmpty(editTextPrice.getText())) {
//            editTextPrice.setError("Price is required");
//            editTextPrice.requestFocus();
//            return false;
//        }
//
//        // Validate price format
//        try {
//            double price = Double.parseDouble(editTextPrice.getText().toString());
//            if (price <= 0) {
//                editTextPrice.setError("Price must be greater than 0");
//                editTextPrice.requestFocus();
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            editTextPrice.setError("Invalid price format");
//            editTextPrice.requestFocus();
//            return false;
//        }
//
//        if (TextUtils.isEmpty(editTextQuantity.getText())) {
//            editTextQuantity.setError("Quantity is required");
//            editTextQuantity.requestFocus();
//            return false;
//        }
//
//        // Validate quantity
//        try {
//            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
//            if (quantity < 0) {
//                editTextQuantity.setError("Quantity cannot be negative");
//                editTextQuantity.requestFocus();
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            editTextQuantity.setError("Invalid quantity format");
//            editTextQuantity.requestFocus();
//            return false;
//        }
//
//        if (TextUtils.isEmpty(editTextCategory.getText())) {
//            editTextCategory.setError("Category is required");
//            editTextCategory.requestFocus();
//            return false;
//        }
//
//        if (TextUtils.isEmpty(editTextImageUrl.getText())) {
//            editTextImageUrl.setError("Image URL is required");
//            editTextImageUrl.requestFocus();
//            return false;
//        }
//
//        // Validate URL format
//        String url = editTextImageUrl.getText().toString();
//        if (!url.startsWith("http://") && !url.startsWith("https://")) {
//            editTextImageUrl.setError("Invalid URL format");
//            editTextImageUrl.requestFocus();
//            return false;
//        }
//
//        // Validate discount if provided
//        if (!TextUtils.isEmpty(editTextDiscount.getText())) {
//            try {
//                double discount = Double.parseDouble(editTextDiscount.getText().toString());
//                if (discount < 0 || discount > 100) {
//                    editTextDiscount.setError("Discount must be between 0 and 100");
//                    editTextDiscount.requestFocus();
//                    return false;
//                }
//            } catch (NumberFormatException e) {
//                editTextDiscount.setError("Invalid discount format");
//                editTextDiscount.requestFocus();
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private void addNewProduct() {
//        String productId = db.collection("sub_product").document().getId();
//        ProductModel product = createProductFromInput(productId);
//
//        db.collection("sub_product")
//                .document(productId)
//                .set(product)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
//                    clearForm();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    private void updateProduct() {
//        ProductModel product = createProductFromInput(productId);
//        product.setUpdatedAt(new Date());
//
//        db.collection("sub_product")
//                .document(productId)
//                .set(product)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    // Update the createProductFromInput method:
//    private ProductModel createProductFromInput(String productId) {
//        return new ProductModel(
//                  productId,
//                editTextName.getText().toString().trim(),
//                editTextDescription.getText().toString().trim(),
//                editTextPrice.getText().toString().trim(),
//                editTextQuantity.getText().toString().trim(),
//                editTextImageUrl.getText().toString().trim(),
//                editTextCategory.getText().toString().trim(),
//                editTextSubCategory.getText().toString().trim(),
//                editTextBrand.getText().toString().trim(),
//                editTextSKU.getText().toString().trim(),
//                TextUtils.isEmpty(editTextDiscount.getText()) ? 0 : Double.parseDouble(editTextDiscount.getText().toString()),
//                editTextTaxRate.getText().toString().trim(),
//                chipInStock.isChecked(),  // MaterialCheckBox uses isChecked()
//                chipFeatured.isChecked(),
//                editTextWeight.getText().toString().trim(),
//                editTextUnit.getText().toString().trim(),
//                editTextMinOrder.getText().toString().trim(),
//                editTextMaxOrder.getText().toString().trim(),
//                editTextSellerName.getText().toString().trim(),
//                editTextSellerId.getText().toString().trim(),
//                editTextBarcode.getText().toString().trim(),
//                TextUtils.isEmpty(editTextStockAlert.getText()) ? 10 : Integer.parseInt(editTextStockAlert.getText().toString()),
//                editTextExpiryDate.getText().toString().trim(),
//                editTextManufacturingDate.getText().toString().trim(),
//                editTextStorageInstructions.getText().toString().trim(),
//                editTextReturnPolicy.getText().toString().trim(),
//                editTextTags.getText().toString().trim(),
//                chipOrganic.isChecked(),
//                chipVegan.isChecked(),
//                chipGlutenFree.isChecked(),
//                editProductCode.getText().toString().trim()
//        );
//    }
//    private void clearForm() {
//        new AlertDialog.Builder(this)
//                .setTitle("Clear Form")
//                .setMessage("Are you sure you want to clear all fields?")
//                .setPositiveButton("Yes", (dialog, which) -> {
//                    // Clear all text fields
//                    editTextName.setText("");
//                    editTextDescription.setText("");
//                    editTextPrice.setText("");
//                    editTextQuantity.setText("");
//                    editTextCategory.setText("");
//                    editTextSubCategory.setText("");
//                    editTextBrand.setText("");
//                    editTextDiscount.setText("");
//                    editTextTaxRate.setText("");
//                    editTextSKU.setText("");
//                    editTextBarcode.setText("");
//                    editTextWeight.setText("");
//                    editTextUnit.setText("kg");
//                    editTextMinOrder.setText("");
//                    editTextMaxOrder.setText("");
//                    editTextImageUrl.setText("");
//                    editTextStockAlert.setText("10");
//                    editTextManufacturingDate.setText("");
//                    editTextExpiryDate.setText("");
//                    editTextSellerName.setText("");
//                    editTextSellerId.setText("");
//                    editTextTags.setText("");
//                    editTextStorageInstructions.setText("");
//                    editTextReturnPolicy.setText("");
//
//                    // Reset chips
//                    chipInStock.setChecked(true);
//                    chipFeatured.setChecked(false);
//                    chipOrganic.setChecked(false);
//                    chipVegan.setChecked(false);
//                    chipGlutenFree.setChecked(false);
//
//                    editTextName.requestFocus();
//                    Toast.makeText(this, "Form cleared", Toast.LENGTH_SHORT).show();
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }
//}
//
//
//
//
//
//
////package com.demo.farmfresh25.crud;
////import android.app.AlertDialog;
////import android.os.Bundle;
////import android.text.TextUtils;
////import android.util.Log;
////import android.view.View;
////import android.widget.ArrayAdapter;
////import android.widget.Toast;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.demo.farmfresh25.Model.ProductModel;
////import com.demo.farmfresh25.R;
////import com.google.android.material.button.MaterialButton;
////import com.google.android.material.textfield.TextInputEditText;
////import com.google.android.material.textfield.TextInputLayout;
////import com.google.firebase.firestore.FirebaseFirestore;
////import com.google.firebase.firestore.QueryDocumentSnapshot;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class AddProductActivity extends AppCompatActivity {
////    private FirebaseFirestore db;
////    private TextInputEditText editTextName, editTextPrice, editTextImageUrl, editTextCategory;
////    private MaterialButton btnSave;
////    private String productId; // For edit mode
////    private boolean isEditMode = false;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_add_product);
////
////        db = FirebaseFirestore.getInstance();
////        initViews();
////
////        // Check if we're in edit mode
////        if (getIntent().hasExtra("product_id")) {
////            isEditMode = true;
////            productId = getIntent().getStringExtra("product_id");
////            loadProductData(productId);
////            btnSave.setText("UPDATE PRODUCT");
////        } else {
////            btnSave.setText("SAVE PRODUCT");
////        }
////
////        btnSave.setOnClickListener(v -> saveProduct());
////    }
////
////    private void initViews() {
////        editTextName = findViewById(R.id.edittextname);
////        editTextPrice = findViewById(R.id.edittextprice);
////        editTextImageUrl = findViewById(R.id.edittextimageurl);
////        editTextCategory = findViewById(R.id.edittextcategory);
////        btnSave = findViewById(R.id.btnSave);
////    }
////
////    private void loadProductData(String productId) {
////        db.collection("sub_product").document(productId)
////                .get()
////                .addOnSuccessListener(documentSnapshot -> {
////                    if (documentSnapshot.exists()) {
////                        ProductAdminModel product = documentSnapshot.toObject(ProductAdminModel.class);
////                        if (product != null) {
////                            editTextName.setText(product.getName());
////                            editTextPrice.setText(product.getPrice());
////                            editTextImageUrl.setText(product.getImage());
////                            editTextCategory.setText(product.getCategory());
////                        }
////                    }
////                })
////                .addOnFailureListener(e -> {
////                    Toast.makeText(this, "Error loading product: " + e.getMessage(),
////                            Toast.LENGTH_SHORT).show();
////                    finish();
////                });
////    }
////
////    private void saveProduct() {
////        String name = editTextName.getText().toString().trim();
////        String price = editTextPrice.getText().toString().trim();
////        String imageUrl = editTextImageUrl.getText().toString().trim();
////        String category = editTextCategory.getText().toString().trim();
////
////        // Input Validation
////        if (!validateInputs(name, price, imageUrl, category)) {
////            return;
////        }
////
////        if (isEditMode) {
////            updateProduct(name, price, imageUrl, category);
////
////        } else {
////            addNewProduct(name, price, imageUrl, category);
////        }
////    }
////
////    private boolean validateInputs(String name, String price, String imageUrl, String category) {
////        if (TextUtils.isEmpty(name)) {
////            editTextName.setError("Product name is required");
////            editTextName.requestFocus();
////            return false;
////        }
////
////        if (TextUtils.isEmpty(price)) {
////            editTextPrice.setError("Price is required");
////            editTextPrice.requestFocus();
////            return false;
////        }
////
////        // Validate price is a number
////        try {
////            double priceValue = Double.parseDouble(price);
////            if (priceValue <= 0) {
////                editTextPrice.setError("Price must be greater than 0");
////                editTextPrice.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            editTextPrice.setError("Invalid price format");
////            editTextPrice.requestFocus();
////            return false;
////        }
////
////        if (TextUtils.isEmpty(imageUrl)) {
////            editTextImageUrl.setError("Image URL is required");
////            editTextImageUrl.requestFocus();
////            return false;
////        }
////
////        // Basic URL validation
////        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
////            editTextImageUrl.setError("Invalid URL format. Use http:// or https://");
////            editTextImageUrl.requestFocus();
////            return false;
////        }
////
////        if (TextUtils.isEmpty(category)) {
////            editTextCategory.setError("Category is required");
////            editTextCategory.requestFocus();
////            return false;
////        }
////
////        return true;
////    }
////
////    private void addNewProduct(String name, String price, String imageUrl, String category) {
////        String productId = db.collection("sub_product").document().getId();
////        ProductModel product = new ProductModel(productId, name, price, imageUrl, category);
////
////        db.collection("sub_product")
////                .document(productId)
////                .set(product)
////                .addOnSuccessListener(aVoid -> {
////                    Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
////                    clearForm();
////                })
////                .addOnFailureListener(e -> {
////                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                });
////    }
////
////    private void updateProduct(String name, String price, String imageUrl, String category) {
////        ProductModel product = new ProductModel(productId, name, price, imageUrl, category);
////
////        db.collection("sub_product")
////                .document(productId)
////                .set(product)
////                .addOnSuccessListener(aVoid -> {
////                    Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
////                    finish(); // Close activity after update
////                })
////                .addOnFailureListener(e -> {
////                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                });
////    }
////
////    private void clearForm() {
////        editTextName.setText("");
////        editTextPrice.setText("");
////        editTextImageUrl.setText("");
////        editTextCategory.setText("");
////        editTextName.requestFocus();
////    }
////}