package com.demo.farmfresh25.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.farmfresh25.Model.ProductModel;
import com.demo.farmfresh25.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductModel product);
    }

    public ProductAdapter(Context context, List<ProductModel> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("GHS %s", product.getPrice()));

        if (product.getRating() > 0) {
            holder.productRating.setText(String.format("%.1f ★", product.getRating()));
        } else {
            holder.productRating.setText("4.5 ★");
        }

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
//            productRating = itemView.findViewById(R.id.productRating);
        }
    }
}