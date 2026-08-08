package com.example.weathernow.repository

import com.example.weathernow.R
import com.example.weathernow.model.WeatherLocation
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class GeocodingRepository {

    private val client = OkHttpClient()

    interface SearchCallback {
        fun onSuccess(locations: List<WeatherLocation>)
        fun onError(message: String)
    }

    fun searchCities(
        query: String,
        callback: SearchCallback
    ) {

        if (query.isBlank()) {
            callback.onSuccess(emptyList())
            return
        }

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("geocoding-api.open-meteo.com")
            .addPathSegments("v1/search")
            .addQueryParameter("name", query)
            .addQueryParameter("count", "10")
            .addQueryParameter("language", "en")
            .addQueryParameter("format", "json")
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(
            object : Callback {

                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {
                    callback.onError(
                        e.message ?: "Unable to search for cities"
                    )
                }

                override fun onResponse(
                    call: Call,
                    response: Response
                ) {
                    response.use {

                        if (!response.isSuccessful) {
                            callback.onError(
                                "Search failed. Please try again."
                            )
                            return
                        }

                        val responseBody =
                            response.body?.string()

                        if (responseBody.isNullOrBlank()) {
                            callback.onSuccess(emptyList())
                            return
                        }

                        try {
                            val locations =
                                parseSearchResults(responseBody)

                            callback.onSuccess(locations)

                        } catch (e: Exception) {
                            callback.onError(
                                "Unable to read search results"
                            )
                        }
                    }
                }
            }
        )
    }

    private fun parseSearchResults(
        json: String
    ): List<WeatherLocation> {

        val locations = mutableListOf<WeatherLocation>()

        val root = JSONObject(json)

        val results =
            root.optJSONArray("results")
                ?: return emptyList()

        for (i in 0 until results.length()) {

            val item = results.getJSONObject(i)

            val cityName =
                item.optString("name")

            val region =
                item.optString("admin1")

            val country =
                item.optString("country")

            val latitude =
                item.optDouble("latitude")

            val longitude =
                item.optDouble("longitude")

            if (cityName.isNotBlank() &&
                country.isNotBlank()
            ) {

                locations.add(
                    WeatherLocation(
                        cityName,
                        region,
                        country,
                        latitude,
                        longitude,
                        R.drawable.baseline_cloud_24
                    )
                )
            }
        }

        return locations
    }

    fun cancelRequests() {
        client.dispatcher.cancelAll()
    }
}