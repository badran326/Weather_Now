package com.example.weathernow;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weathernow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Fragment searchFragment = new SearchFragment();
    private final Fragment savedFragment = new SavedFragment();
    private final Fragment settingsFragment = new SettingsFragment();
    private Fragment activeFragment = searchFragment;
    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(0, systemBars.top, 0, 0);
            binding.bottomNavigation.setPadding(0, 0, 0, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("WeatherNow");
        }

        // Initial setup of fragments
        fm.beginTransaction().add(R.id.fragmentContainer, settingsFragment, "3").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, savedFragment, "2").hide(savedFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, searchFragment, "1").commit();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_search) {
                fm.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                activeFragment = searchFragment;
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("WeatherNow");
                return true;
            } else if (itemId == R.id.nav_saved) {
                fm.beginTransaction().hide(activeFragment).show(savedFragment).commit();
                activeFragment = savedFragment;
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("Saved Cities");
                return true;
            } else if (itemId == R.id.nav_settings) {
                fm.beginTransaction().hide(activeFragment).show(settingsFragment).commit();
                activeFragment = settingsFragment;
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("Settings");
                return true;
            }
            return false;
        });
    }
}