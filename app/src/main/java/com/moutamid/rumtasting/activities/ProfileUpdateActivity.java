package com.moutamid.rumtasting.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.databinding.ActivityProfileUpdateBinding;
import com.moutamid.rumtasting.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileUpdateActivity extends AppCompatActivity {
    ActivityProfileUpdateBinding binding;
    Uri imageURI;
    UserModel userModel;
    private static final int PICK_FROM_GALLERY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);

        binding.toolbar.title.setText("Profile Update");
        binding.toolbar.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(dataSnapshot -> {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    binding.name.getEditText().setText(userModel.name);
                    binding.email.getEditText().setText(userModel.email);
                    Glide.with(this).load(userModel.image).placeholder(R.drawable.profile_icon).into(binding.profile);
                });

        binding.profile.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(PICK_FROM_GALLERY);
        });

        binding.update.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                if (imageURI == null) {
                    uploadData(userModel.image);
                } else {
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid()).child("images").child(new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date())).putFile(imageURI)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadData(uri.toString());
                    });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadData(String img) {
        userModel.image = img;
        userModel.name = binding.name.getEditText().getText().toString();

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(userModel)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    getOnBackPressedDispatcher().onBackPressed();
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private boolean valid() {
        if (binding.name.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            Glide.with(this).load(imageURI).placeholder(R.drawable.profile_icon).into(binding.profile);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}