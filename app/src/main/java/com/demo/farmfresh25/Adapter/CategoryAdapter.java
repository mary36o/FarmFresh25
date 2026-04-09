package com.demo.farmfresh25.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Interface.CategoryInterface;
import com.demo.farmfresh25.Model.ProductModel;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.databinding.CustomCategoryCardBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{


    CategoryInterface categroyinterface;
    Context context;
    private ArrayList<ProductModel> list;


    public CategoryAdapter(Context context, ArrayList<ProductModel> list, CategoryInterface categroyinterface){
        this.context = context;
        this.list = list;
        this.categroyinterface = categroyinterface;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_category_card,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       ProductModel productModel = list.get(position);
//        holder.image.setImageResource(Integer.parseInt(productModel.getImage()));
        // Add error handling
        Glide.with(context)
                .load(productModel.getImage())
                .error(R.drawable.egg1) // Show placeholder on error
                .placeholder(R.drawable.image12) // Show while loading
                .into(holder.image);
        holder.name.setText(productModel.getName());
//        holder.price.setText(productModel.getPrice());
//        holder.description.setText(productModel.getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categroyinterface.onCategoryClick(productModel);
            }
        });

    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        CardView cardView;

        TextView name,price,description;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.CutomCardView);
            image = itemView.findViewById(R.id.itemImage);
            name = itemView.findViewById(R.id.itemName);

        }
    }
}