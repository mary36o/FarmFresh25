package com.demo.farmfresh25;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.farmfresh25.Model.ProductModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class Addproduct extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_addproduct);

    }

    public void addNewProduct(View view){


        TextInputEditText edittextname = findViewById(R.id.edittextname);
        TextInputEditText edittextprice = findViewById(R.id.edittextprice);
        TextInputEditText edittextimageurl = findViewById(R.id.edittextimageurl);

        String name = edittextname.getText().toString();
        String price = edittextprice.getText().toString();
        String image = edittextimageurl.getText().toString();

        ProductModel product = new ProductModel(name, price, image, "    " );

        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                });
    }
}