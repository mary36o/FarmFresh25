package com.demo.farmfresh25;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.farmfresh25.Adapter.CategoryAdapter;
import com.demo.farmfresh25.Adapter.SubCategoryAdapter;
import com.demo.farmfresh25.Model.ProductModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

     SubCategoryAdapter subCategoryAdapter;
    private FirebaseFirestore db;
    RecyclerView recyclerView;
    ArrayList<ProductModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.SubCategoryRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager( new GridLayoutManager (this, 2));


        list = new ArrayList<>();




        subCategoryAdapter = new SubCategoryAdapter(this, list);
        recyclerView.setAdapter(subCategoryAdapter);
        subCategoryAdapter.notifyDataSetChanged();



        db = FirebaseFirestore.getInstance();
        String category = getIntent().getStringExtra("category");
        loadProducts(category);


    }


    private void loadProducts(String category) {

        db.collection("products")
                .whereEqualTo("category", "cassava")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    list.clear(); // clear old data

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        ProductModel product = doc.toObject(ProductModel.class);

                        list.add(product);
                    }

                    subCategoryAdapter.notifyDataSetChanged();// refresh UI
                });
    }

}