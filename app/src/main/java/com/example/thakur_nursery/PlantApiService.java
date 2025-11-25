package com.example.thakur_nursery;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PlantApiService {
    @Multipart
    @POST("api/v3/identification")
    Call<PlantResponse> identifyPlant(
            @Header("Api-Key") String apiKey,
            @Part MultipartBody.Part image,
            @Query("details") String details,
            @Query("language") String language
    );
}
