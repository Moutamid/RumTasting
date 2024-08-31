package com.moutamid.rumtasting.models;

public class RumModel {
    public String id, name, description, image;
    public RatingModel rating;

    public RumModel() {
    }

    public RumModel(String id, String name, String description, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
