package com.example.weathernow.model;

public class WeatherData {

    private final String locationName;
    private final String region;
    private final String country;
    private final double temperatureCelsius;
    private final double temperatureFahrenheit;
    private final String condition;
    private final double windKph;
    private final double feelsLikeCelsius;
    private final double uv;
    private final int humidity;
    private final String lastUpdated;
    private  final String iconUrl;

    public WeatherData(String locationName, String region, String country, double temperatureCelsius, double temperatureFahrenheit, String condition, double windKph, double feelsLikeCelsius, double uv, int humidity, String lastUpdated, String iconUrl
    ) {
        this.locationName = locationName;
        this.region = region;
        this.country = country;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = temperatureFahrenheit;
        this.condition = condition;
        this.windKph = windKph;
        this.feelsLikeCelsius = feelsLikeCelsius;
        this.uv = uv;
        this.humidity = humidity;
        this.lastUpdated = lastUpdated;
        this.iconUrl = iconUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public String getCondition() {
        return condition;
    }

    public double getWindKph() {
        return windKph;
    }

    public double getFeelsLikeCelsius() {
        return feelsLikeCelsius;
    }

    public double getUv() {
        return uv;
    }
    public int getHumidity() {
        return humidity;
    }


    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}