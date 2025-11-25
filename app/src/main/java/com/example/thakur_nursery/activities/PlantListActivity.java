package com.example.thakur_nursery.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.adapters.PlantAdapter;
import com.example.thakur_nursery.models.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlantAdapter plantAdapter;
    private List<Plant> plantList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        recyclerView = findViewById(R.id.plants_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        plantList = new ArrayList<>();
        plantAdapter = new PlantAdapter(this, plantList);
        recyclerView.setAdapter(plantAdapter);

        db = FirebaseFirestore.getInstance();

        fetchPlants();
    }

    private void fetchPlants() {
        db.collection("plants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Plant plant = document.toObject(Plant.class);
                                plantList.add(plant);
                            }
                            plantAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
