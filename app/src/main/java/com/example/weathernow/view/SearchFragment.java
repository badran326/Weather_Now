package com.example.weathernow.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weathernow.R;
import com.example.weathernow.adapter.WeatherLocationAdapter;
import com.example.weathernow.databinding.FragmentSearchBinding;
import com.example.weathernow.model.WeatherLocation;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    public static final String EXTRA_CITY_NAME = "CITY_NAME";
    public static final String CITY_REGION = "CITY_REGION";
    public static final String CITY_COUNTRY = "CITY_COUNTRY";
    public static final String CITY_LATITUDE = "CITY_LATITUDE";
    public static final String CITY_LONGITUDE = "CITY_LONGITUDE";

    private FragmentSearchBinding binding;

    private final List<WeatherLocation> locations = new ArrayList<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSearchBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        initSearchDiagnostics();
        loadLocations();
        setupRecyclerView();
    }

    private void loadLocations() {
        locations.clear();

        locations.add(new WeatherLocation(
                "Toronto",
                "Ontario",
                "Canada",
                43.651605,
                -79.383125,
                R.drawable.baseline_cloud_24
        ));

        locations.add(new WeatherLocation(
                "Montreal",
                "Quebec",
                "Canada",
                45.50169,
                -73.567253,
                R.drawable.baseline_cloud_24
        ));

        locations.add(new WeatherLocation(
                "Sanaa",
                "Sanaa",
                "Yemen",
                15.3567,
                44.2002,
                R.drawable.baseline_cloud_24
        ));
    }

    private void setupRecyclerView() {
        WeatherLocationAdapter adapter =
                new WeatherLocationAdapter(
                        locations,
                        this::openCityDetail
                );

        binding.rvCities.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.rvCities.setHasFixedSize(true);
        binding.rvCities.setAdapter(adapter);
    }

    private void openCityDetail(WeatherLocation location) {
        Intent intent = new Intent(
                requireContext(),
                CityDetail.class
        );

        intent.putExtra(
                EXTRA_CITY_NAME,
                location.getCityName()
        );

        intent.putExtra(
                CITY_REGION,
                location.getRegion()
        );

        intent.putExtra(
                CITY_COUNTRY,
                location.getCountry()
        );

        intent.putExtra(
                CITY_LATITUDE,
                location.getLatitude()
        );

        intent.putExtra(
                CITY_LONGITUDE,
                location.getLongitude()
        );

        startActivity(intent);
    }

    private void initSearchDiagnostics() {
        Log.i(TAG, "search diagnostics enabled");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}