package com.example.thakur_nursery.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thakur_nursery.R;
import com.example.thakur_nursery.models.StockModel; // Ensure the correct package
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private final Context context;
    private final List<StockModel> stockList;
    private final FirebaseFirestore firestore;

    public StockAdapter(Context context, List<StockModel> stockList) {
        this.context = context;
        this.stockList = stockList;
        this.firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockModel stock = stockList.get(position);

        holder.stockName.setText(stock.getname());
        holder.stockAmount.setText("Available Stock: " + stock.getStock());
        Glide.with(context).load(stock.getimg_url()).into(holder.stockImage);

        holder.updateBtn.setOnClickListener(v -> showUpdateStockDialog(stock, holder.stockAmount));
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    private void showUpdateStockDialog(StockModel stockItem, TextView stockTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_stock, null);
        builder.setView(dialogView);

        EditText stockInput = dialogView.findViewById(R.id.stock_input);
        builder.setTitle("Update Stock");

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newStock = stockInput.getText().toString().trim();
            if (!newStock.isEmpty()) {
                try {
                    int updatedStock = Integer.parseInt(newStock);
                    // Find the document ID based on stock name (ensure stock names are unique)
                    updateStockInFirestore(stockItem.getname(), updatedStock);
                    stockTextView.setText("Available Stock: " + updatedStock);
                    stockItem.setStock(updatedStock);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Invalid stock value", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Stock value cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void updateStockInFirestore(String stockName, int newStock) {
        firestore.collection("showAll") // Ensure correct collection name
                .whereEqualTo("name", stockName) // Query by stock name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String documentId = task.getResult().getDocuments().get(0).getId(); // Get document ID
                        firestore.collection("showAll")
                                .document(documentId)
                                .update("stock", newStock)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Stock updated successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error updating stock: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(context, "Stock not found", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stockName, stockAmount;
        ImageView stockImage, updateBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stockName = itemView.findViewById(R.id.stock_name);
            stockAmount = itemView.findViewById(R.id.stock_amount);
            stockImage = itemView.findViewById(R.id.stock_image);
            updateBtn = itemView.findViewById(R.id.update_btn);
        }
    }
}
