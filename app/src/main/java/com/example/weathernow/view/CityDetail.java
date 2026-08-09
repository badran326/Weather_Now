package com.example.weathernow.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.widget.Toast;

import com.example.weathernow.R;
import com.example.weathernow.databinding.ActivityCityDetailBinding;
import com.example.weathernow.model.WeatherData;
import com.example.weathernow.model.WeatherLocation;
import com.example.weathernow.viewmodel.WeatherViewModel;
import com.example.weathernow.model.SavedLocation;
import com.example.weathernow.repository.SavedLocationRepository;

import java.util.Locale;

public class CityDetail extends AppCompatActivity {

    ActivityCityDetailBinding binding;
    private WeatherViewModel weatherViewModel;
    private WeatherLocation weatherLocation;
    private SharedPreferences preferences;
    private SavedLocationRepository savedLocationRepository;
    private SavedLocation currentLocation;
    private boolean isLocationSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.dark(Color.TRANSPARENT)
        );
        binding = ActivityCityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences(
                SettingsFragment.PREFS_NAME,
                Context.MODE_PRIVATE
        );
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String cityName = getIntent()
                .getStringExtra(SearchFragment.EXTRA_CITY_NAME);
        String cityRegion = getIntent()
                .getStringExtra(SearchFragment.CITY_REGION);
        String cityCountry = getIntent()
                .getStringExtra(SearchFragment.CITY_COUNTRY);
        double cityLatitude = getIntent()
                .getDoubleExtra(SearchFragment.CITY_LATITUDE, 0.0);
        double cityLongitude = getIntent()
                .getDoubleExtra(SearchFragment.CITY_LONGITUDE,0.0);
        currentLocation = new SavedLocation(
                cityName,
                cityRegion,
                cityCountry,
                cityLatitude,
                cityLongitude
        );

        savedLocationRepository = new SavedLocationRepository();
        binding.cityName.setText(cityName);
        binding.cityLocation.setText(
                String.format(
                        Locale.getDefault(),
                        "%s, %s",
                        cityRegion,
                        cityCountry
                )
        );

        weatherLocation = new WeatherLocation(
                cityName,
                cityRegion,
                cityCountry,
                cityLatitude,
                cityLongitude,
                R.drawable.baseline_cloud_24
        );

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        weatherViewModel.getWeatherData().observe(
                this,
                new Observer<WeatherData>() {

                    @Override
                    public void onChanged(WeatherData weatherData) {

                        if (weatherData != null) {

                            // Show successful weather screen
                            binding.weatherContent.setVisibility(View.VISIBLE);
                            binding.loadingContainer.setVisibility(View.GONE);
                            binding.errorContainer.setVisibility(View.GONE);

                            String selectedUnit = preferences.getString(
                                    SettingsFragment.KEY_TEMPERATURE_UNIT,
                                    SettingsFragment.UNIT_CELSIUS
                            );

                            if (SettingsFragment.UNIT_FAHRENHEIT.equals(selectedUnit)) {

                                // Fahrenheit is the main temperature
                                binding.tempC.setText(
                                        String.format(
                                                Locale.getDefault(),
                                                "%.0f°",
                                                weatherData.getTemperatureFahrenheit()
                                        )
                                );

                                binding.tempUnit.setText("F");

                                // Celsius becomes the smaller secondary temperature
                                binding.tempF.setText(
                                        String.format(
                                                Locale.getDefault(),
                                                "%.1f°C",
                                                weatherData.getTemperatureCelsius()
                                        )
                                );

                                double feelsLikeFahrenheit =
                                        weatherData.getFeelsLikeCelsius() * 9.0 / 5.0 + 32.0;

                                binding.feelLike.setText(
                                        String.format(
                                                Locale.getDefault(),
                                                "%.0f°F",
                                                feelsLikeFahrenheit
                                        )
                                );

                            } else {

                                // Celsius is the main temperature
                                binding.tempC.setText(
                                        String.format(
                                                Locale.getDefault(),
                                                "%.0f°",
                                                weatherData.getTemperatureCelsius()
                                        )
                                );

                                binding.tempUnit.setText("C");

                                // Fahrenheit becomes secondary
                                binding.tempF.setText(
                                        String.format(
                                                Locale.getDefault(),
                                                "%.1f°F",
                                                weatherData.getTemperatureFahrenheit()
                                        )
                                );

                                binding.feelLike.setText(
                                        String.format(
                                                Locale.getDefault(),
                                                "%.0f°C",
                                                weatherData.getFeelsLikeCelsius()
                                        )
                                );
                            }

                            // Wind
                            binding.windKph.setText(
                                    String.format(
                                            Locale.getDefault(),
                                            "%.1f kph",
                                            weatherData.getWindKph()
                                    )
                            );

                            // Humidity
                            binding.humidity.setText(
                                    String.format(
                                            Locale.getDefault(),
                                            "%d%%",
                                            weatherData.getHumidity()
                                    )
                            );

                            // Updated time
                            binding.lastUpdate.setText(
                                    weatherData.getLastUpdated()
                            );

                            // Weather condition
                            binding.weatherCondition.setText(
                                    weatherData.getCondition()
                            );

                            updateWeatherIcon(
                                    weatherData.getCondition()
                            );
                        }
                    }
                }
        );
        weatherViewModel.getErrorMessage().observe(
                this,
                new Observer<String>() {

                    @Override
                    public void onChanged(String errorMessage) {

                        if (errorMessage != null &&
                                !errorMessage.isEmpty()) {

                            binding.errorMessage.setText(
                                    errorMessage
                            );

                            binding.errorContainer.setVisibility(
                                    View.VISIBLE
                            );

                            binding.weatherContent.setVisibility(
                                    View.GONE
                            );

                            binding.loadingContainer.setVisibility(
                                    View.GONE
                            );
                        }
                    }
                }
        );
        weatherViewModel.getLoading().observe(
                this,
                new Observer<Boolean>() {

                    @Override
                    public void onChanged(Boolean isLoading) {

                        if (Boolean.TRUE.equals(isLoading)) {

                            binding.loadingContainer.setVisibility(
                                    View.VISIBLE
                            );

                            binding.weatherContent.setVisibility(
                                    View.GONE
                            );

                            binding.errorContainer.setVisibility(
                                    View.GONE
                            );

                        } else {

                            binding.loadingContainer.setVisibility(
                                    View.GONE
                            );
                        }
                    }
                }
        );
        weatherViewModel.loadWeather(weatherLocation);

        binding.btnRetry.setOnClickListener(v -> {
            weatherViewModel.loadWeather(weatherLocation);
        });

        binding.backBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        checkSavedStatus();

        binding.bookmarkBtn.setOnClickListener(v ->
                toggleSavedLocation()
        );
    }

    private void checkSavedStatus() {

        savedLocationRepository.isLocationSaved(
                currentLocation,
                new SavedLocationRepository.StatusCallback() {

                    @Override
                    public void onResult(boolean isSaved) {

                        isLocationSaved = isSaved;
                        updateBookmarkIcon();
                    }

                    @Override
                    public void onError(String message) {

                        Toast.makeText(
                                CityDetail.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void toggleSavedLocation() {

        if (isLocationSaved) {
            removeLocation();
        } else {
            saveLocation();
        }
    }

    private void saveLocation() {

        savedLocationRepository.saveLocation(
                currentLocation,
                new SavedLocationRepository.SaveCallback() {

                    @Override
                    public void onSaved() {

                        isLocationSaved = true;
                        updateBookmarkIcon();

                        Toast.makeText(
                                CityDetail.this,
                                "Location saved",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onAlreadySaved() {

                        isLocationSaved = true;
                        updateBookmarkIcon();

                        Toast.makeText(
                                CityDetail.this,
                                "Location is already saved",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onError(String message) {

                        Toast.makeText(
                                CityDetail.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void removeLocation() {

        savedLocationRepository.removeLocation(
                currentLocation,
                new SavedLocationRepository.RemoveCallback() {

                    @Override
                    public void onRemoved() {

                        isLocationSaved = false;
                        updateBookmarkIcon();

                        Toast.makeText(
                                CityDetail.this,
                                "Location removed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onError(String message) {

                        Toast.makeText(
                                CityDetail.this,
                                message,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void updateBookmarkIcon() {

        if (isLocationSaved) {

            binding.bookmarkBtn.setImageResource(
                    R.drawable.baseline_bookmark_24
            );

            binding.bookmarkBtn.setContentDescription(
                    "Remove saved location"
            );

        } else {

            binding.bookmarkBtn.setImageResource(
                    R.drawable.baseline_bookmark_border_24
            );

            binding.bookmarkBtn.setContentDescription(
                    "Save location"
            );
        }
    }

    private void updateWeatherIcon(String condition) {
        String weatherCondition = condition.toLowerCase(Locale.ROOT);

        if (weatherCondition.contains("sunny")
                || weatherCondition.contains("clear")) {

            binding.iconUrl.setImageResource(
                    R.drawable.outline_clear_day_24
            );

        } else if (weatherCondition.contains("rain")
                || weatherCondition.contains("drizzle")) {

            binding.iconUrl.setImageResource(
                    R.drawable.baseline_water_drop_24
            );

        } else if (weatherCondition.contains("snow")
                || weatherCondition.contains("sleet")
                || weatherCondition.contains("ice")) {

            binding.iconUrl.setImageResource(
                    R.drawable.outline_ac_unit_24
            );

        } else {

            binding.iconUrl.setImageResource(
                    R.drawable.baseline_cloud_24
            );
        }
    }

}