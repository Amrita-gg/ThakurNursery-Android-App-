package com.example.thakur_nursery.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thakur_nursery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlantInformationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText plantId, plantName, growthRequirement, careInstructions;
    private Button savePlantButton, updatePlantButton, viewPlantButton, chooseImageButton;
    private ImageView plantImageView;
    private Uri plantImageUri;
    private FirebaseFirestore db;
    private String currentPlantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_information);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        plantId = findViewById(R.id.plant_id);
        plantName = findViewById(R.id.plant_name);
        growthRequirement = findViewById(R.id.growth_requirement);
        careInstructions = findViewById(R.id.care_instructions);
        plantImageView = findViewById(R.id.plant_image_view);
        chooseImageButton = findViewById(R.id.choose_image_button);
        savePlantButton = findViewById(R.id.save_plant_button);
        updatePlantButton = findViewById(R.id.update_plant_button);
        viewPlantButton = findViewById(R.id.view_plant_button);

        // Get plant ID from intent or other source if needed
        currentPlantId = getIntent().getStringExtra("PLANT_ID");

        // Set up button click listeners
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        savePlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlant();
            }
        });

        updatePlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlant();
            }
        });

        viewPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlantInformationActivity.this, PlantListActivity.class));
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            plantImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), plantImageUri);
                plantImageView.setImageBitmap(bitmap); // Display the selected image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePlant() {
        String id = plantId.getText().toString().trim();
        String name = plantName.getText().toString().trim();
        String growth = growthRequirement.getText().toString().trim();
        String care = careInstructions.getText().toString().trim();

        if (!id.isEmpty() && !name.isEmpty() && !growth.isEmpty() && !care.isEmpty()) {
            Map<String, Object> plant = new HashMap<>();
            plant.put("id", id);
            plant.put("name", name);
            plant.put("growthRequirement", growth);
            plant.put("careInstructions", care);
            // You can also add image URL if you store it in Firebase Storage.

            db.collection("plants").document(id)
                    .set(plant)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PlantInformationActivity.this, "Plant saved!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlantInformationActivity.this, "Error saving plant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePlant() {
        String id = plantId.getText().toString().trim();
        String name = plantName.getText().toString().trim();
        String growth = growthRequirement.getText().toString().trim();
        String care = careInstructions.getText().toString().trim();

        if (!id.isEmpty() && !name.isEmpty() && !growth.isEmpty() && !care.isEmpty()) {
            Map<String, Object> plant = new HashMap<>();
            plant.put("name", name);
            plant.put("growthRequirement", growth);
            plant.put("careInstructions", care);
            // You can also update the image URL if changed.

            db.collection("plants").document(id)
                    .update(plant)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PlantInformationActivity.this, "Plant updated!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlantInformationActivity.this, "Error updating plant: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
