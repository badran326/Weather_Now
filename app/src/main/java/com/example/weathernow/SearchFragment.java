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
            cityDetailActivity();
        });

        binding.cardNewYork.setOnClickListener(view -> {
            cityDetailActivity();
        });

        binding.cardSydney.setOnClickListener(view -> {
            cityDetailActivity();
        });

        binding.cardTokyo.setOnClickListener(view -> {
            cityDetailActivity();
        });

        binding.cardToronto.setOnClickListener(view -> {
            cityDetailActivity();
        });
        return binding.getRoot();
    }

    public void cityDetailActivity () {
        Intent intent = new Intent(getContext(), CityDetail.class);
        startActivity(intent);
    }

}