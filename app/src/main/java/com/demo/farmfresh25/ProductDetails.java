package com.demo.farmfresh25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetails extends AppCompatActivity {

    ImageView image;
    TextView name, price;

    Button addToCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

//        image = findViewById(R.id.detailImage);
//        name = findViewById(R.id.detailName);
//        price = findViewById(R.id.detailPrice);

        Intent intent = getIntent();

        name.setText(intent.getStringExtra("name"));
        price.setText("GHS "+intent.getStringExtra("price"));
        image.setImageResource(intent.getIntExtra("image",0));
    }


//
//
//    addToCart = findViewById(R.id.addToCart);
//
//addToCart.setOnClickListener(v -> {
//
//        CartManager.cartList.add(
//                new CartModel(
//                        name.getText().toString(),
//                        price.getText().toString(),
//                        image
//                )
//        );
//
//    });

    }

