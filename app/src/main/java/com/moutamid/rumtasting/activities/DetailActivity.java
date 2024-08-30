package com.moutamid.rumtasting.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.databinding.ActivityDetailBinding;
import com.moutamid.rumtasting.models.RumModel;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    String ID;
    RumModel rumModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ID = getIntent().getStringExtra(Constants.RUMS);

        Constants.databaseReference().child(Constants.RUMS).child(ID)
                .get().addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(dataSnapshot -> {
                   rumModel = dataSnapshot.getValue(RumModel.class);
                   updateView();
                });
    }

    private void updateView() {
        binding.name.setText(rumModel.name);
        binding.description.setText(rumModel.description);
        Glide.with(this).load(rumModel.image).placeholder(R.color.background).into(binding.profile);
    }
}