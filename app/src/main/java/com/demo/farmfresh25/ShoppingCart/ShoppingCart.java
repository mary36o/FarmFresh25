package com.demo.farmfresh25.ShoppingCart;

//import static com.demo.farmfresh25.DashBoard.DashBoard.ARG_PARAM1;
//import static com.demo.farmfresh25.DashBoard.DashBoard.ARG_PARAM2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.farmfresh25.Adapter.CartAdapter;
import com.demo.farmfresh25.CartManager;
import com.demo.farmfresh25.R;

public class ShoppingCart extends Fragment {



        RecyclerView recyclerView;

//        public ShoppingCart() {
//            // Required empty public constructor
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

            recyclerView = view.findViewById(R.id.recyclerCart);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new CartAdapter(CartManager.cartList));

            return view;
        }



    private String mParam1;
    private String mParam2;

    public ShoppingCart() {
        // Required empty public constructor
    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ShoppingCart.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ShoppingCart newInstance(String param1, String param2) {
//        ShoppingCart fragment = new ShoppingCart();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


}