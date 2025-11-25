package com.example.thakur_nursery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thakur_nursery.R;

public class UserdashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdashboard);
    }

    public void plant(View view){
        startActivity(new Intent(UserdashboardActivity.this, PlantRecognitionActivity.class));
        finish();
    }
    public void feedback(View view){
        startActivity(new Intent(UserdashboardActivity.this, FeedbackActivity.class));
    }
}