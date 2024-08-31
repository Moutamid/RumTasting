package com.moutamid.rumtasting.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.rumtasting.Constants;
import com.moutamid.rumtasting.R;
import com.moutamid.rumtasting.adapter.RankingAdapter;
import com.moutamid.rumtasting.adapter.RumsAdapter;
import com.moutamid.rumtasting.databinding.FragmentRankingBinding;
import com.moutamid.rumtasting.models.RumModel;

import java.util.ArrayList;
import java.util.Collections;

public class RankingFragment extends Fragment {
    FragmentRankingBinding binding;
    RankingAdapter adapter;

    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRankingBinding.inflate(getLayoutInflater(), container, false);

        binding.rankingRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rankingRC.setHasFixedSize(false);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
        getData();
    }

    private void getData() {
        Constants.showDialog();
        ArrayList<RumModel> list = new ArrayList<>();
        Constants.databaseReference().child(Constants.RUMS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded() && getActivity() != null) {
                    Constants.dismissDialog();
                    list.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            RumModel model = dataSnapshot.getValue(RumModel.class);
                            list.add(model);
                        }
                        list.sort((rum1, rum2) -> {
                            if (rum1.rating == null && rum2.rating == null) return 0;
                            if (rum1.rating == null) return 1;
                            if (rum2.rating == null) return -1;

                            double rating1 = (rum1.rating.star1 + rum1.rating.star2 + rum1.rating.star3 + rum1.rating.star4 + rum1.rating.star5) / 5.0;
                            double rating2 = (rum2.rating.star1 + rum2.rating.star2 + rum2.rating.star3 + rum2.rating.star4 + rum2.rating.star5) / 5.0;
                            return Double.compare(rating2, rating1);
                        });
                    } else {
                        Toast.makeText(requireActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    adapter = new RankingAdapter(requireContext(), list);
                    binding.rankingRC.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.dismissDialog();
                Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}