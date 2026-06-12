package com.example.weathernow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weathernow.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        binding.cardLondon.setOnClickListener(view -> {
            cityDetailActivity(String.valueOf(binding.tvLondonName.getText()));
        });

        binding.cardNewYork.setOnClickListener(view -> {
            cityDetailActivity(String.valueOf(binding.tvNewYorkName.getText()));
        });

        binding.cardSydney.setOnClickListener(view -> {
            cityDetailActivity(String.valueOf(binding.tvSydneyName.getText()));
        });

        binding.cardTokyo.setOnClickListener(view -> {
            cityDetailActivity(String.valueOf(binding.tvTokyoName.getText()));
        });

        binding.cardToronto.setOnClickListener(view -> {
            cityDetailActivity(String.valueOf(binding.tvTorontoName.getText()));
        });
        return binding.getRoot();
    }

    public void cityDetailActivity (String cityName) {
        Intent intent = new Intent(getContext(), CityDetail.class);
        intent.putExtra("CITY_NAME", cityName);
        startActivity(intent);

    }

}