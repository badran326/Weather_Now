package com.example.weathernow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernow.model.SavedLocation
import com.example.weathernow.repository.SavedLocationRepository
import com.google.firebase.firestore.ListenerRegistration

class SavedLocationViewModel : ViewModel() {

    private val repository =
        SavedLocationRepository()

    private var listenerRegistration:
            ListenerRegistration? = null

    private val _locations =
        MutableLiveData<List<SavedLocation>>(emptyList())

    val locations: LiveData<List<SavedLocation>>
        get() = _locations

    private val _errorMessage =
        MutableLiveData<String?>()

    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _message =
        MutableLiveData<String?>()

    val message: LiveData<String?>
        get() = _message

    fun startListening() {

        listenerRegistration?.remove()

        listenerRegistration =
            repository.listenToSavedLocations(
                object :
                    SavedLocationRepository.SavedLocationsCallback {

                    override fun onLocationsChanged(
                        locations: List<SavedLocation>
                    ) {
                        _locations.postValue(locations)
                    }

                    override fun onError(message: String) {
                        _errorMessage.postValue(message)
                    }
                }
            )
    }

    fun removeLocation(
        location: SavedLocation
    ) {

        repository.removeLocation(
            location,
            object :
                SavedLocationRepository.RemoveCallback {

                override fun onRemoved() {
                    _message.postValue(
                        "Location removed"
                    )
                }

                override fun onError(message: String) {
                    _errorMessage.postValue(message)
                }
            }
        )
    }

    fun clearMessage() {
        _message.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()

        listenerRegistration?.remove()
    }
}