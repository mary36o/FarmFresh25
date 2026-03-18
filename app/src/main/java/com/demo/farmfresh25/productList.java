package com.demo.farmfresh25;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class productList extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<ProductModel> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        recyclerView = findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        productList = new ArrayList<>();

        String category = getIntent().getStringExtra("category");

//        if(category.equals("sweet_potato"))
//        {
//            productList.add(new ProductModel("Fresh Sweet Potato","20",R.drawable.sweet1));
//            productList.add(new ProductModel("Organic Sweet Potato","25",R.drawable.sweet2));
//        }
//
//        if(category.equals("cassava"))
//        {
//            productList.add(new ProductModel("Fresh Cassava","18",R.drawable.cas1));
//            productList.add(new ProductModel("Farm Cassava","22",R.drawable.cas2));
//        }

        adapter = new ProductAdapter(this,productList);
        recyclerView.setAdapter(adapter);

    }
}