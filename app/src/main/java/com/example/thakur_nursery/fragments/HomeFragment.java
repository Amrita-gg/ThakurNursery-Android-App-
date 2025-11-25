package com.example.thakur_nursery.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.thakur_nursery.R;
import com.example.thakur_nursery.activities.ShowAllActivity;
import com.example.thakur_nursery.adapters.CategoryAdapter;
import com.example.thakur_nursery.adapters.NewProductsAdapter;
import com.example.thakur_nursery.adapters.PopularProductsAdapter;
import com.example.thakur_nursery.models.CategoryModel;
import com.example.thakur_nursery.models.NewProductsModel;
import com.example.thakur_nursery.models.PopularProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll,popularShowAll,newProductShowAll;
    RecyclerView catRecyclerview, newProductRecyclerview, popularRecyclerview;
    CategoryAdapter categoryAdapter;
    NewProductsAdapter newProductsAdapter;
    PopularProductsAdapter popularProductsAdapter;
    List<CategoryModel> categoryModelList;

    ProgressDialog progressDialog;
    List<NewProductsModel> newProductsModelList;
    List<PopularProductsModel> popularProductsModelList;
    FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        // Set the toolbar title or customize it
//        if (getActivity() != null) {
//            AppCompatActivity activity = (AppCompatActivity) getActivity();
//            if (activity.getSupportActionBar() != null) {
//                activity.getSupportActionBar().setTitle("Home");
//                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // Disable back button
//            }
//        }


        // Initialize RecyclerView and Firestore
        progressDialog = new ProgressDialog(getActivity());
        catRecyclerview = root.findViewById(R.id.rec_category);
        newProductRecyclerview = root.findViewById(R.id.new_product_rec); // Initialize the RecyclerView for new products
        popularRecyclerview = root.findViewById(R.id.popular_rec); // Initialize RecyclerView for popular products
        db = FirebaseFirestore.getInstance();
        catShowAll = root.findViewById(R.id.category_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowAll = root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ShowAllActivity.class);
                startActivity(i);
            }
        });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ShowAllActivity.class);
                startActivity(i);
            }
        });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ShowAllActivity.class);
                startActivity(i);
            }
        });




        // Image Slider setup
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.p6, "", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.p8, "Cart Available", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.p5, "Discount", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.p7, "SHOP NOW", ScaleTypes.CENTER_CROP));

        // Set image list to the slider
        imageSlider.setImageList(slideModels);

        progressDialog.setTitle("Welcome To ThakurNursery");
        progressDialog.setMessage("Please wait......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Setup RecyclerView for categories
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                            }
                            categoryAdapter.notifyDataSetChanged(); // Moved out of the loop
                            progressDialog.dismiss(); // Moved out of the loop
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Setup RecyclerView for new products
        newProductRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getContext(), newProductsModelList);
        newProductRecyclerview.setAdapter(newProductsAdapter);

        db.collection("newProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                            }
                            newProductsAdapter.notifyDataSetChanged(); // Moved out of the loop
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Popular product RecyclerView setup
        popularRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularProductsModelList = new ArrayList<>();
        popularProductsAdapter = new PopularProductsAdapter(getContext(), popularProductsModelList);
        popularRecyclerview.setAdapter(popularProductsAdapter);

        db.collection("allProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularProductsModel popularProductsModel = document.toObject(PopularProductsModel.class);
                                popularProductsModelList.add(popularProductsModel);
                            }
                            popularProductsAdapter.notifyDataSetChanged(); // Moved out of the loop
                        } else {
                            Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }
}
