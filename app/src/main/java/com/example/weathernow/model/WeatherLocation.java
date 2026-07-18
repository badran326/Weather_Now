package com.example.weathernow.model;

public class WeatherLocation {
    private final String cityName;
    private final String region;
    private final String country;
    private final double latitude;
    private final double longitude;
    private final int iconResource;

    public WeatherLocation(String cityName, String region, String country, double latitude, double longitude, int iconResource) {
        this.cityName = cityName;
        this.region = region;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iconResource = iconResource;
    }

    public String getCityName() {
        return cityName;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getIconResource() {
        return iconResource;
    }
}
