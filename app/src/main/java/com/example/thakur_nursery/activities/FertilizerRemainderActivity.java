package com.example.thakur_nursery.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thakur_nursery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FertilizerRemainderActivity extends AppCompatActivity {

    private EditText plantName, frequency, nextDate;
    private Spinner fertilizerTypeSpinner;
    private Button pickDateButton, saveReminderButton, updateReminderButton, deleteReminderButton, viewRemindersButton;
    private Calendar calendar;

    // Initialize Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_remainder);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        plantName = findViewById(R.id.plant_name);
        fertilizerTypeSpinner = findViewById(R.id.fertilizer_type_spinner);
        frequency = findViewById(R.id.frequency);
        nextDate = findViewById(R.id.next_date);
        pickDateButton = findViewById(R.id.pick_date_button);
        saveReminderButton = findViewById(R.id.save_reminder_button);
        updateReminderButton = findViewById(R.id.update_reminder_button);
        deleteReminderButton = findViewById(R.id.delete_reminder_button);
        viewRemindersButton = findViewById(R.id.view_reminders_button);

        // Create an ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.fertilizer_types,
                android.R.layout.simple_spinner_item
        );
        // Set the layout for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the ArrayAdapter to the Spinner
        fertilizerTypeSpinner.setAdapter(adapter);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Set up date picker
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Save reminder button
        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });

        // Update reminder button
        updateReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReminder();
            }
        });

        // Delete reminder button
        deleteReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReminder();
            }
        });

        // View reminders button
        viewRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewReminder();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                FertilizerRemainderActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveReminder() {
        String plant = plantName.getText().toString();
        String fertilizerType = fertilizerTypeSpinner.getSelectedItem().toString();
        String freq = frequency.getText().toString();
        String date = nextDate.getText().toString();

        // Create a Map to hold the reminder data
        Map<String, Object> reminder = new HashMap<>();
        reminder.put("plantName", plant);
        reminder.put("fertilizerType", fertilizerType);
        reminder.put("frequency", freq);
        reminder.put("nextDate", date);

        // Save the data to Firestore
        db.collection("fertilizer_reminders")
                .add(reminder)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FertilizerRemainderActivity.this, "Reminder saved!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FertilizerRemainderActivity.this, "Error saving reminder: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateReminder() {
        // This method would generally require an identifier to update a specific reminder
        // For now, we will just show a toast message
        Toast.makeText(this, "Reminder updated! (This feature is not yet implemented)", Toast.LENGTH_SHORT).show();
    }

    private void deleteReminder() {
        // This method would generally require an identifier to delete a specific reminder
        // For now, we will just show a toast message
        Toast.makeText(this, "Reminder deleted! (This feature is not yet implemented)", Toast.LENGTH_SHORT).show();
    }

    private void viewReminder() {
        // Create an Intent to start the FertilizerReminderDisplayActivity
        Intent intent = new Intent(FertilizerRemainderActivity.this, FertilizerReminderDisplayActivity.class);

        // Start the activity
        startActivity(intent);
    }
}
