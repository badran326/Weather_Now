package com.example.weathernow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weathernow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final Fragment searchFragment = new SearchFragment();
    private final Fragment savedFragment = new SavedFragment();
    private final Fragment settingsFragment = new SettingsFragment();
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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