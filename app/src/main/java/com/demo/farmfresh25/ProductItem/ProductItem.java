package com.demo.farmfresh25.ProductItem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.farmfresh25.R;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProductItem#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProductItem extends Fragment {
        String name;
        String price;
        String image;

        public ProductItem(){}

        public ProductItem(String name, String price, String image){
            this.name = name;
            this.price = price;
            this.image = image;
        }

        public String getName(){
            return name;
        }

        public String getPrice(){
            return price;
        }

        public String getImage(){
            return image;
        }


//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public ProductItem() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ProductItem.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ProductItem newInstance(String param1, String param2) {
//        ProductItem fragment = new ProductItem();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_item, container, false);
    }
}