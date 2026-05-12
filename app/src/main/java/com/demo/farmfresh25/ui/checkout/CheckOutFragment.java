package com.demo.farmfresh25.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.demo.farmfresh25.databinding.FragmentGalleryBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CheckOutFragment extends Fragment {

    DocumentReference db;
    View checkoutBtn;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        checkoutBtn.setOnClickListener(v -> {


            db.collection("cart")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            db.collection("cart").document(doc.getId()).delete();
                        }

                        Toast.makeText(requireContext(),
                                "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    });
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}