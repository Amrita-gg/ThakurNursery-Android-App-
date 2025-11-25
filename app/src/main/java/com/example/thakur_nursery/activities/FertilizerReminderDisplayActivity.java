package com.example.thakur_nursery.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.models.Reminder;
import com.example.thakur_nursery.adapters.ReminderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FertilizerReminderDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<Reminder> reminderList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_reminder_display);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.reminder_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderList = new ArrayList<>();
        adapter = new ReminderAdapter(this, reminderList);
        recyclerView.setAdapter(adapter);

        // Fetch reminders from Firestore
        fetchReminders();
    }

    private void fetchReminders() {
        db.collection("fertilizer_reminders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reminderList.clear();
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                Reminder reminder = document.toObject(Reminder.class);
                                reminder.setId(document.getId()); // Optionally set the document ID
                                reminderList.add(reminder);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(FertilizerReminderDisplayActivity.this, "Error getting reminders: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
