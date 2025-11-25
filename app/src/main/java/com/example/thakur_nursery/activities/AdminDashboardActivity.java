package com.example.thakur_nursery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thakur_nursery.R;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
    }

    public void bill(View view){
        startActivity(new Intent(AdminDashboardActivity.this, BillGenerateActivity.class));
        finish();
    }

    public void Ibill(View view){
        startActivity(new Intent(AdminDashboardActivity.this,BillGenerateActivity.class));
        finish();
    }
    public void stock(View view){
        startActivity(new Intent(AdminDashboardActivity.this, StockActivity.class));
        finish();

    }
    public void Istock(View view){
        startActivity(new Intent(AdminDashboardActivity.this, StockActivity.class));
        finish();

    }
    public void alarm(View view){
        startActivity(new Intent(AdminDashboardActivity.this, FertilizerRemainderActivity.class));

    }
    public void Ialarm(View view){
        startActivity(new Intent(AdminDashboardActivity.this,FertilizerRemainderActivity.class));

    }
    public void plant(View view){
        startActivity(new Intent(AdminDashboardActivity.this, PlantInformationActivity.class));

    }
    public void Iplant(View view){
        startActivity(new Intent(AdminDashboardActivity.this,PlantInformationActivity.class));

    }
}