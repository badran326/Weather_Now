package com.example.weathernow.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weathernow.R;
import com.example.weathernow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_SELECTED_TAB = "selected_tab";

    private static final String TAG_SEARCH = "search_fragment";
    private static final String TAG_SAVED = "saved_fragment";
    private static final String TAG_SETTINGS = "settings_fragment";

    private ActivityMainBinding binding;

    private Fragment searchFragment;
    private Fragment savedFragment;
    private Fragment settingsFragment;

    private Fragment activeFragment;

    private int selectedTabId = R.id.search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(
                getLayoutInflater()
        );

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(
                binding.main,
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        FragmentManager fragmentManager =
                getSupportFragmentManager();

        // Reuse fragments restored by Android after
        // configuration changes such as Light/Dark mode.
        searchFragment =
                fragmentManager.findFragmentByTag(TAG_SEARCH);

        savedFragment =
                fragmentManager.findFragmentByTag(TAG_SAVED);

        settingsFragment =
                fragmentManager.findFragmentByTag(TAG_SETTINGS);

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        if (savedFragment == null) {
            savedFragment = new SavedFragment();
        }

        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }

        // Restore the selected tab after Activity recreation.
        if (savedInstanceState != null) {

            selectedTabId =
                    savedInstanceState.getInt(
                            KEY_SELECTED_TAB,
                            R.id.search
                    );
        }

        binding.bottomNavView.setOnItemSelectedListener(
                item -> {

                    selectedTabId = item.getItemId();

                    showSelectedFragment(selectedTabId);

                    return true;
                }
        );

        // Visually restore the correct selected navigation item.
        binding.bottomNavView
                .getMenu()
                .findItem(selectedTabId)
                .setChecked(true);

        // Restore the correct Fragment.
        showSelectedFragment(selectedTabId);
    }

    private void showSelectedFragment(int itemId) {

        Fragment fragment;
        String tag;

        if (itemId == R.id.saved) {

            fragment = savedFragment;
            tag = TAG_SAVED;

        } else if (itemId == R.id.settings) {

            fragment = settingsFragment;
            tag = TAG_SETTINGS;

        } else {

            fragment = searchFragment;
            tag = TAG_SEARCH;
        }

        switchFragment(fragment, tag);
    }

    private void switchFragment(
            Fragment fragment,
            String tag
    ) {

        if (activeFragment == fragment) {
            return;
        }

        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction();

        // Hide all existing fragments.
        if (searchFragment != null &&
                searchFragment.isAdded() &&
                searchFragment != fragment) {

            transaction.hide(searchFragment);
        }

        if (savedFragment != null &&
                savedFragment.isAdded() &&
                savedFragment != fragment) {

            transaction.hide(savedFragment);
        }

        if (settingsFragment != null &&
                settingsFragment.isAdded() &&
                settingsFragment != fragment) {

            transaction.hide(settingsFragment);
        }

        // Add the Fragment the first time,
        // otherwise simply show it.
        if (!fragment.isAdded()) {

            transaction.add(
                    R.id.frame_layout,
                    fragment,
                    tag
            );

        } else {

            transaction.show(fragment);
        }

        transaction
                .setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE
                )
                .commit();

        activeFragment = fragment;
    }

    @Override
    protected void onSaveInstanceState(
            Bundle outState
    ) {

        outState.putInt(
                KEY_SELECTED_TAB,
                selectedTabId
        );

        super.onSaveInstanceState(outState);
    }
}