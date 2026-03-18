package com.demo.farmfresh25.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.demo.farmfresh25.Authentification.Register;
import com.demo.farmfresh25.ProductDetails;
import com.demo.farmfresh25.R;
import com.demo.farmfresh25.SliderAdapter;
import com.demo.farmfresh25.databinding.FragmentHomeBinding;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
public class HomeFragment extends Fragment {

    CardView sweetPotato, cassava, fruits, leaves;
    private FragmentHomeBinding binding;

    ViewPager2 viewPager2;
    Handler handler = new Handler();

    int[] images = {
            R.drawable.image2,
            R.drawable.cabbage,
            R.drawable.image12,
            R.drawable.image45,
            R.drawable.imagee,
            R.drawable.imagess,
            R.drawable.lmage
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            int next = viewPager2.getCurrentItem() + 1;

            if(next >= images.length){
                next = 0;
            }

            viewPager2.setCurrentItem(next,true);

            handler.postDelayed(this,3000);
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager2 = binding.sliderViewPager;

        SliderAdapter adapter = new SliderAdapter(getContext(), images);
        viewPager2.setAdapter(adapter);

        handler.postDelayed(runnable, 3000);



            View view = inflater.inflate(R.layout.fragment_home, container, false);

//            sweetPotato = view.findViewById(R.id.SweetPotato);
            cassava = view.findViewById(R.id.cardCassava);
            fruits = view.findViewById(R.id.cardFruits);
            leaves = view.findViewById(R.id.cardLeaves);

//            sweetPotato.setOnClickListener(v -> {
//                Intent intent = new Intent(getActivity(), ProductDetails.class);
//                intent.putExtra("category","sweet_potato");
//                startActivity(intent);
//            });

            cassava.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("category","cassava");
                startActivity(intent);
            });

            fruits.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("category","fruits");
                startActivity(intent);
            });

            leaves.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("category","leaves");
                startActivity(intent);
            });

        return root;
        }

    }





