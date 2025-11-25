package com.example.thakur_nursery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.adapters.MyCartAdapter;
import com.example.thakur_nursery.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    Button buyNow;
    TextView subTotal, grandTotal;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Toolbar myToolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buyNow = findViewById(R.id.buy_now);
        subTotal = findViewById(R.id.sub_total);
        grandTotal = findViewById(R.id.grand_total);

        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);

        // Check if the user is authenticated
        if (auth.getCurrentUser() == null) {
            Toast.makeText(CartActivity.this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the user is not authenticated
            return;
        }

        // Load cart items from Firestore
        loadCartItems();

        // Set up buy now button click listener
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this,BillGenerateActivity.class));
                finish();
            }
        });
    }

    private void loadCartItems() {
        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                String documentId = doc.getId();
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                if (myCartModel != null) {
                                    myCartModel.setDocumentId(documentId);
                                    cartModelList.add(myCartModel);
                                }
                            }
                            cartAdapter.notifyDataSetChanged();
                            calculateTotalAmount(cartModelList);
                        } else {
                            Toast.makeText(CartActivity.this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
                            finish(); // Optionally, close the activity if data is crucial
                        }
                    }
                });
    }

    public void calculateTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount = 0.0;

        if (cartModelList.isEmpty()) {
            subTotal.setText("₹ 0");
            grandTotal.setText("₹ 0");
        } else {
            for (MyCartModel myCartModel : cartModelList) {
                totalAmount += myCartModel.getTotalPrice();
            }
            subTotal.setText("₹ " + totalAmount);
            grandTotal.setText("₹ " + totalAmount);
        }
    }


}
