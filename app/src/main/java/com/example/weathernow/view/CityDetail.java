package com.example.weathernow.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.weathernow.R;
import com.example.weathernow.databinding.ActivityCityDetailBinding;
import com.example.weathernow.model.WeatherData;
import com.example.weathernow.model.WeatherLocation;
import com.example.weathernow.viewmodel.WeatherViewModel;

import java.util.Locale;

public class CityDetail extends AppCompatActivity {

    ActivityCityDetailBinding binding;
    private WeatherViewModel weatherViewModel;

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

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        weatherViewModel.getWeatherData().observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                if (weatherData != null) {
                    binding.cityName.setText(weatherData.getLocationName());
                    binding.cityLocation.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "%s, %s",
                                    weatherData.getRegion(),
                                    weatherData.getCountry()
                            )
                    );

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
                    String iconUrl = weatherData.getIconUrl();

                    if (iconUrl != null && iconUrl.startsWith("//")) {
                        iconUrl = "https:" + iconUrl;
                    }

                    Glide.with(CityDetail.this)
                            .load(iconUrl)
                            .into(binding.iconUrl);
                }
            }
        });
        weatherViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(CityDetail.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        weatherViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
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

        WeatherLocation weatherLocation = new WeatherLocation(cityName, cityRegion, cityCountry, cityLatitude, cityLongitude, R.drawable.baseline_cloud_24);
        weatherViewModel.loadWeather(weatherLocation);


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}