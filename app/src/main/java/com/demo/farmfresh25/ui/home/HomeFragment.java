package com.demo.farmfresh25.ui.home;

import android.content.Intent;import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.demo.farmfresh25.Adapter.CategoryAdapter;
import com.demo.farmfresh25.Category;
import com.demo.farmfresh25.Interface.CategoryInterface;
import com.demo.farmfresh25.Model.ProductModel;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.Adapter.SliderAdapter;
import com.demo.farmfresh25.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment   implements CategoryInterface {

    private FragmentHomeBinding binding;

    private static final String TAG = "HomeFragment";
    private Handler handler = new Handler();

    RecyclerView categoryRecyclerView;
    private CategoryAdapter Adapter2;
    private ArrayList<ProductModel> list;

    private FirebaseFirestore db;

    int[] images = {
            R.drawable.image2,
            R.drawable.cabbage,
            R.drawable.image12,
            R.drawable.image45,
            R.drawable.imagee,
            R.drawable.imagess,
            R.drawable.lmage
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding == null) return;
            int next = binding.sliderViewPager.getCurrentItem() + 1;
            if (next >= images.length) {
                next = 0;
            }
            binding.sliderViewPager.setCurrentItem(next, true);
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        categoryRecyclerView  = binding.categoryRecyclerView;

        // Setup ViewPager
        SliderAdapter adapter = new SliderAdapter(getContext(), images);
        binding.sliderViewPager.setAdapter(adapter);
        handler.postDelayed(runnable, 3000);

        list = new ArrayList<>();
        Adapter2 = new CategoryAdapter(getContext(),list,this);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoryRecyclerView.setAdapter(Adapter2);

        db = FirebaseFirestore.getInstance();
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               loadProducts();
           }
       });

        return root;
    }

    private void loadProducts() {
            db.collection("products")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if(Adapter2 == null || getContext() == null) return;

                        list.clear(); // clear old data

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            try {
                                ProductModel product = doc.toObject(ProductModel.class);
                                if (product != null) {
                                    // Optional: Validate required fields
                                    if (product.getName() != null && Double.parseDouble(product.getPrice()) > 0) {
                                        Log.d(TAG, "Added product: " + product.getName());


                                            list.add(product);
                                    } else {
                                        Log.w(TAG, "Skipping invalid product: " + doc.getId());
                                    }
                                } else {
                                    Log.w(TAG, "Failed to convert document to ProductModel: " + doc.getId());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error processing document " + doc.getId(), e);
                            }
                        }

                        Adapter2.notifyDataSetChanged();

                        // Show message if no products loaded
                        if (list.isEmpty()) {
                            Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading products", e);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Failed to load products: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

    }


    private void openDetails(String category) {
        Intent intent = new Intent(getActivity(), ProductDetails.class);
        intent.putExtra("category", category);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        binding = null;
    }

    @Override
    public void onCategoryClick(ProductModel productModel) {
////      From Fragement to activity
        Intent intent = new Intent(getActivity(), Category.class);
        intent.putExtra("category", productModel.getCategory());
        startActivity(intent);

        Toast.makeText(getContext(), productModel.getCategory(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryLongClick(ProductModel productModel) {

    }


}





