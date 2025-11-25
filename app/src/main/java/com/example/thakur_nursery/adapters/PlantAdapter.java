package com.example.thakur_nursery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.models.Plant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context context;
    private List<Plant> plantList;

    public PlantAdapter(Context context, List<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_list, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.plantName.setText(plant.getName());
        holder.growthRequirement.setText(plant.getGrowthRequirement());
        holder.careInstructions.setText(plant.getCareInstructions());

        if (plant.getImageUrl() != null && !plant.getImageUrl().isEmpty()) {
            Picasso.get().load(plant.getImageUrl()).into(holder.plantImageView);
        }
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView plantName, growthRequirement, careInstructions;
        ImageView plantImageView;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.plant_name);
            growthRequirement = itemView.findViewById(R.id.growth_requirement);
            careInstructions = itemView.findViewById(R.id.care_instructions);
            plantImageView = itemView.findViewById(R.id.plant_image_view);
        }
    }
}
