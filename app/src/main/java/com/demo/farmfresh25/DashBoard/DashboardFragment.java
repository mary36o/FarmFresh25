//package com.demo.farmfresh25.DashBoard;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.demo.farmfresh25.Adapter.CategoryAdapter;
//import com.demo.farmfresh25.Adapter.ProductAdapter;
//import com.demo.farmfresh25.Model.Category;
//import com.demo.farmfresh25.Model.ProductModel;
//import com.demo.farmfresh25.R;
//import com.demo.farmfresh25.ProductDetails;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//
//public class DashboardFragment extends Fragment {
//
//    // Views
//    private RecyclerView categoryRecyclerView, featuredRecyclerView, popularRecyclerView;
//    private ViewPager2 bannerViewPager;
//    private TextView userName, welcomeText;
//    private ImageView profileImage, searchIcon, notificationIcon;
//    private CardView veggiesCard, fruitsCard, dairyCard, organicCard;
//
//    // Adapters
//    private CategoryAdapter categoryAdapter;
//    private ProductAdapter featuredAdapter, popularAdapter;
//
//    // Data Lists
//    private ArrayList<Category> categoryList;
//    private ArrayList<ProductModel> featuredList;
//    private ArrayList<ProductModel> popularList;
//    private ArrayList<String> bannerList;
//
//    // Firebase
//    private FirebaseFirestore db;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
//
//        // Initialize views
//        initializeViews(view);
//
//        // Initialize Firebase
//        db = FirebaseFirestore.getInstance();
//
//        // Initialize lists
//        initializeLists();
//
//        // Setup adapters
//        setupAdapters();
//
//        // Load data
//        loadCategories();
//        loadFeaturedProducts();
//        loadPopularProducts();
//        loadBanners();
//
//        // Setup click listeners
//        setupClickListeners();
//
//        return view;
//    }
//
//    private void initializeViews(View view) {
//        // Header
//        userName = view.findViewById(R.id.userName);
//        welcomeText = view.findViewById(R.id.welcomeText);
//        profileImage = view.findViewById(R.id.profileImage);
//        searchIcon = view.findViewById(R.id.searchIcon);
//        notificationIcon = view.findViewById(R.id.notificationIcon);
//
//        // Categories
//        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
//
//        // Banner
//        bannerViewPager = view.findViewById(R.id.bannerViewPager);
//
//        // Featured Products
//        featuredRecyclerView = view.findViewById(R.id.featuredRecyclerView);
//
//        // Popular Products
//        popularRecyclerView = view.findViewById(R.id.popularRecyclerView);
//
//        // Quick Action Cards
//        veggiesCard = view.findViewById(R.id.veggiesCard);
//        fruitsCard = view.findViewById(R.id.fruitsCard);
//        dairyCard = view.findViewById(R.id.dairyCard);
//        organicCard = view.findViewById(R.id.organicCard);
//    }
//
//    private void initializeLists() {
//        categoryList = new ArrayList<>();
//        featuredList = new ArrayList<>();
//        popularList = new ArrayList<>();
//        bannerList = new ArrayList<>();
//    }
//
//    private void setupAdapters() {
//        // Categories adapter
//        categoryAdapter = new CategoryAdapter(requireContext(), categoryList);
//        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        categoryRecyclerView.setAdapter(categoryAdapter);
//        categoryRecyclerView.setNestedScrollingEnabled(false);
//
//        // Featured products adapter
//        featuredAdapter = new ProductAdapter(requireContext(), featuredList, product -> {
//            openProductDetails(product);
//        });
//        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        featuredRecyclerView.setAdapter(featuredAdapter);
//        featuredRecyclerView.setNestedScrollingEnabled(false);
//
//        // Popular products adapter
//        popularAdapter = new ProductAdapter(requireContext(), popularList, product -> {
//            openProductDetails(product);
//        });
//        popularRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
//        popularRecyclerView.setAdapter(popularAdapter);
//        popularRecyclerView.setNestedScrollingEnabled(false);
//    }
//
//    private void loadCategories() {
//        db.collection("categories")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    categoryList.clear();
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        Category category = new Category(
//                                doc.getId(),
//                                doc.getString("name"),
//                                doc.getString("image"),
//                                doc.getString("color")
//                        );
//                        categoryList.add(category);
//                    }
//                    categoryAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    loadDefaultCategories();
//                });
//    }
//
//    private void loadDefaultCategories() {
//        categoryList.clear();
//        categoryList.add(new Category("1", "Vegetables", "https://example.com/veggies.jpg", "#4CAF50"));
//        categoryList.add(new Category("2", "Fruits", "https://example.com/fruits.jpg", "#FF9800"));
//        categoryList.add(new Category("3", "Dairy", "https://example.com/dairy.jpg", "#2196F3"));
//        categoryList.add(new Category("4", "Organic", "https://example.com/organic.jpg", "#8BC34A"));
//        categoryList.add(new Category("5", "Herbs", "https://example.com/herbs.jpg", "#9C27B0"));
//        categoryAdapter.notifyDataSetChanged();
//    }
//
//    private void loadFeaturedProducts() {
//        db.collection("products")
//                .whereEqualTo("featured", true)
//                .limit(10)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    featuredList.clear();
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        String productId = doc.getId();
//                        String name = doc.getString("name");
//                        String price = doc.getString("price");
//                        String image = doc.getString("image");
//                        String description = doc.getString("description");
//                        String imageUrl = doc.getString("imageUrl");
//                        // ... (other fields)
//                        String category = doc.getString("category");
//                        String subCategory = doc.getString("subCategory");
//                        String brand = doc.getString("brand");
//                        String sku = doc.getString("sku");
//                        Double discountPercentage = doc.getDouble("discountPercentage");
//                        String taxRate = doc.getString("taxRate");
//                        Boolean inStock = doc.getBoolean("inStock");
//                        Boolean featured = doc.getBoolean("featured");
//                        String weight = doc.getString("weight");
//                        String unit = doc.getString("unit");
//                        String minOrderQuantity = doc.getString("minOrderQuantity");
//                        String maxOrderQuantity = doc.getString("maxOrderQuantity");
//                        String sellerName = doc.getString("sellerName");
//                        String sellerId = doc.getString("sellerId");
//                        String barcode = doc.getString("barcode");
//                        Integer stockAlertThreshold = doc.getLong("stockAlertThreshold").intValue();
//                        String expiryDate = doc.getString("expiryDate");
//                        String manufacturingDate = doc.getString("manufacturingDate");
//                        String storageInstructions = doc.getString("storageInstructions");
//                        String returnPolicy = doc.getString("returnPolicy");
//                        String tags = doc.getString("tags");
//                        Boolean organic = doc.getBoolean("organic");
//                        Boolean vegan = doc.getBoolean("vegan");
//                        Boolean glutenFree = doc.getBoolean("glutenFree");
//                        String productCode = doc.getString("productCode");
//
//
//
//
//                        // Use the constructor that matches your ProductModel
//                        ProductModel product = new ProductModel(
//                                productId,
//                                "name",
//                                "description",
//                               "price",
//                                "image",
//                                "rating" ,
//                               "category",
//                               "subCategory",
//                               "brand",
//                               "sku",
//                               discountPercentage,
//                               "taxRate",
//                               inStock,
//                               featured,
//                                weight,
//                               unit,
//                               minOrderQuantity,
//                               maxOrderQuantity,
//                               sellerName,
//                               sellerId,
//                               barcode,
//                               stockAlertThreshold,
//                               "expiryDate",
//                               "manufacturingDate",
//                               "storageInstructions",
//                               "returnPolicy",
//                               "tags",
//                                organic,
//                               vegan,
//                               glutenFree,
//                               "productCode"
//                        );;
//                        featuredList.add(product);
//                    }
//                    featuredAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
////                    loadDefaultFeaturedProducts();
//                });
//    }
//
////    private void loadDefaultFeaturedProducts() {
////        featuredList.clear();
////        featuredList.add(new ProductModel("1", "Fresh Tomatoes", "5.99", "", "Fresh organic tomatoes", 4.5));
////        featuredList.add(new ProductModel("2", "Green Apples", "3.99", "", "Crisp green apples", 4.8));
////        featuredList.add(new ProductModel("3", "Organic Carrots", "2.99", "", "Sweet organic carrots", 4.7));
////        featuredList.add(new ProductModel("4", "Fresh Spinach", "4.99", "", "Nutrient-rich spinach", 4.6));
////        featuredAdapter.notifyDataSetChanged();
////    }
//
//    private void loadPopularProducts() {
//        db.collection("products")
//                .orderBy("rating")
//                .limit(6)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    popularList.clear();
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        String productId = doc.getId();
//                        String name = doc.getString("name");
//                        String price = doc.getString("price");
//                        String image = doc.getString("image");
//                        String description = doc.getString("description");
//                        String imageUrl = doc.getString("imageUrl");
//                        // ... (other fields)
//                        String category = doc.getString("category");
//                        String subCategory = doc.getString("subCategory");
//                        String brand = doc.getString("brand");
//                        String sku = doc.getString("sku");
//                        Double discountPercentage = doc.getDouble("discountPercentage");
//                        String taxRate = doc.getString("taxRate");
//                        Boolean inStock = doc.getBoolean("inStock");
//                        Boolean featured = doc.getBoolean("featured");
//                        String weight = doc.getString("weight");
//                        String unit = doc.getString("unit");
//                        String minOrderQuantity = doc.getString("minOrderQuantity");
//                        String maxOrderQuantity = doc.getString("maxOrderQuantity");
//                        String sellerName = doc.getString("sellerName");
//                        String sellerId = doc.getString("sellerId");
//                        String barcode = doc.getString("barcode");
//                        Integer stockAlertThreshold = doc.getLong("stockAlertThreshold").intValue();
//                        String expiryDate = doc.getString("expiryDate");
//                        String manufacturingDate = doc.getString("manufacturingDate");
//                        String storageInstructions = doc.getString("storageInstructions");
//                        String returnPolicy = doc.getString("returnPolicy");
//                        String tags = doc.getString("tags");
//                        Boolean organic = doc.getBoolean("organic");
//                        Boolean vegan = doc.getBoolean("vegan");
//                        Boolean glutenFree = doc.getBoolean("glutenFree");
//                        String productCode = doc.getString("productCode");
//
//                        ProductModel product = new ProductModel(  productId,
//                                "name",
//                                "description",
//                                "price",
//                                "image",
//                                "rating" ,
//                                "category",
//                                "subCategory",
//                                "brand",
//                                "sku",
//                                discountPercentage,
//                                "taxRate",
//                                inStock,
//                                featured,
//                                weight,
//                                unit,
//                                minOrderQuantity,
//                                maxOrderQuantity,
//                                sellerName,
//                                sellerId,
//                                barcode,
//                                stockAlertThreshold,
//                                "expiryDate",
//                                "manufacturingDate",
//                                "storageInstructions",
//                                "returnPolicy",
//                                "tags",
//                                organic,
//                                vegan,
//                                glutenFree,
//                                "productCode");
//                        popularList.add(product);
//                    }
//                    popularAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
////                    loadDefaultPopularProducts();
//                });
//    }
//
////    private void loadDefaultPopularProducts() {
////        popularList.clear();
////        popularList.add(new ProductModel("1", "Fresh Broccoli", "3.99", "", "Fresh green broccoli", 4.9));
////        popularList.add(new ProductModel("2", "Red Bell Pepper", "2.99", "", "Sweet red peppers", 4.7));
////        popularList.add(new ProductModel("3", "Organic Avocado", "1.99", "", "Creamy avocados", 4.8));
////        popularList.add(new ProductModel("4", "Fresh Strawberries", "4.99", "", "Sweet strawberries", 4.9));
////        popularList.add(new ProductModel("5", "Organic Eggs", "5.99", "", "Free-range eggs", 4.6));
////        popularList.add(new ProductModel("6", "Fresh Milk", "3.99", "", "Organic fresh milk", 4.7));
////        popularAdapter.notifyDataSetChanged();
////    }
//
//    private void loadBanners() {
//        // Add banner images
//        bannerList.add("https://example.com/banner1.jpg");
//        bannerList.add("https://example.com/banner2.jpg");
//        bannerList.add("https://example.com/banner3.jpg");
//
//        // TODO: Setup banner adapter
//        // BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
//        // bannerViewPager.setAdapter(bannerAdapter);
//    }
//
//    private void setupClickListeners() {
//        // User greeting
//        userName.setText("Welcome back,");
//        welcomeText.setText("John Doe!");
//
//        // Profile image click
//        profileImage.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        // Search icon click
//        searchIcon.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "Search clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        // Notification icon click
//        notificationIcon.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "Notifications clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        // Quick category cards
//        veggiesCard.setOnClickListener(v -> {
//            filterByCategory("Vegetables");
//        });
//
//        fruitsCard.setOnClickListener(v -> {
//            filterByCategory("Fruits");
//        });
//
//        dairyCard.setOnClickListener(v -> {
//            filterByCategory("Dairy");
//        });
//
//        organicCard.setOnClickListener(v -> {
//            filterByCategory("Organic");
//        });
//    }
//
//    private void filterByCategory(String category) {
//        Toast.makeText(requireContext(), "Showing " + category, Toast.LENGTH_SHORT).show();
//        // You can implement category filtering here
//    }
//
//    private void openProductDetails(ProductModel product) {
//        Intent intent = new Intent(requireContext(), ProductDetails.class);
//        intent.putExtra("productId", product.getId());
//        startActivity(intent);
//    }
//}