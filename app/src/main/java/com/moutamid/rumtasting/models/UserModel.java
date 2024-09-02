package com.moutamid.rumtasting.models;

public class UserModel {
    public String ID, name, email, password, image;
    public int totalRated;

    public UserModel() {
    }

    public UserModel(String ID, String name, String email, String password, String image, int totalRated) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.totalRated = totalRated;
    }
}
