package com.demo.farmfresh25.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo.farmfresh25.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

// Replace 'Object' with your specific Cart Item model class if you have one
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<?> cartList;

    public CartAdapter(List<?> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_shopping_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Bind your data here

        holder.deleteBtn.setOnClickListener(v -> {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("cart")
                    .document(cartList.get(position).getId())
                    .delete();
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        public View deleteBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}





