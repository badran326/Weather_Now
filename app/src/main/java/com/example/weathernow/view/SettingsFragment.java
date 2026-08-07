package com.example.weathernow.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weathernow.BuildConfig;
import com.example.weathernow.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvVersion.setText("Version " + BuildConfig.VERSION_NAME);

        // Display the signed-in user's email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.getEmail() != null) {
            binding.tvUserEmail.setText(currentUser.getEmail());
        } else {
            binding.tvUserEmail.setText("No user signed in");
        }

        // Sign out
        binding.btnSignOut.setOnClickListener(v -> signOut());

        // Existing Assignment 1 actions
        binding.btnSendFeedback.setOnClickListener(v -> sendFeedback());
        binding.btnViewGithub.setOnClickListener(v -> viewOnGithub());
        binding.btnShareApp.setOnClickListener(v -> shareApp());
    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@weathernow.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "WeatherNow Feedback");

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewOnGithub() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/"));
        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing Weather app: WeatherNow!");
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void signOut() {

        FirebaseAuth.getInstance().signOut();

        Toast.makeText(
                requireContext(),
                "Signed out successfully",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                requireContext(),
                LoginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}