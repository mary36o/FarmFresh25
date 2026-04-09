
package com.demo.farmfresh25;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Adapter.SubCategoryAdapter;
import com.demo.farmfresh25.Model.ProductModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    private SubCategoryAdapter subCategoryAdapter2, subCategoryAdapter;

    private static final String TAG = "Category";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ArrayList<ProductModel> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        SearchView searchView = findViewById(R.id.SubCategorySearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FilltedList(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                FilltedList(newText);
                return false;
            }
        });


        recyclerView = findViewById(R.id.SubCategoryRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        list2 = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // Safely get the category from Intent
        String category = getIntent().getStringExtra("category");
        if (category == null) category = "";

        Log.d(TAG, "OnCategory: " + category);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();

        subCategoryAdapter2 = new SubCategoryAdapter(this, list2);
        recyclerView.setAdapter(subCategoryAdapter2);

        loadProducts(category);

        // If you want to run backup, pass the category to it
        // backup(category);
    }

    private void FilltedList(String newText) {



        ArrayList<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel item : list2) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            subCategoryAdapter = new SubCategoryAdapter(this, filteredList);
            recyclerView.setAdapter(subCategoryAdapter);
            subCategoryAdapter.notifyDataSetChanged();
        }else{
            subCategoryAdapter = new SubCategoryAdapter(this, filteredList);
            recyclerView.setAdapter(subCategoryAdapter);
            subCategoryAdapter.notifyDataSetChanged();
        }

    }

    private void loadProducts(String category) {
        // Firestore callbacks run on the main thread, runOnUiThread is usually unnecessary
        list2.clear();

        db.collection("sub_product")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    list2.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        ProductModel product = doc.toObject(ProductModel.class);
                        if (product != null) {
                            list2.add(product);
                            Log.d(TAG, "loadProducts: " + product.getName());
                        }
                    }
                    subCategoryAdapter2.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading products", e));
    }

    // Pass category as a parameter to fix scope issues
    public void backup(String category) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference oldCollection = db.collection("subCategory");
        CollectionReference newCollection = db.collection("sub_product");

        // Use Firestore's whereEqualTo instead of Realtime Database syntax
        oldCollection.whereEqualTo("category", category).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // 1. Copy data to new collection
                    DocumentReference newDocRef = newCollection.document(document.getId());
                    batch.set(newDocRef, document.getData());

                    // 2. Mark old document for deletion
                    batch.delete(document.getReference());
                }

                // 3. Commit changes
                batch.commit().addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Collection renamed successfully");
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Error committing batch", e);
                });
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}