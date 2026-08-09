package com.example.weathernow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernow.model.WeatherLocation
import com.example.weathernow.repository.GeocodingRepository

class SearchViewModel : ViewModel() {

    private val repository = GeocodingRepository()

    private val _searchResults =
        MutableLiveData<List<WeatherLocation>>(emptyList())

    val searchResults: LiveData<List<WeatherLocation>>
        get() = _searchResults

    private val _loading =
        MutableLiveData(false)

    val loading: LiveData<Boolean>
        get() = _loading

    private val _errorMessage =
        MutableLiveData<String?>()

    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var latestQuery = ""

    fun searchCities(query: String) {

        val cleanQuery = query.trim()
        latestQuery = cleanQuery

        if (cleanQuery.isEmpty()) {
            _searchResults.value = emptyList()
            _loading.value = false
            _errorMessage.value = null
            return
        }

        _loading.value = true
        _errorMessage.value = null

        repository.searchCities(
            cleanQuery,
            object : GeocodingRepository.SearchCallback {

                override fun onSuccess(
                    locations: List<WeatherLocation>
                ) {
                    // Ignore an old response if the user
                    // has already searched for something else.
                    if (cleanQuery != latestQuery) {
                        return
                    }

                    _searchResults.postValue(locations)
                    _loading.postValue(false)
                }

                override fun onError(message: String) {

                    if (cleanQuery != latestQuery) {
                        return
                    }

                    _searchResults.postValue(emptyList())
                    _errorMessage.postValue(message)
                    _loading.postValue(false)
                }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        repository.cancelRequests()
    }
}