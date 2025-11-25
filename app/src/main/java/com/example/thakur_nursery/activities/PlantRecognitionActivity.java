package com.example.thakur_nursery.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thakur_nursery.PlantApiService;
import com.example.thakur_nursery.PlantResponse;
import com.example.thakur_nursery.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantRecognitionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private TextView textViewPlantName, textViewHealthStatus;
    private ImageView imageViewPlant;
    private PlantApiService plantApiService;
    String apiKey = "REpPKLqbcro4oaJyXQE8vwvWCJBNRdEaGBVYXlT06W7VXGo09F";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_recognition);

        textViewPlantName = findViewById(R.id.textViewPlantName);
        textViewHealthStatus = findViewById(R.id.textViewHealthStatus);
        imageViewPlant = findViewById(R.id.imageViewPlant);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plant.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        plantApiService = retrofit.create(PlantApiService.class);

        openGallery();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageViewPlant.setImageURI(imageUri);
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            File tempFile = createTempFileFromUri(inputStream);

            RequestBody requestFile = RequestBody.create(tempFile, MediaType.parse("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", tempFile.getName(), requestFile);
            String details = "common_names,url,description,taxonomy,rank,gbif_id,inaturalist_id,image,synonyms,edible_parts,watering";
            String language = "en";

            Call<PlantResponse> call = plantApiService.identifyPlant(apiKey, body, details, language);
            call.enqueue(new Callback<PlantResponse>() {
                @Override
                public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                    if (response.isSuccessful()) {
                        try {
                            String rawJson = new Gson().toJson(response.body());
                            Log.d("API Response", rawJson);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PlantResponse plantResponse = response.body();
                        showPlantInfo(plantResponse);
                    } else {
                        Log.e("API Response", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<PlantResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createTempFileFromUri(InputStream inputStream) throws Exception {
        File tempFile = File.createTempFile("upload", ".jpg", getCacheDir());
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        }
        return tempFile;
    }

    private void showPlantInfo(PlantResponse plantResponse) {
        if (plantResponse != null &&
                plantResponse.getResult() != null &&
                plantResponse.getResult().getClassification() != null &&
                !plantResponse.getResult().getClassification().getSuggestions().isEmpty()) {

            PlantResponse.Result.Classification.Suggestion suggestion = plantResponse.getResult().getClassification().getSuggestions().get(0);
            PlantResponse.Result.Classification.Suggestion.Details details = suggestion.getDetails();

            if (details != null) {
                String plantName = (details.getCommonNames() != null && !details.getCommonNames().isEmpty()) ? details.getCommonNames().get(0) : suggestion.getName();
                textViewPlantName.setText(plantName);

                PlantResponse.Result.Classification.Suggestion.Details.Description description = details.getDescription();
                if (description != null) {
                    String plantDescription = description.getValue();
                    textViewHealthStatus.setText(plantDescription != null ? plantDescription : "Description not available");
                } else {
                    textViewHealthStatus.setText("Description not available");
                }
            } else {
                textViewPlantName.setText("Details not available");
                textViewHealthStatus.setText("Details not available");
            }
        } else {
            textViewPlantName.setText("No plant found");
            textViewHealthStatus.setText("Details not found");
        }
    }
}