package com.example.weathernow.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathernow.databinding.ItemSavedLocationBinding;
import com.example.weathernow.model.SavedLocation;

import java.util.ArrayList;
import java.util.List;

public class SavedLocationAdapter
        extends RecyclerView.Adapter<SavedLocationAdapter.SavedLocationViewHolder> {

    private final List<SavedLocation> locations = new ArrayList<>();

    private final OnLocationClickListener locationClickListener;
    private final OnRemoveClickListener removeClickListener;

    public SavedLocationAdapter(
            OnLocationClickListener locationClickListener,
            OnRemoveClickListener removeClickListener
    ) {
        this.locationClickListener = locationClickListener;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public SavedLocationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        ItemSavedLocationBinding binding =
                ItemSavedLocationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new SavedLocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SavedLocationViewHolder holder,
            int position
    ) {
        holder.bind(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void updateLocations(List<SavedLocation> newLocations) {

        locations.clear();
        locations.addAll(newLocations);

        notifyDataSetChanged();
    }

    public interface OnLocationClickListener {
        void onLocationClick(SavedLocation location);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(SavedLocation location);
    }

    class SavedLocationViewHolder
            extends RecyclerView.ViewHolder {

        private final ItemSavedLocationBinding binding;

        public SavedLocationViewHolder(
                @NonNull ItemSavedLocationBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SavedLocation location) {

            binding.tvCityName.setText(
                    location.getCityName()
            );

            String locationText;

            if (location.getRegion() != null &&
                    !location.getRegion().isEmpty()) {

                locationText =
                        location.getRegion()
                                + ", "
                                + location.getCountry();

            } else {

                locationText =
                        location.getCountry();
            }

            binding.tvCountry.setText(locationText);

            binding.getRoot().setOnClickListener(v ->
                    locationClickListener.onLocationClick(location)
            );

            binding.btnRemove.setOnClickListener(v ->
                    removeClickListener.onRemoveClick(location)
            );
        }
    }
}