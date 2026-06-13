package com.example.weathernow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weathernow.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewCities.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<City> cities = new ArrayList<>();
        cities.add(new City("London", "City of London, Greater London", "14°", "Cloudy", "\uD83C\uDDEC\uD83C\uDDE7"));
        cities.add(new City("Toronto", "Ontario", "22°", "Sunny", "\uD83C\uDDE8\uD83C\uDDE6"));
        cities.add(new City("Tokyo", "Tokyo-to", "28°", "Clear", "\uD83C\uDDEF\uD83C\uDDF5"));
        cities.add(new City("Sydney", "New South Wales", "20°", "Sunny", "\uD83C\uDDE6\uD83C\uDDFA"));
        cities.add(new City("New York", "New York State", "11°", "Rainy", "\uD83C\uDDFA\uD83C\uDDF8"));

        CityAdapter adapter = new CityAdapter(cities, city -> {
            Intent intent = new Intent(getContext(), WeatherDetailActivity.class);
            intent.putExtra("CITY_NAME", city.getName());
            intent.putExtra("CITY_DETAILS", city.getDetails());
            intent.putExtra("CITY_DEGREE", city.getDegree());
            intent.putExtra("CITY_CONDITION", city.getCondition());
            intent.putExtra("CITY_FLAG", city.getFlagEmoji());
            startActivity(intent);
        });
        binding.recyclerViewCities.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}