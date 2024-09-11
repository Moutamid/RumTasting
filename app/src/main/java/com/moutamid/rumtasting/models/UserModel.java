package com.moutamid.rumtasting.models;

import java.util.ArrayList;

public class UserModel {
    public String ID, name, email, password, image;
    public ArrayList<String> ids;

    public UserModel() {
    }

    public UserModel(String ID, String name, String email, String password, String image) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }
}
