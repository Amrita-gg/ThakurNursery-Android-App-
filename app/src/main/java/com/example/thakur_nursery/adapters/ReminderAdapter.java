package com.example.thakur_nursery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thakur_nursery.R;
import com.example.thakur_nursery.models.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private List<Reminder> reminderList;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reminder_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.plantNameTextView.setText(reminder.getPlantName());
        holder.fertilizerTypeTextView.setText(reminder.getFertilizerType());
        holder.frequencyTextView.setText(reminder.getFrequency());
        holder.nextDateTextView.setText(reminder.getNextDate());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView plantNameTextView;
        TextView fertilizerTypeTextView;
        TextView frequencyTextView;
        TextView nextDateTextView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            plantNameTextView = itemView.findViewById(R.id.plant_name_text);
            fertilizerTypeTextView = itemView.findViewById(R.id.fertilizer_type_text);
            frequencyTextView = itemView.findViewById(R.id.frequency_text);
            nextDateTextView = itemView.findViewById(R.id.next_date_text);
        }
    }
}
