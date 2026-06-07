package com.example.weathernow;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathernow.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new SearchFragment());

        binding.bottomNavView.setBackground(null);

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (itemId == R.id.saved) {
                replaceFragment(new SavedFragment());
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
            }

            return true;
        });
    }

    private void replaceFragment (Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }
}