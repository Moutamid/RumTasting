package com.moutamid.rumtasting.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.databinding.ActivityDetailBinding;
import com.moutamid.rumtasting.models.RatingModel;
import com.moutamid.rumtasting.models.RumModel;
import com.moutamid.rumtasting.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    String ID;
    RumModel rumModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);
        binding.toolbar.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ID = getIntent().getStringExtra(Constants.RUMS);

        Constants.databaseReference().child(Constants.RUMS).child(ID)
                .get().addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        rumModel = dataSnapshot.getValue(RumModel.class);
                        updateView();
                    } else {
                        Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                });

        binding.rate.setOnClickListener(v -> {
            showRating();
        });

    }

    private void showRating() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rum_rider);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        Button submit = dialog.findViewById(R.id.submit);
        MaterialRatingBar ratingBar = dialog.findViewById(R.id.ratingBar);

        submit.setOnClickListener(v -> {
            if (rumModel.rating == null) {
                rumModel.rating = new ArrayList<>();
            }
            rumModel.rating.add(ratingBar.getRating());
            dialog.dismiss();
            addRating();
        });
    }

    private void addRating() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.RUMS).child(rumModel.id).setValue(rumModel)
                .addOnSuccessListener(unused -> {
                    Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                            .get().addOnFailureListener(e -> {
                                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnSuccessListener(dataSnapshot -> {
                                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                Map<String, Object> map = new HashMap<>();
                                map.put("totalRated", (userModel.totalRated + 1));
                                Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map)
                                        .addOnSuccessListener(unused1 -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(this, "Thanks for your rating", Toast.LENGTH_SHORT).show();
                                            updateRating();
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateRating() {
        if (rumModel.rating != null) {
            double star = 0;
            for (Float rate : rumModel.rating) {
                star += rate;
            }
            star = star / rumModel.rating.size();
            binding.rating.setText(String.format("%.2f", star));
        }
    }

    private void updateView() {
        binding.name.setText(rumModel.name);
        binding.description.setText(rumModel.description);
        Glide.with(this).load(rumModel.image).placeholder(R.color.background).into(binding.profile);

        if (rumModel.rating != null) {
            double star = 0;
            for (Float rate : rumModel.rating) {
                star += rate;
            }
            star = star / rumModel.rating.size();
            binding.rating.setText(String.format("%.2f", star));
        }

        binding.profile.setOnClickListener(v ->  {
           previewImage();
        });
    }

    private void previewImage() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imageviewer);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.show();

        ImageView image = dialog.findViewById(R.id.image);
        MaterialCardView back = dialog.findViewById(R.id.back);
        TextView name = dialog.findViewById(R.id.name);

        name.setText(rumModel.name);
        back.setOnClickListener(v -> dialog.dismiss());
        Glide.with(this).load(rumModel.image).placeholder(R.color.background).into(image);
    }
}