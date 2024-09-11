package com.moutamid.rumtasting.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.adapter.RatedAdapter;
import com.moutamid.rumtasting.databinding.FragmentRatedBinding;
import com.moutamid.rumtasting.models.RumModel;
import com.moutamid.rumtasting.models.UserModel;

import java.util.ArrayList;

public class RatedFragment extends Fragment {
    FragmentRatedBinding binding;
    RatedAdapter adapter;

    public RatedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRatedBinding.inflate(getLayoutInflater(), container, false);

        binding.ratedRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.ratedRC.setHasFixedSize(false);

        binding.search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
        Constants.showDialog();
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(dataSnapshot -> {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        if (userModel.ids == null) userModel.ids = new ArrayList<>();
                        getRated(userModel.ids);
                    }
                });

    }

    private void getRated(ArrayList<String> ids) {
        ArrayList<RumModel> list = new ArrayList<>();
        Constants.databaseReference().child(Constants.RUMS)
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RumModel model = snapshot.getValue(RumModel.class);
                            if (model != null && ids.contains(model.id)) {
                                list.add(model);
                            }
                        }
                    } else {
                        Toast.makeText(requireActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    adapter = new RatedAdapter(requireContext(), list);
                    binding.ratedRC.setAdapter(adapter);
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}