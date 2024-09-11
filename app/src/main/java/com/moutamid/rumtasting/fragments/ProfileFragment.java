package com.moutamid.rumtasting.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.activities.ProfileUpdateActivity;
import com.moutamid.rumtasting.activities.SplashActivity;
import com.moutamid.rumtasting.databinding.FragmentProfileBinding;
import com.moutamid.rumtasting.models.UserModel;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);

        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        Constants.auth().signOut();
                        startActivity(new Intent(requireContext(), SplashActivity.class));
                        requireActivity().finish();
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        binding.edit.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ProfileUpdateActivity.class));
        });

        binding.privacy.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")));
        });

        return binding.getRoot();
    }

    private static final String TAG = "ProfileFragment";

    @Override
    public void onResume() {
        super.onResume();
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(dataSnapshot -> {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        binding.name.setText(userModel.name);
                        binding.email.setText(userModel.email);
                        if (userModel.ids == null) userModel.ids = new ArrayList<>();
                        binding.totalRum.setText(String.format("%,d", userModel.ids.size()));
                        Glide.with(requireContext()).load(userModel.image).placeholder(R.drawable.profile_icon).into(binding.profile);
                    }
                });
    }
}