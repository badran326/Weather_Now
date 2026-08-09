package com.example.weathernow.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathernow.databinding.ItemWeatherLocationBinding;
import com.example.weathernow.model.WeatherLocation;

import java.util.List;

public class WeatherLocationAdapter
        extends RecyclerView.Adapter<WeatherLocationAdapter.LocationViewHolder> {

    private final List<WeatherLocation> locations;
    private final OnLocationClickListener listener;

    public WeatherLocationAdapter(List<WeatherLocation> locations, OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    public void updateLocations(List<WeatherLocation> newLocations) {
        locations.clear();
        locations.addAll(newLocations);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeatherLocationBinding binding = ItemWeatherLocationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LocationViewHolder holder,
            int position
    ) {
        holder.bind(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public interface OnLocationClickListener {
        void onLocationClick(WeatherLocation location);
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {

        private final ItemWeatherLocationBinding binding;

        public LocationViewHolder(
                @NonNull ItemWeatherLocationBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WeatherLocation location) {
            binding.tvCityName.setText(location.getCityName());

            String locationText =
                    location.getRegion() + ", " + location.getCountry();

            binding.tvRegion.setText(locationText);

            binding.imageWeatherIcon.setImageResource(
                    location.getIconResource()
            );

            binding.getRoot().setOnClickListener(view ->
                    listener.onLocationClick(location)
            );
        }
    }
}