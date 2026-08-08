package com.example.weathernow.model

data class SavedLocation(
    var cityName: String = "",
    var region: String = "",
    var country: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)