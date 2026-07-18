package com.example.weathernow.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.weathernow.R;
import com.example.weathernow.databinding.ActivityMainBinding;
import com.example.weathernow.model.WeatherData;
import com.example.weathernow.viewmodel.WeatherViewModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final Fragment searchFragment = new SearchFragment();
    private final Fragment savedFragment = new SavedFragment();
    private final Fragment settingsFragment = new SettingsFragment();
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        switchFragment(searchFragment);

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search) {
                switchFragment(searchFragment);
            } else if (itemId == R.id.saved) {
                switchFragment(savedFragment);
            } else if (itemId == R.id.settings) {
                switchFragment(settingsFragment);
            }

            return true;
        });
    }

    private void switchFragment (Fragment fragment) {
        if (activeFragment == fragment) return;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (activeFragment != null) {
            ft.hide(activeFragment);
        }

        if (!fragment.isAdded()) {
            ft.add(R.id.frame_layout, fragment);
        } else {
            ft.show(fragment);
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        activeFragment = fragment;
    }
}