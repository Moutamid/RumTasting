package com.moutamid.rumtasting.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.adapter.RumsAdapter;
import com.moutamid.rumtasting.databinding.FragmentFavoriteBinding;
import com.moutamid.rumtasting.models.RumModel;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    FragmentFavoriteBinding binding;
    RumsAdapter adapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater(), container, false);

        binding.RC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.RC.setHasFixedSize(false);

        ArrayList<RumModel> favorite = Stash.getArrayList(Constants.FAVORITES, RumModel.class);

        adapter = new RumsAdapter(requireContext(), favorite);
        binding.RC.setAdapter(adapter);

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
}