package com.moutamid.rumtasting.models;

public class UserModel {
    public String ID, name, email, password;

    public UserModel() {
    }

    public UserModel(String ID, String name, String email, String password) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
