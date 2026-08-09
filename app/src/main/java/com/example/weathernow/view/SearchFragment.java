package com.example.weathernow.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weathernow.adapter.WeatherLocationAdapter;
import com.example.weathernow.databinding.FragmentSearchBinding;
import com.example.weathernow.model.WeatherLocation;
import com.example.weathernow.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    public static final String EXTRA_CITY_NAME = "CITY_NAME";
    public static final String CITY_REGION = "CITY_REGION";
    public static final String CITY_COUNTRY = "CITY_COUNTRY";
    public static final String CITY_LATITUDE = "CITY_LATITUDE";
    public static final String CITY_LONGITUDE = "CITY_LONGITUDE";

    private static final long SEARCH_DELAY = 300L;

    private FragmentSearchBinding binding;

    private WeatherLocationAdapter adapter;
    private SearchViewModel searchViewModel;

    private final Handler searchHandler =
            new Handler(Looper.getMainLooper());

    private Runnable searchRunnable;

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

        searchViewModel = new ViewModelProvider(this)
                .get(SearchViewModel.class);

        setupRecyclerView();
        setupSearch();
        observeSearchData();

        showStartMessage();
    }

    private void setupRecyclerView() {

        adapter = new WeatherLocationAdapter(
                new ArrayList<>(),
                this::openCityDetail
        );

        binding.rvCities.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.rvCities.setHasFixedSize(true);
        binding.rvCities.setAdapter(adapter);
    }

    private void setupSearch() {

        binding.searchBar.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        // Cancel the previous delayed search
                        if (searchRunnable != null) {
                            searchHandler.removeCallbacks(searchRunnable);
                        }

                        String query = newText.trim();

                        // If the search box is empty,
                        // reset the screen.
                        if (query.isEmpty()) {

                            searchViewModel.searchCities("");

                            adapter.updateLocations(
                                    Collections.emptyList()
                            );

                            showStartMessage();

                            return true;
                        }

                        // Clear old results while waiting
                        // for the new search.
                        adapter.updateLocations(
                                Collections.emptyList()
                        );

                        binding.tvEmptyState.setVisibility(View.GONE);

                        // Wait 300ms after the user stops typing
                        searchRunnable = () ->
                                searchViewModel.searchCities(query);

                        searchHandler.postDelayed(
                                searchRunnable,
                                SEARCH_DELAY
                        );

                        return true;
                    }
                }
        );
    }

    private void observeSearchData() {

        searchViewModel.getSearchResults().observe(
                getViewLifecycleOwner(),
                locations -> {

                    adapter.updateLocations(locations);

                    String currentQuery =
                            binding.searchBar
                                    .getQuery()
                                    .toString()
                                    .trim();

                    if (currentQuery.isEmpty()) {

                        showStartMessage();

                    } else if (locations.isEmpty()) {

                        binding.tvEmptyState.setText(
                                "No cities found. Try another search."
                        );

                        binding.tvEmptyState.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        binding.tvEmptyState.setVisibility(
                                View.GONE
                        );
                    }
                }
        );

        searchViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                isLoading -> {

                    binding.progressSearch.setVisibility(
                            Boolean.TRUE.equals(isLoading)
                                    ? View.VISIBLE
                                    : View.GONE
                    );

                    if (Boolean.TRUE.equals(isLoading)) {
                        binding.tvEmptyState.setVisibility(
                                View.GONE
                        );
                    }
                }
        );

        searchViewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                errorMessage -> {

                    if (errorMessage != null &&
                            !errorMessage.isEmpty()) {

                        binding.tvEmptyState.setText(
                                errorMessage
                        );

                        binding.tvEmptyState.setVisibility(
                                View.VISIBLE
                        );
                    }
                }
        );
    }

    private void showStartMessage() {

        binding.progressSearch.setVisibility(View.GONE);

        binding.tvEmptyState.setText(
                "Start typing to search for a city"
        );

        binding.tvEmptyState.setVisibility(View.VISIBLE);
    }

    private void openCityDetail(
            WeatherLocation location
    ) {

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

        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }

        super.onDestroyView();
        binding = null;
    }
}