package com.moutamid.rumtasting;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.rumtasting.databinding.ActivityMainBinding;
import com.moutamid.rumtasting.fragments.FavoriteFragment;
import com.moutamid.rumtasting.fragments.HomeFragment;
import com.moutamid.rumtasting.fragments.ProfileFragment;
import com.moutamid.rumtasting.fragments.RankingFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getColor(R.color.background)));
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
                }
                if (id == R.id.favorite) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FavoriteFragment()).commit();
                }
                if (id == R.id.ranking) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RankingFragment()).commit();
                }
                if (id == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProfileFragment()).commit();
                }
                return true;
            }
        });

        binding.bottomNav.setSelectedItemId(R.id.home);

    }
}