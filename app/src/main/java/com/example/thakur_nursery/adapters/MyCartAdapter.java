package com.example.thakur_nursery.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.activities.CartActivity;
import com.example.thakur_nursery.models.MyCartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    private Context context;
    private List<MyCartModel> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCartModel currentItem = list.get(position);

        // Set the product details
        holder.price.setText("Rs. " + currentItem.getProductPrice());
        holder.name.setText(currentItem.getProductName());
        holder.totalPrice.setText("Rs. " + currentItem.getTotalPrice());
        holder.totalQuantity.setText(currentItem.getTotalQuantity()); // Corrected to directly set the String value
        //Glide.with(context).load(currentItem.getProduct_image()).into(holder.product_image);

        // Handle the delete operation
        holder.deleteItem.setOnClickListener(v -> deleteItemFromCart(currentItem.getDocumentId(), position));

        // Handle the addition of item quantity
        holder.addButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(currentItem.getTotalQuantity());
            int newQuantity = currentQuantity + 1;
            updateCartItem(currentItem, newQuantity);
        });

        // Handle the removal of item quantity
        holder.removeButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(currentItem.getTotalQuantity());
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                updateCartItem(currentItem, newQuantity);
            } else {
                // Remove item from the cart if quantity reaches 0 or 1
                deleteItemFromCart(currentItem.getDocumentId(), position);
            }
        });
    }

    private void updateCartItem(MyCartModel item, int newQuantity) {
        // Convert newQuantity to String for Firestore
        String newQuantityString = String.valueOf(newQuantity);
        // Calculate new total price based on the new quantity
        int newTotalPrice = newQuantity * Integer.parseInt(item.getProductPrice());

        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .document(item.getDocumentId())
                .update("TotalQuantity", newQuantityString, "totalPrice", newTotalPrice)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update the model with the new values
                        item.setTotalQuantity(newQuantityString); // Update quantity in the model
                        item.setTotalPrice(newTotalPrice); // Update total price in the model
                        notifyDataSetChanged(); // Notify the adapter to refresh the UI

                        // Notify the CartActivity to recalculate the total amount
                        if (context instanceof CartActivity) {
                            ((CartActivity) context).calculateTotalAmount(list);
                        }
                    } else {
                        // Log or handle error
                        Log.e("CartAdapter", "Error updating cart item", task.getException());
                        Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, totalQuantity, totalPrice;
        ImageView deleteItem, removeButton, addButton, product_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            totalQuantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
            removeButton = itemView.findViewById(R.id.remove_item);
            addButton = itemView.findViewById(R.id.add_item);
            deleteItem = itemView.findViewById(R.id.delete);
        }
    }

    private void deleteItemFromCart(String documentId, int position) {
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User")
                .document(documentId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();

                        // Recalculate the total amount in CartActivity
                        if (context instanceof CartActivity) {
                            ((CartActivity) context).calculateTotalAmount(list);
                        }
                    } else {
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
