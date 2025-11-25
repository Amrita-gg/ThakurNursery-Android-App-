package com.example.thakur_nursery.models;

public class Reminder {

    private String plantName;
    private String fertilizerType;
    private String frequency;
    private String nextDate;
    private String id; // Optional: If you want to keep track of the document ID

    public Reminder() {
        // Default constructor required for Firestore
    }

    public Reminder(String plantName, String fertilizerType, String frequency, String nextDate) {
        this.plantName = plantName;
        this.fertilizerType = fertilizerType;
        this.frequency = frequency;
        this.nextDate = nextDate;
    }

    // Getters and setters

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(String fertilizerType) {
        this.fertilizerType = fertilizerType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
