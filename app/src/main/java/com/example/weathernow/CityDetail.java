package com.example.weathernow;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weathernow.databinding.ActivityCityDetailBinding;

public class CityDetail extends AppCompatActivity {

    ActivityCityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


}