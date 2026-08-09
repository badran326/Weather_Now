package com.example.weathernow.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.weathernow.BuildConfig;
import com.example.weathernow.R;
import com.example.weathernow.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    public static final String PREFS_NAME = "weather_settings";

    public static final String KEY_TEMPERATURE_UNIT =
            "temperature_unit";

    public static final String KEY_THEME =
            "theme_mode";

    public static final String UNIT_CELSIUS = "C";
    public static final String UNIT_FAHRENHEIT = "F";

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";

    private FragmentSettingsBinding binding;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentSettingsBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        preferences =
                requireContext().getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        setupAccount();
        setupVersion();
        setupTemperaturePreference();
        setupThemePreference();

        binding.btnSignOut.setOnClickListener(
                v -> signOut()
        );

        // Keep Assignment 1 functionality
        binding.btnSendFeedback.setOnClickListener(
                v -> sendFeedback()
        );

        binding.btnViewGithub.setOnClickListener(
                v -> viewOnGithub()
        );

        binding.btnShareApp.setOnClickListener(
                v -> shareApp()
        );
    }

    private void setupAccount() {

        FirebaseUser currentUser =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (currentUser != null &&
                currentUser.getEmail() != null) {

            binding.tvUserEmail.setText(
                    currentUser.getEmail()
            );

        } else {

            binding.tvUserEmail.setText(
                    "No user signed in"
            );
        }
    }

    private void setupVersion() {

        binding.tvVersion.setText(
                "Version " + BuildConfig.VERSION_NAME
        );
    }

    private void setupTemperaturePreference() {

        String unit =
                preferences.getString(
                        KEY_TEMPERATURE_UNIT,
                        UNIT_CELSIUS
                );

        if (UNIT_FAHRENHEIT.equals(unit)) {

            binding.toggleTemperature.check(
                    R.id.btnFahrenheit
            );

        } else {

            binding.toggleTemperature.check(
                    R.id.btnCelsius
            );
        }

        binding.toggleTemperature
                .addOnButtonCheckedListener(
                        (group, checkedId, isChecked) -> {

                            if (!isChecked) {
                                return;
                            }

                            String selectedUnit;

                            if (checkedId ==
                                    R.id.btnFahrenheit) {

                                selectedUnit =
                                        UNIT_FAHRENHEIT;

                            } else {

                                selectedUnit =
                                        UNIT_CELSIUS;
                            }

                            preferences
                                    .edit()
                                    .putString(
                                            KEY_TEMPERATURE_UNIT,
                                            selectedUnit
                                    )
                                    .apply();

                            Toast.makeText(
                                    requireContext(),
                                    selectedUnit.equals(
                                            UNIT_CELSIUS
                                    )
                                            ? "Temperature set to Celsius"
                                            : "Temperature set to Fahrenheit",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );
    }

    private void setupThemePreference() {

        String theme =
                preferences.getString(
                        KEY_THEME,
                        THEME_LIGHT
                );

        if (THEME_DARK.equals(theme)) {

            binding.toggleTheme.check(
                    R.id.btnDark
            );

        } else {

            binding.toggleTheme.check(
                    R.id.btnLight
            );
        }

        binding.toggleTheme
                .addOnButtonCheckedListener(
                        (group, checkedId, isChecked) -> {

                            if (!isChecked) {
                                return;
                            }

                            if (checkedId ==
                                    R.id.btnDark) {

                                preferences
                                        .edit()
                                        .putString(
                                                KEY_THEME,
                                                THEME_DARK
                                        )
                                        .apply();

                                AppCompatDelegate
                                        .setDefaultNightMode(
                                                AppCompatDelegate
                                                        .MODE_NIGHT_YES
                                        );

                            } else {

                                preferences
                                        .edit()
                                        .putString(
                                                KEY_THEME,
                                                THEME_LIGHT
                                        )
                                        .apply();

                                AppCompatDelegate
                                        .setDefaultNightMode(
                                                AppCompatDelegate
                                                        .MODE_NIGHT_NO
                                        );
                            }
                        }
                );
    }

    private void signOut() {

        FirebaseAuth.getInstance().signOut();

        Toast.makeText(
                requireContext(),
                "Signed out successfully",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        requireContext(),
                        LoginActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        requireActivity().finish();
    }

    private void sendFeedback() {

        Intent intent =
                new Intent(Intent.ACTION_SENDTO);

        intent.setData(
                Uri.parse("mailto:")
        );

        intent.putExtra(
                Intent.EXTRA_EMAIL,
                new String[]{
                        "support@weathernow.com"
                }
        );

        intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "WeatherNow Feedback"
        );

        try {

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    requireContext(),
                    "No email app installed",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void viewOnGithub() {

        Intent intent =
                new Intent(Intent.ACTION_VIEW);

        intent.setData(
                Uri.parse("https://github.com/badran326/Weather_Now")
        );

        try {

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    requireContext(),
                    "No browser available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void shareApp() {

        Intent intent =
                new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");

        intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out WeatherNow!"
        );

        startActivity(
                Intent.createChooser(
                        intent,
                        "Share via"
                )
        );
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}