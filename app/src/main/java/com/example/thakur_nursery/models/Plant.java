package com.example.thakur_nursery.models;

public class Plant {
    private String id;
    private String name;
    private String growthRequirement;
    private String careInstructions;
    private String imageUrl;

    public Plant() {
        // Default constructor required for Firebase
    }

    public Plant(String id, String name, String growthRequirement, String careInstructions, String imageUrl) {
        this.id = id;
        this.name = name;
        this.growthRequirement = growthRequirement;
        this.careInstructions = careInstructions;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getGrowthRequirement() { return growthRequirement; }
    public String getCareInstructions() { return careInstructions; }
    public String getImageUrl() { return imageUrl; }
}
