package com.example.weathernow.repository;


import androidx.annotation.NonNull;

import com.example.weathernow.BuildConfig;
import com.example.weathernow.model.WeatherData;
import com.example.weathernow.model.WeatherLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherRepository {

    private final OkHttpClient client;

    public WeatherRepository() {
        client = new OkHttpClient();
    }

    public interface WeatherCallback {
        void onSuccess(WeatherData weatherData);
        void onError(String errorMessage);
    }

    public void getWeather(WeatherLocation location, WeatherCallback callback) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String coordinates = latitude + "," + longitude;
        HttpUrl baseUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.weatherapi.com")
                .addPathSegment("v1")
                .addPathSegment("current.json")
                .addQueryParameter("q", coordinates)
                .addQueryParameter("aqi", "no")
                .addQueryParameter("key", BuildConfig.WEATHER_API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage() != null ? e.getMessage() : "Network request failed.");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);

                        JSONObject location = jsonObject.getJSONObject("location");
                        JSONObject current = jsonObject.getJSONObject("current");

                        String locationName = location.getString("name");
                        String region = location.getString("region");
                        String country = location.getString("country");

                        double tempC = current.getDouble("temp_c");
                        double tempF = current.getDouble("temp_f");
                        double windKph = current.getDouble("wind_kph");
                        double feelsLikeC = current.getDouble("feelslike_c");
                        double uv = current.getDouble("uv");

                        int humidity = current.getInt("humidity");

                        String lastUpdated = current.getString("last_updated");

                        JSONObject conditionObject = current.getJSONObject("condition");
                        String condition = conditionObject.getString("text");
                        String iconUrl = conditionObject.getString("icon");

                        WeatherData weatherData = new WeatherData(locationName, region, country, tempC, tempF, condition, windKph, feelsLikeC, uv, humidity, lastUpdated, iconUrl);

                        callback.onSuccess(weatherData);


                    } catch (JSONException e) {
                        callback.onError(e.getMessage() != null ? e.getMessage() : "Network request failed.");
                    }
                } else {
                    callback.onError("Weather request failed. Error code: " + response.code());
                }
            }
        });
    }

}