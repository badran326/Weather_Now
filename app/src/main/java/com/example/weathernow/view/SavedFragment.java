package com.example.weathernow.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weathernow.adapter.SavedLocationAdapter;
import com.example.weathernow.databinding.FragmentSavedBinding;
import com.example.weathernow.model.SavedLocation;
import com.example.weathernow.viewmodel.SavedLocationViewModel;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    private SavedLocationAdapter adapter;
    private SavedLocationViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentSavedBinding.inflate(
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

        viewModel = new ViewModelProvider(this)
                .get(SavedLocationViewModel.class);

        setupRecyclerView();
        observeData();

        viewModel.startListening();
    }

    private void setupRecyclerView() {

        adapter = new SavedLocationAdapter(
                this::openLocation,
                this::removeLocation
        );

        binding.rvSavedLocations.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.rvSavedLocations.setAdapter(adapter);
    }

    private void observeData() {

        viewModel.getLocations().observe(
                getViewLifecycleOwner(),
                locations -> {

                    adapter.updateLocations(locations);

                    boolean isEmpty =
                            locations == null ||
                                    locations.isEmpty();

                    binding.emptyState.setVisibility(
                            isEmpty
                                    ? View.VISIBLE
                                    : View.GONE
                    );

                    binding.rvSavedLocations.setVisibility(
                            isEmpty
                                    ? View.GONE
                                    : View.VISIBLE
                    );
                }
        );

        viewModel.getMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null) {

                        Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_SHORT
                        ).show();

                        viewModel.clearMessage();
                    }
                }
        );

        viewModel.getErrorMessage().observe(
                getViewLifecycleOwner(),
                message -> {

                    if (message != null) {

                        Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_LONG
                        ).show();

                        viewModel.clearError();
                    }
                }
        );
    }

    private void removeLocation(
            SavedLocation location
    ) {

        viewModel.removeLocation(location);
    }

    private void openLocation(
            SavedLocation location
    ) {

        Intent intent = new Intent(
                requireContext(),
                CityDetail.class
        );

        intent.putExtra(
                SearchFragment.EXTRA_CITY_NAME,
                location.getCityName()
        );

        intent.putExtra(
                SearchFragment.CITY_REGION,
                location.getRegion()
        );

        intent.putExtra(
                SearchFragment.CITY_COUNTRY,
                location.getCountry()
        );

        intent.putExtra(
                SearchFragment.CITY_LATITUDE,
                location.getLatitude()
        );

        intent.putExtra(
                SearchFragment.CITY_LONGITUDE,
                location.getLongitude()
        );

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}