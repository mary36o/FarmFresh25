package com.demo.farmfresh25.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Model.ProductModel;
import com.demo.farmfresh25.ProductDetails;
import com.demo.farmfresh25.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>{

    Context context;
    private ArrayList<ProductModel> list;;

    private ProductModel productModel;

    public SubCategoryAdapter(Context context, ArrayList<ProductModel> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sub_category_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        productModel = list.get(position);
//        holder.imageView.setImageResource(images[position]);
        Glide.with(context)
                .load(productModel.getImage())
                .into(holder.image);
        holder.name.setText(productModel.getName());
        holder.price.setText(productModel.getPrice());
        holder.description.setText(productModel.getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("productId", productModel.getId());
                context.startActivity(intent);
                Toast.makeText(context, "Hiiiiiiiiiiiiiiiiiii", Toast.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,price,description;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemImage);
            name = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.itemPrice);
            description = itemView.findViewById(R.id.itemDescription);
            cardView = itemView.findViewById(R.id.CategoryCard);
        }
    }
}
