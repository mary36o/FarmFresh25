package com.demo.farmfresh25.crud;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.demo.farmfresh25.Model.ProductModel;
import com.demo.farmfresh25.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductActionListener {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ProductModel> productList;
    private List<ProductModel> filteredList;
    private TextInputEditText searchEditText;
//    private Spinner categorySpinner;
    private FloatingActionButton fabAdd;
    private Set<String> categories;
    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        categories = new HashSet<>();
        categories.add("All");

        initViews();
        setupRecyclerView();
        loadProducts();
        setupSearch();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.productsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
//        categorySpinner = findViewById(R.id.categorySpinner);
        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadProducts() {
        db.collection("sub_product")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    categories.clear();
                    categories.add("All");

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ProductModel product = document.toObject(ProductModel.class);
                        productList.add(product);
                        categories.add(product.getCategory());
                    }

                    updateCategorySpinner();
                    filterProducts();
                })
                .addOnFailureListener(e -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error")
                            .setMessage("Failed to load products: " + e.getMessage())
                            .setPositiveButton("Retry", (dialog, which) -> loadProducts())
                            .setNegativeButton("Cancel", null)
                            .show();
                });
    }

    private void updateCategorySpinner() {
        List<String> categoryList = new ArrayList<>(categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(adapter);

//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedCategory = categoryList.get(position);
//                filterProducts();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                selectedCategory = "All";
//                filterProducts();
//            }
//        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterProducts() {
        filteredList.clear();
        String searchQuery = searchEditText.getText().toString().toLowerCase().trim();

        for (ProductModel product : productList) {
            boolean matchesCategory = selectedCategory.equals("All") ||
                    product.getName().toLowerCase().equals(selectedCategory);

            boolean matchesSearch = searchQuery.isEmpty() ||
                    product.getName().toLowerCase().contains(searchQuery);

            if (matchesCategory && matchesSearch) {
                filteredList.add(product);
            }
        }

        adapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            // Show empty state message
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Products Found")
                    .setMessage("No products match your search criteria")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    public void onEdit(ProductModel product) {
        Log.d("edit", product.getId());
        Intent intent = new Intent(this, AddProductActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(ProductModel product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete " + product.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.collection("sub_product")
                            .document(product.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Product deleted successfully",
                                        Toast.LENGTH_SHORT).show();
                                loadProducts(); // Refresh the list
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Refresh when returning to this activity
    }
}