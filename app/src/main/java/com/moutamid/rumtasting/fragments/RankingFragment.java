package com.moutamid.rumtasting.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.databinding.FragmentRankingBinding;

public class RankingFragment extends Fragment {
    FragmentRankingBinding binding;
    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRankingBinding.inflate(getLayoutInflater(), container, false);



        return binding.getRoot();
    }
}