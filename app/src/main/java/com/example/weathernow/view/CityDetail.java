package com.example.weathernow.view;

import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
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

    private SavedLocationRepository savedLocationRepository;
    private SavedLocation currentLocation;
    private boolean isLocationSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

        WeatherLocation weatherLocation = new WeatherLocation(cityName, cityRegion, cityCountry, cityLatitude, cityLongitude, R.drawable.baseline_cloud_24);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        weatherViewModel.getWeatherData().observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                if (weatherData != null) {

                    binding.weatherContent.setVisibility(View.VISIBLE);
                    binding.errorCard.setVisibility(View.GONE);

                    binding.tempC.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1f °C",
                                    weatherData.getTemperatureCelsius()
                            )
                    );

                    binding.tempF.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1f °F",
                                    weatherData.getTemperatureFahrenheit()
                            )
                    );

                    binding.feelLike.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1f °C",
                                    weatherData.getFeelsLikeCelsius()
                            )
                    );

                    binding.windKph.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1f km/h",
                                    weatherData.getWindKph()
                            )
                    );

                    binding.uvIndex.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%.1f",
                                    weatherData.getUv()
                            )
                    );

                    binding.humidity.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%d%%",
                                    weatherData.getHumidity()
                            )
                    );
                    binding.lastUpdate.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "Last updated: %s",
                                    weatherData.getLastUpdated()
                            )
                    );
                    binding.weatherCondition.setText(weatherData.getCondition());

                    updateWeatherIcon(weatherData.getCondition());
                }
            }
        });
        weatherViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    binding.errorMessage.setText(errorMessage);
                    binding.errorCard.setVisibility(View.VISIBLE);
                    binding.weatherContent.setVisibility(View.GONE);
                }
            }
        });
        weatherViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.errorCard.setVisibility(View.GONE);
                    binding.weatherContent.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
        weatherViewModel.loadWeather(weatherLocation);


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