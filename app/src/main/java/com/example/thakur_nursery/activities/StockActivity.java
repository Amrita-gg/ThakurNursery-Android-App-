package com.example.thakur_nursery.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.adapters.StockAdapter;
import com.example.thakur_nursery.models.StockModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView stockRecyclerView;
    private StockAdapter stockAdapter;
    private List<StockModel> stockList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        // Initialize Firebase services
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize RecyclerView and Adapter
        stockRecyclerView = findViewById(R.id.stock_recycler_view);
        stockRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        stockList = new ArrayList<>();
        stockAdapter = new StockAdapter(this, stockList);
        stockRecyclerView.setAdapter(stockAdapter);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading stock data...");
        progressDialog.setCancelable(false);

        // Show progress dialog and load data
        showProgressDialog();
        loadStockData();
    }

    private void loadStockData() {
        firestore.collection("showAll") // Ensure the collection name is correct
                .get()
                .addOnCompleteListener(task -> {
                    dismissProgressDialog(); // Dismiss the dialog in any case
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null && snapshot.isEmpty()) {
                            Toast.makeText(StockActivity.this, "No stock data found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                StockModel stockModel = document.toObject(StockModel.class);
                                if (stockModel != null) {
                                    // No longer setting Id as it doesn't exist in StockModel
                                    stockList.add(stockModel);
                                }
                            }
                            stockAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                        }
                    } else {
                        Toast.makeText(StockActivity.this, "Failed to load stock data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog(); // Ensure dialog is dismissed
    }
}
