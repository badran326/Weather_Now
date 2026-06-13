package com.example.weathernow;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weathernow.databinding.ActivityWeatherDetailBinding;

public class WeatherDetailActivity extends AppCompatActivity {

    private ActivityWeatherDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityWeatherDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            binding.toolbar.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Weather Detail");
        }

        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        String cityName = getIntent().getStringExtra("CITY_NAME");
        String cityDetails = getIntent().getStringExtra("CITY_DETAILS");
        String cityDegree = getIntent().getStringExtra("CITY_DEGREE");
        String cityCondition = getIntent().getStringExtra("CITY_CONDITION");

        binding.tvCityName.setText(cityName);
        binding.tvCityDetails.setText(cityDetails);
        binding.tvConditionText.setText(cityCondition);
        binding.tvTemperatureC.setText(cityDegree + "C");
        
        try {
            int c = Integer.parseInt(cityDegree.replace("°", ""));
            int f = (int) (c * 1.8 + 32);
            binding.tvTemperatureF.setText(f + "°F");
            binding.tvFeelsLike.setText((c - 2) + "°C");
        } catch (Exception e) {
            binding.tvTemperatureF.setText("57°F");
            binding.tvFeelsLike.setText("12°C");
        }

        binding.tvHumidity.setText("78%");
        binding.tvWindSpeed.setText("18 kph");
        binding.tvUVIndex.setText("2 Low");
    }
}